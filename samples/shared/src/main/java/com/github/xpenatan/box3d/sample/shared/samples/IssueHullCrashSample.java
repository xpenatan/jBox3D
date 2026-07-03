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

final class IssueHullCrashSample extends AbstractBox3DSample {
    IssueHullCrashSample() {
        addGroundBox(18.0f);
        for(int i = 0; i < 20; i++) {
            B3Hull hull = B3Hull.CreateCone(0.9f, 0.35f, 0.75f, 6 + (i % 5));
            addHull(hull, B3.DynamicBody(), -7.0f + (i % 7) * 2.1f, 4.0f + (i / 7) * 1.3f, 0.0f,
                    null, 1.0f, 0.5f, 0.0f, 0.0f);
            dispose(hull);
        }
    }
}
