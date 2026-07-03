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

final class GeometryHullTransformSample extends AbstractBox3DSample {
    GeometryHullTransformSample() {
        addGroundBox(18.0f);
        for(int i = 0; i < 12; i++) {
            B3Quat localRotation = rotationZ(i * 0.2f);
            addDynamicBox(-6.0f + i * 1.1f, 5.0f + (i % 3) * 0.8f, 0.0f, 0.45f, 0.25f, 0.65f,
                    localRotation, 1.0f, 0.6f, 0.0f, 0.0f);
            dispose(localRotation);
        }
    }
}
