package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3ShapeDef;

/** Exact initial physical scene from Issues/Multiple Prismatic. */
final class IssueMultiplePrismaticSample extends AbstractBox3DSample {
    IssueMultiplePrismaticSample() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3Hull box = B3Hull.CreateBox(0.5f, 0.5f, 0.5f);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3PrismaticJointDef jointDef = new B3PrismaticJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, 0.0f, 0.0f, 0.0f);
        JointSampleUtil.setLocalPositionB(jointDef, 0.0f, -0.6f, 0.0f);
        jointDef.SetDrawScale(2.0f);
        jointDef.SetConstraintHertz(240.0f);
        jointDef.SetLowerTranslation(-6.0f);
        jointDef.SetUpperTranslation(6.0f);
        jointDef.SetEnableLimit(true);

        for(int i = 0; i < 6; i++) {
            B3Body body = createBody(B3.DynamicBody(), 0.0f, 0.6f + 1.2f * i, 0.0f, null);
            dispose(body.CreateHullShape(shapeDef, box));
            jointDef.SetBodyIdB(body.GetId());
            dispose(world().CreatePrismaticJoint(jointDef));
            jointDef.SetBodyIdA(body.GetId());
            JointSampleUtil.setLocalPositionA(jointDef, 0.0f, 0.6f, 0.0f);
            dispose(body);
        }
        dispose(jointDef, shapeDef, box, ground);
    }
}
