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

final class GeometryCapsuleMassSample extends AbstractBox3DSample {
    GeometryCapsuleMassSample() {
        addGroundBox(16.0f);
        for(int i = 0; i < 12; i++) {
            addDynamicCapsule(-6.0f + i * 1.1f, 5.0f + (i % 3), 0.0f, 0.35f + 0.04f * i, 0.18f);
        }
    }
}
