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

final class BenchmarkHeightFieldSample extends AbstractBox3DSample {
    BenchmarkHeightFieldSample() {
        for(int i = 0; i < 46; i++) {
            float x = -23.0f + i;
            float height = 0.6f + (float)Math.sin(i * 0.45f) * 0.5f + (float)Math.cos(i * 0.17f) * 0.35f;
            addStaticBox(x, height * 0.5f - 0.25f, 0.0f, 0.55f, height * 0.5f, 8.0f, null);
        }
        for(int i = 0; i < 80; i++) {
            addDynamicBox(-18.0f + (i % 16) * 2.4f, 8.0f + (i / 16) * 1.2f, -3.0f + (i % 5) * 1.5f,
                    0.35f, 0.35f, 0.35f);
        }
    }
}
