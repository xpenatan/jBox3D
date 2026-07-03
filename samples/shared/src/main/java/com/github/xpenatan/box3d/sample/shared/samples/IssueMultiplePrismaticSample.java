package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3DistanceJointDef;
import com.github.xpenatan.box3d.B3Filter;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3Vec3;

import java.util.ArrayList;
import java.util.List;

final class IssueMultiplePrismaticSample extends AbstractBox3DSample {
    IssueMultiplePrismaticSample() {
        addGroundBox(20.0f);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        for(int i = 0; i < 8; i++) {
            B3Body body = addDynamicBox(-7.0f + i * 2.0f, 5.0f, 0.0f, 0.4f, 0.8f, 0.3f);
            B3DistanceJointDef jointDef = new B3DistanceJointDef();
            jointDef.SetBodyIdA(ground.GetId());
            jointDef.SetBodyIdB(body.GetId());
            JointSampleUtil.setLocalPositionA(jointDef, -7.0f + i * 2.0f, 7.0f, 0.0f);
            JointSampleUtil.setLocalPositionB(jointDef, 0.0f, 0.8f, 0.0f);
            jointDef.SetLength(2.0f);
            jointDef.SetHertz(4.0f);
            jointDef.SetDampingRatio(0.7f);
            dispose(world().CreateDistanceJoint(jointDef), jointDef);
        }
    }
}
