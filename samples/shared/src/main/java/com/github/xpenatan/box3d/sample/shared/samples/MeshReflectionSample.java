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

final class MeshReflectionSample extends AbstractBox3DSample {
    MeshReflectionSample() {
        addGroundBox(20.0f);
        for(int i = 0; i < 10; i++) {
            B3Quat left = rotationZ(i * 0.08f);
            B3Quat right = rotationZ(-i * 0.08f);
            addStaticBox(-5.0f + i, 1.0f + i * 0.2f, -2.0f, 0.55f, 0.12f, 1.5f, left);
            addStaticBox(5.0f - i, 1.0f + i * 0.2f, 2.0f, 0.55f, 0.12f, 1.5f, right);
            dispose(right, left);
        }
        DiagnosticUtil.addFallingMix(this, 28, 8.0f);
    }
}
