package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Quat;

final class FarStackSample extends AbstractBox3DSample {
    private static final float OFFSET = 10000.0f;

    FarStackSample() {
        B3Body ground = createBody(B3.StaticBody(), OFFSET, -1.0f, 0.0f, null);
        addBoxShape(ground, 12.0f, 1.0f, 12.0f, 0.0f, 0.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        for(int i = 0; i < 6; i++) {
            float skew = (i & 1) == 0 ? -0.02f : 0.02f;
            addDynamicBox(OFFSET + skew, 0.5f + i, 0.0f, 0.5f, 0.5f, 0.5f);
        }
    }

    static float offset() {
        return OFFSET;
    }
}
