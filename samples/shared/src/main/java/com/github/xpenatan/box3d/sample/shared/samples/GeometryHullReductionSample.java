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

final class GeometryHullReductionSample extends AbstractBox3DSample {
    GeometryHullReductionSample() {
        addGroundBox(18.0f);
        for(int i = 0; i < 16; i++) {
            B3Hull hull = B3Hull.CreateCone(1.0f, 0.25f + i * 0.03f, 0.65f, 5 + (i % 6));
            addHull(hull, B3.DynamicBody(), -7.0f + i, 5.0f, 0.0f, null, 1.0f, 0.6f, 0.0f, 0.0f);
            dispose(hull);
        }
    }
}
