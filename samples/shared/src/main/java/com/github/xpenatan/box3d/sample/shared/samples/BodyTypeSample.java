package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3RevoluteJointDef;

final class BodyTypeSample extends AbstractBox3DSample {
    BodyTypeSample() {
        B3Body ground = addGroundBox(20.0f);
        B3Body attachment = addDynamicBox(-2.0f, 3.0f, 0.0f, 0.5f, 2.0f, 0.5f);
        B3Body secondAttachment = addDynamicBox(3.0f, 3.0f, 0.0f, 0.5f, 2.0f, 0.5f);

        B3Body platform = createBody(B3.DynamicBody(), -4.0f, 5.0f, 0.0f, null);
        B3Quat platformRotation = rotationZ(0.5f * (float)Math.PI);
        addBoxShape(platform, 0.5f, 4.0f, 0.5f, 4.0f, 0.0f, 0.0f, platformRotation,
                2.0f, 0.6f, 0.0f, 0.0f);
        dispose(platformRotation);

        createPlatformJoint(attachment, platform, 0.0f, 2.0f, 2.0f, 0.0f);
        createPlatformJoint(secondAttachment, platform, 0.0f, 2.0f, 7.0f, 0.0f);

        B3PrismaticJointDef prismaticDef = new B3PrismaticJointDef();
        prismaticDef.SetBodyIdA(ground.GetId());
        prismaticDef.SetBodyIdB(platform.GetId());
        JointSampleUtil.setLocalPositionA(prismaticDef, 0.0f, 6.0f, 0.0f);
        JointSampleUtil.setLocalPositionB(prismaticDef, 4.0f, 0.0f, 0.0f);
        prismaticDef.SetMaxMotorForce(1000.0f);
        prismaticDef.SetMotorSpeed(0.0f);
        prismaticDef.SetEnableMotor(true);
        prismaticDef.SetLowerTranslation(-10.0f);
        prismaticDef.SetUpperTranslation(10.0f);
        prismaticDef.SetEnableLimit(true);
        dispose(world().CreatePrismaticJoint(prismaticDef), prismaticDef);

        addDynamicBox(-3.0f, 8.0f, 0.0f, 0.75f, 0.75f, 0.75f, null, 2.0f, 0.6f, 0.0f, 0.0f);
        addDynamicBox(2.0f, 8.0f, 0.0f, 0.75f, 0.75f, 0.75f, null, 2.0f, 0.6f, 0.0f, 0.0f);

        B3Body touchingBody = createBody(B3.DynamicBody(), 8.0f, 0.2f, 0.0f, null);
        addCapsuleShape(touchingBody, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
                0.25f, 2.0f, 0.6f, 0.0f, 0.0f);

        B3Body floatingBody = createBody(B3.DynamicBody(), -8.0f, 12.0f, 0.0f, null);
        floatingBody.SetGravityScale(0.0f);
        addSphereShape(floatingBody, 0.0f, 0.5f, 0.0f, 0.25f, 2.0f, 0.6f, 0.0f, 0.0f);
    }

    private void createPlatformJoint(B3Body bodyA, B3Body platform, float localAX, float localAY,
            float localBX, float localBY) {
        B3RevoluteJointDef jointDef = new B3RevoluteJointDef();
        jointDef.SetBodyIdA(bodyA.GetId());
        jointDef.SetBodyIdB(platform.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, localAX, localAY, 0.0f);
        JointSampleUtil.setLocalPositionB(jointDef, localBX, localBY, 0.0f);
        jointDef.SetMaxMotorTorque(50.0f);
        jointDef.SetEnableMotor(true);
        dispose(world().CreateRevoluteJoint(jointDef), jointDef);
    }
}
