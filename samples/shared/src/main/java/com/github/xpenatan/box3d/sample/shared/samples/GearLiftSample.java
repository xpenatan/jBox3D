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

final class GearLiftSample extends AbstractBox3DSample {
    GearLiftSample() {
        addGroundBox(22.0f);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3Body platform = addDynamicBox(0.0f, 2.0f, 0.0f, 2.0f, 0.2f, 1.5f);
        B3PrismaticJointDef jointDef = new B3PrismaticJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(platform.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, 0.0f, 2.0f, 0.0f);
        JointSampleUtil.setLocalPositionB(jointDef, 0.0f, 0.0f, 0.0f);
        jointDef.SetEnableLimit(true);
        jointDef.SetLowerTranslation(0.0f);
        jointDef.SetUpperTranslation(6.0f);
        jointDef.SetEnableMotor(true);
        jointDef.SetMotorSpeed(2.0f);
        jointDef.SetMaxMotorForce(2000.0f);
        dispose(world().CreatePrismaticJoint(jointDef), jointDef);
        for(int i = 0; i < 8; i++) {
            addDynamicBox(-1.4f + (i % 4) * 0.9f, 4.0f + (i / 4) * 0.7f, 0.0f, 0.35f, 0.35f, 0.35f);
        }
    }
}
