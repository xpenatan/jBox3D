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

final class PrismaticJointSample extends AbstractBox3DSample {
    PrismaticJointSample() {
        addGroundBox(20.0f);

        B3Body ground = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, 4.0f, 0.0f, null);
        bodyDef.SetGravityScale(0.0f);
        B3Body body = world().CreateBody(bodyDef);
        B3Shape shape = createBoxShape(body, 0.5f, 1.5f, 0.25f, 1.0f, 0.6f, 0.0f, 0.0f);

        B3PrismaticJointDef jointDef = new B3PrismaticJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(body.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, 0.0f, 6.5f, 0.0f);
        JointSampleUtil.setLocalPositionB(jointDef, 0.0f, 1.5f, 0.0f);
        jointDef.SetEnableSpring(true);
        jointDef.SetHertz(2.0f);
        jointDef.SetDampingRatio(0.7f);
        jointDef.SetLowerTranslation(-1.0f);
        jointDef.SetUpperTranslation(1.0f);
        jointDef.SetMaxMotorForce(20.0f);
        dispose(world().CreatePrismaticJoint(jointDef));

        dispose(jointDef, shape, bodyDef);
    }
}
