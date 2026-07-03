package com.github.xpenatan.box3d.sample.shared.samples;

final class CylinderStackSample extends AbstractBox3DSample {
    CylinderStackSample() {
        addGroundBox(10.0f);
        for(int i = 0; i < 10; i++) {
            addDynamicCylinder(0.0f, 1.1f * i, 0.0f, 1.0f, 0.5f, 15);
        }
    }
}
