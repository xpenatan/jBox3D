package com.github.xpenatan.box3d.sample.shared.samples;

final class CylinderSample extends AbstractBox3DSample {
    CylinderSample() {
        addGroundBox(10.0f);
        addDynamicCylinder(0.0f, 2.0f, 0.0f, 1.0f, 0.25f, 12);
    }
}
