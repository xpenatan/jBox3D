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

final class RagdollMeshSample extends AbstractBox3DSample {
    RagdollMeshSample() {
        addGroundBox(26.0f);
        for(int i = 0; i < 10; i++) {
            B3Quat rotation = rotationZ((i - 5) * 0.08f);
            addStaticBox(-10.0f + i * 2.2f, 1.0f + (i % 3) * 0.55f, 0.0f, 1.2f, 0.15f, 4.0f, rotation);
            dispose(rotation);
        }
        for(int i = 0; i < 3; i++) {
            SamplePortUtil.createSimpleRagdoll(this, -5.0f + i * 5.0f, 10.0f + i, 0.0f);
        }
    }
}
