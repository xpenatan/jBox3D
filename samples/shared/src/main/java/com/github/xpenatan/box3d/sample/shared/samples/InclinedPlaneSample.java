package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Quat;

final class InclinedPlaneSample extends AbstractBox3DSample {
    InclinedPlaneSample() {
        addGroundBox(50.0f);

        B3Quat rampRotation = rotationX(40.0f * (float)Math.PI / 180.0f);
        addStaticBox(0.0f, 7.5f, -5.0f, 16.0f, 0.5f, 10.0f, rampRotation);
        dispose(rampRotation);

        for(int i = 0; i < 5; i++) {
            float friction = (i + 1) * (i + 1) * 0.04f;
            addDynamicBox(-10.0f + 5.0f * i, 15.75f, -10.6f, 1.0f, 1.0f, 1.0f, null, 1.0f, friction, 0.0f,
                    0.0f);
        }
    }
}
