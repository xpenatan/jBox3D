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

final class GeometryBoxHullSample extends AbstractBox3DSample {
    GeometryBoxHullSample() {
        addGroundBox(16.0f);
        for(int i = 0; i < 9; i++) {
            B3Quat rotation = rotationY(i * 0.25f);
            addDynamicBox(-6.0f + i * 1.5f, 5.0f, 0.0f, 0.45f + i * 0.03f, 0.35f, 0.55f, rotation,
                    1.0f, 0.6f, 0.0f, 0.0f);
            dispose(rotation);
        }
    }
}
