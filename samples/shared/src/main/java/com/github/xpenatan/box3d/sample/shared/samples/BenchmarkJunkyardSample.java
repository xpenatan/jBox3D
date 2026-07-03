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

final class BenchmarkJunkyardSample extends AbstractBox3DSample {
    BenchmarkJunkyardSample() {
        addGroundBox(45.0f);
        for(int i = 0; i < 160; i++) {
            float x = -18.0f + (i % 16) * 2.4f;
            float y = 2.0f + (i / 16) * 1.0f;
            float z = -8.0f + (i % 8) * 2.2f;
            int type = i % 4;
            if(type == 0) {
                addDynamicSphere(x, y, z, 0.45f);
            }
            else if(type == 1) {
                addDynamicCapsule(x, y, z, 0.45f, 0.2f);
            }
            else if(type == 2) {
                addDynamicCylinder(x, y, z, 0.6f, 0.35f, 10);
            }
            else {
                addDynamicBox(x, y, z, 0.45f, 0.35f, 0.3f);
            }
        }
    }
}
