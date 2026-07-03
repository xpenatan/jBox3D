package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3FilterJointDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Joint;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3RevoluteJointDef;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3Vec3;

final class WindFlapSample extends AbstractBox3DSample {
    private final B3Shape wingShape1;
    private final B3Shape wingShape2;
    private final B3Joint joint1;
    private final B3Joint joint2;
    private float time;

    WindFlapSample() {
        addGroundBox(50.0f);

        float a = 0.4f;
        float y = 20.0f;
        B3Quat wingRotation = rotationX(0.1f);

        B3Body wingBody1 = createBody(B3.DynamicBody(), -2.0f * a, y, 0.0f, null);
        wingShape1 = createBoxShape(wingBody1, 2.0f * a, 0.01f, a, 0.0f, 0.0f, 0.0f, wingRotation, 5.0f,
                0.6f, 0.0f, 0.0f);

        B3Body wingBody2 = createBody(B3.DynamicBody(), 2.0f * a, y, 0.0f, null);
        wingShape2 = createBoxShape(wingBody2, 2.0f * a, 0.01f, a, 0.0f, 0.0f, 0.0f, wingRotation, 5.0f,
                0.6f, 0.0f, 0.0f);

        B3Body torso = createBody(B3.DynamicBody(), 0.0f, y, 0.0f, null);
        dispose(createCapsuleShape(torso, 0.0f, 0.0f, -a, 0.0f, 0.0f, a, 0.25f * a, 10.0f, 0.6f,
                0.0f, 0.0f));

        B3RevoluteJointDef jointDef = new B3RevoluteJointDef();
        jointDef.SetDrawScale(0.1f);
        jointDef.SetBodyIdA(torso.GetId());
        setLocalPositionA(jointDef, 0.0f, 0.0f, 0.0f);
        jointDef.SetBodyIdB(wingBody1.GetId());
        setLocalPositionB(jointDef, 2.0f * a, 0.0f, 0.0f);
        jointDef.SetEnableSpring(true);
        jointDef.SetHertz(6.0f);
        jointDef.SetDampingRatio(0.5f);
        jointDef.SetEnableLimit(true);
        jointDef.SetLowerAngle((float)(-30.0 * Math.PI / 180.0));
        jointDef.SetUpperAngle((float)(30.0 * Math.PI / 180.0));
        joint1 = world().CreateRevoluteJoint(jointDef);

        jointDef.SetBodyIdB(wingBody2.GetId());
        setLocalPositionB(jointDef, -2.0f * a, 0.0f, 0.0f);
        joint2 = world().CreateRevoluteJoint(jointDef);

        B3FilterJointDef filterDef = new B3FilterJointDef();
        filterDef.SetBodyIdA(wingBody1.GetId());
        filterDef.SetBodyIdB(wingBody2.GetId());
        dispose(world().CreateFilterJoint(filterDef));
        dispose(filterDef, jointDef, wingRotation);
    }

    @Override
    public void step(float deltaSeconds) {
        B3Vec3 wind = new B3Vec3(0.0f, 0.0f, 0.0f);
        wingShape1.ApplyWind(wind, 1.0f, 2.0f, 10.0f, false);
        wingShape2.ApplyWind(wind, 1.0f, 2.0f, 10.0f, false);
        dispose(wind);

        float angle = (float)Math.sin(10.0f * time);
        joint1.SetRevoluteTargetAngle(angle);
        joint2.SetRevoluteTargetAngle(-angle);
        time += Math.max(1.0f / 240.0f, Math.min(deltaSeconds, 1.0f / 30.0f));
        super.step(deltaSeconds);
    }

    private void setLocalPositionA(B3RevoluteJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionA(position);
        dispose(position);
    }

    private void setLocalPositionB(B3RevoluteJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionB(position);
        dispose(position);
    }
}
