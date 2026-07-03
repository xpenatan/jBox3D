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

final class BenchmarkLargeWorldSample extends AbstractBox3DSample {
    BenchmarkLargeWorldSample() {
        float base = 50000.0f;
        addStaticBox(base, -0.25f, base, 50.0f, 0.25f, 50.0f, null);
        for(int i = 0; i < 120; i++) {
            addDynamicBox(base - 20.0f + (i % 12) * 3.5f, 1.0f + (i / 12) * 1.1f,
                    base - 10.0f + (i % 5) * 4.0f, 0.45f, 0.45f, 0.45f);
        }
    }
}
