package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Quat;

final class HighResistanceSample extends AbstractBox3DSample {
    HighResistanceSample() {
        addGroundBox(50.0f);

        B3Quat rotation = rotationZ(radians(30.0f));
        for(int i = 0; i < 10; i++) {
            addDynamicCapsule(-22.0f + 5.0f * i, 1.5f, 0.0f, 1.0f, 0.5f, rotation, 1.0f, 0.6f, 0.0f,
                    0.2f * i);
        }
        dispose(rotation);
    }

    private static float radians(float degrees) {
        return degrees * (float)Math.PI / 180.0f;
    }
}
