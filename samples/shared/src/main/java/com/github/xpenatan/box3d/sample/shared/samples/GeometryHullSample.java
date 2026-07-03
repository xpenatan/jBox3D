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

final class GeometryHullSample extends AbstractBox3DSample {
    GeometryHullSample() {
        addGroundBox(18.0f);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        for(int i = 0; i < 14; i++) {
            B3Hull hull = i % 3 == 0 ? B3Hull.CreateRock(0.55f)
                    : (i % 3 == 1 ? B3Hull.CreateCylinder(0.8f, 0.35f, 0.0f, 8) : B3Hull.CreateCone(0.9f, 0.25f, 0.55f, 8));
            B3Body body = createBody(B3.DynamicBody(), -7.0f + i * 1.1f, 5.0f + (i % 4) * 0.5f, 0.0f, null);
            dispose(body.CreateHullShape(shapeDef, hull), hull);
        }
        dispose(shapeDef);
    }
}
