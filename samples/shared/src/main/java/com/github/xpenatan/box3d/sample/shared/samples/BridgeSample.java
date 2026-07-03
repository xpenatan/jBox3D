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

final class BridgeSample extends AbstractBox3DSample {
    private static final int COUNT = 150;

    BridgeSample() {
        addGroundBox(60.0f);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);

        float a = 0.125f;
        float xBase = -160.0f * a;
        B3ShapeDef shapeDef = shapeDef(20.0f, 0.6f, 0.0f, 0.0f);
        B3Hull box = B3Hull.CreateBox(a, 0.125f, 0.5f);

        B3SphericalJointDef jointDef = new B3SphericalJointDef();
        jointDef.SetConstraintHertz(1000.0f);
        jointDef.SetEnableSpring(true);
        jointDef.SetHertz(2.0f);
        jointDef.SetDampingRatio(1.0f);

        B3Body previous = ground;
        float previousX = 0.0f;
        float previousY = 0.0f;
        for(int i = 0; i < COUNT; i++) {
            float bodyX = xBase + a * (1.0f + 2.0f * i);
            float bodyY = 20.0f;
            B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), bodyX, bodyY, 0.0f, null);
            bodyDef.SetLinearDamping(0.1f);
            bodyDef.SetAngularDamping(0.1f);
            B3Body body = world().CreateBody(bodyDef);
            dispose(body.CreateHullShape(shapeDef, box));

            createBridgeJoint(jointDef, previous, body, xBase + 2.0f * a * i, 20.0f, -0.5f, previousX, previousY, bodyX, bodyY);
            createBridgeJoint(jointDef, previous, body, xBase + 2.0f * a * i, 20.0f, 0.5f, previousX, previousY, bodyX, bodyY);

            previous = body;
            previousX = bodyX;
            previousY = bodyY;
            dispose(bodyDef);
        }

        createBridgeJoint(jointDef, previous, ground, xBase + 2.0f * a * COUNT, 20.0f, -0.5f, previousX, previousY, 0.0f, 0.0f);
        createBridgeJoint(jointDef, previous, ground, xBase + 2.0f * a * COUNT, 20.0f, 0.5f, previousX, previousY, 0.0f, 0.0f);

        dispose(jointDef, box, shapeDef);
    }

    private void createBridgeJoint(B3SphericalJointDef jointDef, B3Body bodyA, B3Body bodyB, float pivotX,
            float pivotY, float pivotZ, float bodyAX, float bodyAY, float bodyBX, float bodyBY) {
        jointDef.SetBodyIdA(bodyA.GetId());
        jointDef.SetBodyIdB(bodyB.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, pivotX - bodyAX, pivotY - bodyAY, pivotZ);
        JointSampleUtil.setLocalPositionB(jointDef, pivotX - bodyBX, pivotY - bodyBY, pivotZ);
        dispose(world().CreateSphericalJoint(jointDef));
    }
}
