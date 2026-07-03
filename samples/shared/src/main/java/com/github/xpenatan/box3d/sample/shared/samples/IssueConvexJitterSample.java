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

final class IssueConvexJitterSample extends AbstractBox3DSample {
    IssueConvexJitterSample() {
        addGroundBox(16.0f);
        B3Hull rock = B3Hull.CreateRock(0.8f);
        for(int i = 0; i < 24; i++) {
            B3Quat rotation = rotationY(i * 0.35f);
            addHull(rock, B3.DynamicBody(), -6.0f + (i % 6) * 2.4f, 4.0f + (i / 6) * 1.2f, 0.0f,
                    rotation, 1.0f, 0.6f, 0.0f, 0.0f);
            dispose(rotation);
        }
        dispose(rock);
    }
}
