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

final class MotorJointSample extends AbstractBox3DSample {
    private final B3Body target;
    private float time;

    MotorJointSample() {
        addGroundBox(20.0f);

        B3Body ground = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        target = createBody(B3.KinematicBody(), 0.0f, 10.0f, 0.0f, null);

        B3Body body = addDynamicBox(0.0f, 10.0f, 0.0f, 1.0f, 0.25f, 0.25f);

        B3MotorJointDef motorDef = new B3MotorJointDef();
        motorDef.SetBodyIdA(target.GetId());
        motorDef.SetBodyIdB(body.GetId());
        motorDef.SetLinearHertz(4.0f);
        motorDef.SetLinearDampingRatio(0.7f);
        motorDef.SetAngularHertz(4.0f);
        motorDef.SetAngularDampingRatio(0.7f);
        motorDef.SetMaxSpringForce(400000.0f);
        motorDef.SetMaxSpringTorque(500000.0f);
        dispose(world().CreateMotorJoint(motorDef));

        B3Body springBody = addDynamicBox(-2.0f, 2.0f, 0.0f, 0.5f, 0.5f, 0.5f);
        B3MotorJointDef springDef = new B3MotorJointDef();
        springDef.SetBodyIdA(ground.GetId());
        springDef.SetBodyIdB(springBody.GetId());
        JointSampleUtil.setLocalPositionA(springDef, -1.75f, 3.25f, 0.0f);
        JointSampleUtil.setLocalPositionB(springDef, 0.25f, 0.25f, 0.0f);
        springDef.SetCollideConnected(true);
        springDef.SetLinearHertz(7.5f);
        springDef.SetLinearDampingRatio(0.7f);
        springDef.SetAngularHertz(7.5f);
        springDef.SetAngularDampingRatio(0.7f);
        springDef.SetMaxSpringForce(200000.0f);
        springDef.SetMaxSpringTorque(10000.0f);
        dispose(world().CreateMotorJoint(springDef));

        dispose(springDef, motorDef);
    }

    @Override
    public void step(float deltaSeconds) {
        time += deltaSeconds * 0.75f;
        B3Vec3 position = new B3Vec3(6.0f * (float)Math.sin(2.0f * time),
                10.0f + 4.0f * (float)Math.sin(time), 0.0f);
        B3Quat rotation = rotationZ(2.0f * time);
        target.SetTransform(position, rotation);
        dispose(rotation, position);
        super.step(deltaSeconds);
    }
}
