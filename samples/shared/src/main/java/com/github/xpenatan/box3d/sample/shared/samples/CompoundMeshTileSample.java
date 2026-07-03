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

final class CompoundMeshTileSample extends AbstractBox3DSample {
    CompoundMeshTileSample() {
        for(int x = -8; x <= 8; x++) {
            for(int z = -8; z <= 8; z++) {
                float h = 0.15f + ((x * x + z * z) % 5) * 0.08f;
                addStaticBox(x, h - 0.25f, z, 0.48f, h, 0.48f, null);
            }
        }
        for(int i = 0; i < 48; i++) {
            addDynamicBox(-6.0f + (i % 8) * 1.8f, 6.0f + (i / 8), -4.0f + (i % 4) * 2.0f,
                    0.35f, 0.35f, 0.35f);
        }
    }
}
