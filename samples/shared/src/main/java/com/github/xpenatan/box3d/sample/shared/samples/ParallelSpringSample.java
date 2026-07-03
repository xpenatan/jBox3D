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

final class ParallelSpringSample extends AbstractBox3DSample {
    ParallelSpringSample() {
        addGroundBox(20.0f);

        B3Body ground = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        addBoxShape(ground, 20.0f, 5.0f, 0.1f, 0.0f, 5.0f, -20.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 20.0f, 5.0f, 0.1f, 0.0f, 5.0f, 20.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 0.1f, 5.0f, 20.0f, -20.0f, 5.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 0.1f, 5.0f, 20.0f, 20.0f, 5.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);

        B3Quat bodyRotation = rotationX(0.25f * (float)Math.PI);
        B3Body body = addDynamicBox(0.0f, 4.0f, 0.0f, 0.5f, 1.5f, 0.25f, bodyRotation, 1.0f, 0.6f, 0.0f, 0.0f);
        B3Quat axisA = rotationX(-0.5f * (float)Math.PI);
        B3Quat axisB = rotationX(-0.75f * (float)Math.PI);

        B3ParallelJointDef jointDef = new B3ParallelJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(body.GetId());
        JointSampleUtil.setLocalFrameA(jointDef, 0.0f, 0.0f, 0.0f, axisA);
        JointSampleUtil.setLocalFrameB(jointDef, 0.0f, 0.0f, 0.0f, axisB);
        jointDef.SetDrawScale(2.0f);
        jointDef.SetCollideConnected(true);
        jointDef.SetHertz(10.0f);
        jointDef.SetDampingRatio(0.7f);
        jointDef.SetMaxTorque(5000.0f);
        dispose(world().CreateParallelJoint(jointDef));

        dispose(jointDef, axisB, axisA, bodyRotation);
    }
}
