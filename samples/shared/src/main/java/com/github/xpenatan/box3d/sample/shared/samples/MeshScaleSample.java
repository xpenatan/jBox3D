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

final class MeshScaleSample extends AbstractBox3DSample {
    MeshScaleSample() {
        for(int i = 0; i < 18; i++) {
            float scale = 0.5f + i * 0.06f;
            addStaticBox(-12.0f + i * 1.5f, scale - 0.25f, 0.0f, 0.7f, scale, 4.0f, null);
        }
        DiagnosticUtil.addFallingMix(this, 36, 8.0f);
    }
}
