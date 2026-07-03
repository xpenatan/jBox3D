package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Quat;

final class RollingResistanceSample extends AbstractBox3DSample {
    RollingResistanceSample() {
        addGroundBox(50.0f);

        B3Quat planeRotation = rotationX(radians(10.0f));
        addStaticBox(0.0f, 2.0f, -20.0f, 32.0f, 0.5f, 15.0f, planeRotation);
        dispose(planeRotation);

        for(int i = 0; i < 5; i++) {
            addDynamicSphere(-25.0f + 5.0f * i, 8.0f, -24.0f, 1.0f, 1.0f, 0.6f, 0.0f, 0.05f * i);
            addDynamicCapsule(2.0f + 5.0f * i, 8.0f, -24.0f, 1.0f, 0.5f, null, 1.0f, 0.6f, 0.0f,
                    0.05f * i);
        }
    }

    private static float radians(float degrees) {
        return degrees * (float)Math.PI / 180.0f;
    }
}
