package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Hull;

final class CylinderSample extends AbstractBox3DSample {
    CylinderSample() {
        addGroundBox(10.0f);
        B3Hull cylinder = B3Hull.CreateCylinder(1.0f, 0.25f, 0.0f, 12);
        B3Body body = createBody(B3.DynamicBody(), 0.0f, 2.0f, 0.0f, null);
        addHullShape(body, cylinder, 1000.0f, 0.6f, 0.0f, 0.05f);
        dispose(cylinder);
    }
}
