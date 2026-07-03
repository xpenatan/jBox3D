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

final class MeshGridSample extends AbstractBox3DSample {
    MeshGridSample() {
        for(int x = -8; x <= 8; x++) {
            for(int z = -8; z <= 8; z++) {
                float h = 0.2f + ((x + z + 20) % 4) * 0.15f;
                addStaticBox(x, h - 0.25f, z, 0.48f, h, 0.48f, null);
            }
        }
        DiagnosticUtil.addFallingMix(this, 32, 8.0f);
    }
}
