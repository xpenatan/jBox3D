package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WheelJointDef;

final class DrivingSample extends AbstractBox3DSample {
    DrivingSample() {
        addGroundBox(30.0f);
        B3Body chassis = addDynamicBox(0.0f, 3.0f, 0.0f, 1.8f, 0.35f, 1.0f);
        addWheel(chassis, -1.2f, 2.35f, -0.85f);
        addWheel(chassis, 1.2f, 2.35f, -0.85f);
        addWheel(chassis, -1.2f, 2.35f, 0.85f);
        addWheel(chassis, 1.2f, 2.35f, 0.85f);
        B3Vec3 impulse = new B3Vec3(80.0f, 0.0f, 0.0f);
        chassis.ApplyLinearImpulseToCenter(impulse, true);
        dispose(impulse);
    }

    private void addWheel(B3Body chassis, float x, float y, float z) {
        B3Quat rotation = rotationX(0.5f * (float)Math.PI);
        B3Body wheel = createBody(B3.DynamicBody(), x, y, z, rotation);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.9f, 0.0f, 0.02f);
        B3Hull cylinder = B3Hull.CreateCylinder(0.3f, 0.35f, 0.0f, 12);
        dispose(wheel.CreateHullShape(shapeDef, cylinder));
        B3WheelJointDef jointDef = new B3WheelJointDef();
        jointDef.SetBodyIdA(chassis.GetId());
        jointDef.SetBodyIdB(wheel.GetId());
        B3Quat localFrameA = rotationZ(0.5f * (float)Math.PI);
        B3Quat localFrameB = rotationX(-0.5f * (float)Math.PI);
        JointSampleUtil.setLocalFrameA(jointDef, x, -0.35f, z, localFrameA);
        JointSampleUtil.setLocalFrameB(jointDef, 0.0f, 0.0f, 0.0f, localFrameB);
        jointDef.SetEnableSuspensionSpring(true);
        jointDef.SetSuspensionHertz(4.0f);
        jointDef.SetSuspensionDampingRatio(0.7f);
        jointDef.SetEnableSpinMotor(true);
        jointDef.SetSpinSpeed(-18.0f);
        jointDef.SetMaxSpinTorque(80.0f);
        dispose(world().CreateWheelJoint(jointDef), jointDef, localFrameB, localFrameA, cylinder, shapeDef, rotation);
    }
}
