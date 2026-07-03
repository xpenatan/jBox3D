package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Quat;

final class FarPyramidSample extends AbstractBox3DSample {
    private static final float OFFSET = 10000.0f;

    FarPyramidSample() {
        B3Body ground = createBody(B3.StaticBody(), OFFSET, -1.0f, 0.0f, null);
        addBoxShape(ground, 120.0f, 1.0f, 120.0f, 0.0f, 0.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        int baseCount = 24;
        float h = 0.5f;
        for(int row = 0; row < baseCount; row++) {
            float y = (2.0f * row + 1.0f) * h;
            for(int column = row; column < baseCount; column++) {
                float x = (row + 1.0f) * h + 2.0f * (column - row) * h - h * baseCount;
                addDynamicBox(OFFSET + x, y, 0.0f, h, h, h, null, 100.0f, 0.6f, 0.0f, 0.0f);
            }
        }
    }

    static float offset() {
        return OFFSET;
    }
}
