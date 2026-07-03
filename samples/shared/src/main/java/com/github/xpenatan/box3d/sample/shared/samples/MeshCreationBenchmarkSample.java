package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WheelJointDef;

final class MeshCreationBenchmarkSample extends AbstractBox3DSample {
    MeshCreationBenchmarkSample() {
        addGroundBox(30.0f);
        for(int i = 0; i < 120; i++) {
            B3Hull hull = B3Hull.CreateRock(0.3f + (i % 5) * 0.04f);
            addHull(hull, B3.DynamicBody(), -12.0f + (i % 12) * 2.0f, 2.0f + (i / 12) * 0.7f,
                    -4.0f + (i % 4) * 2.0f, null, 1.0f, 0.6f, 0.0f, 0.0f);
            dispose(hull);
        }
    }
}
