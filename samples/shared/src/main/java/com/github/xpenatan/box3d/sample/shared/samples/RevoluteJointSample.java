package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3DistanceJointDef;
import com.github.xpenatan.box3d.B3FilterJointDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3MotorJointDef;
import com.github.xpenatan.box3d.B3ParallelJointDef;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3RevoluteJointDef;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WeldJointDef;
import com.github.xpenatan.box3d.B3WheelJointDef;

final class RevoluteJointSample extends AbstractBox3DSample {
    RevoluteJointSample() {
        addGroundBox(20.0f);

        B3Body ground = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        B3Body body = addDynamicBox(0.0f, 4.0f, 0.0f, 0.5f, 1.5f, 0.25f);

        B3RevoluteJointDef jointDef = new B3RevoluteJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(body.GetId());
        jointDef.SetDrawScale(2.0f);
        jointDef.SetEnableLimit(false);
        jointDef.SetLowerAngle(degreesToRadians(-35.0f));
        jointDef.SetUpperAngle(degreesToRadians(35.0f));
        jointDef.SetEnableSpring(false);
        jointDef.SetHertz(2.0f);
        jointDef.SetDampingRatio(0.7f);
        setLocalPositionA(jointDef, 0.0f, 6.5f, 0.0f);
        setLocalPositionB(jointDef, 0.0f, 1.5f, 0.0f);
        dispose(world().CreateRevoluteJoint(jointDef));
        dispose(jointDef);
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

    private float degreesToRadians(float degrees) {
        return (float)(degrees * Math.PI / 180.0);
    }
}
