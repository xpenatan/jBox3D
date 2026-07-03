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

final class DeterminismFallingRagdollsSample extends AbstractBox3DSample {
    DeterminismFallingRagdollsSample() {
        addGroundBox(38.0f);
        for(int i = 0; i < 8; i++) {
            SamplePortUtil.createSimpleRagdoll(this, -12.0f + i * 3.5f, 14.0f + (i % 3), -3.0f + (i & 1) * 6.0f);
        }
    }
}
