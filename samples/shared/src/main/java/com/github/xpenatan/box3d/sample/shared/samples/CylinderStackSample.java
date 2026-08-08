package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

final class CylinderStackSample extends AbstractBox3DSample {
    CylinderStackSample() {
        addGroundBox(10.0f);

        B3Hull cylinder = B3Hull.CreateCylinder(1.0f, 0.5f, 0.0f, 15);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 origin = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Quat identityRotation = new B3Quat();
        B3Transform identity = new B3Transform(origin, identityRotation);
        float[][] scales = {
                {1.0f, 1.0f, 1.0f},
                {-0.75f, 1.0f, 1.0f},
                {1.2f, 1.0f, -0.9f},
                {0.9f, 0.9f, 0.9f}
        };

        for(int i = 0; i < 10; i++) {
            B3Body body = createBody(B3.DynamicBody(), 0.0f, 1.1f * i, 0.0f, null);
            float[] values = scales[i & 3];
            B3Vec3 scale = new B3Vec3(values[0], values[1], values[2]);
            dispose(body.CreateTransformedHullShape(shapeDef, cylinder, identity, scale), scale);
        }

        dispose(identity, identityRotation, origin, shapeDef, cylinder);
    }
}
