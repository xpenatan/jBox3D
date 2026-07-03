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

final class IssueCapsuleMeshSample extends AbstractBox3DSample {
    IssueCapsuleMeshSample() {
        addGroundBox(22.0f);
        SamplePortUtil.addObstacleCourse(this);
        for(int i = 0; i < 18; i++) {
            addDynamicCapsule(-8.0f + (i % 6) * 3.0f, 8.0f + (i / 6) * 1.2f, 0.0f, 0.55f, 0.25f);
        }
    }
}
