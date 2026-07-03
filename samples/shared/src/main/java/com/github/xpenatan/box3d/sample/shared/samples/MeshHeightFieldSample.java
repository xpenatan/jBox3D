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

final class MeshHeightFieldSample extends AbstractBox3DSample {
    MeshHeightFieldSample() {
        for(int i = 0; i < 40; i++) {
            float x = -20.0f + i;
            float h = 0.4f + (float)Math.sin(i * 0.32f) * 0.8f;
            addStaticBox(x, h * 0.5f, 0.0f, 0.55f, Math.max(0.1f, h), 6.0f, null);
        }
        DiagnosticUtil.addFallingMix(this, 40, 10.0f);
    }
}
