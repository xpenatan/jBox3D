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

final class WheelJointSample extends AbstractBox3DSample {
    WheelJointSample() {
        addGroundBox(20.0f);

        B3Body ground = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        B3Quat wheelRotation = rotationX(0.5f * (float)Math.PI);
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, 2.0f, 0.0f, wheelRotation);
        B3Body body = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        B3Hull cylinder = B3Hull.CreateCylinder(0.25f, 0.4f, 0.0f, 12);
        dispose(body.CreateHullShape(shapeDef, cylinder));

        B3Quat axisA = rotationZ(0.5f * (float)Math.PI);
        B3Quat axisB = rotationX(-0.5f * (float)Math.PI);
        B3WheelJointDef jointDef = new B3WheelJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(body.GetId());
        JointSampleUtil.setLocalFrameA(jointDef, 0.0f, 3.0f, 0.0f, axisA);
        JointSampleUtil.setLocalFrameB(jointDef, 0.0f, 0.0f, 0.0f, axisB);
        jointDef.SetCollideConnected(true);
        jointDef.SetLowerSuspensionLimit(-1.0f);
        jointDef.SetUpperSuspensionLimit(1.0f);
        jointDef.SetSuspensionHertz(2.0f);
        jointDef.SetSuspensionDampingRatio(0.7f);
        jointDef.SetMaxSpinTorque(20.0f);
        jointDef.SetSteeringHertz(1.0f);
        jointDef.SetSteeringDampingRatio(0.7f);
        jointDef.SetMaxSteeringTorque(20.0f);
        jointDef.SetLowerSteeringLimit(degreesToRadians(-45.0f));
        jointDef.SetUpperSteeringLimit(degreesToRadians(45.0f));
        dispose(world().CreateWheelJoint(jointDef));

        dispose(jointDef, axisB, axisA, cylinder, shapeDef, bodyDef, wheelRotation);
    }

    private float degreesToRadians(float degrees) {
        return (float)(degrees * Math.PI / 180.0);
    }
}
