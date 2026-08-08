package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;

/** Exact default scene from World/Far Pyramid. */
final class FarPyramidSample extends AbstractBox3DSample {
    private static final float OFFSET = 10_000_000.0f;

    FarPyramidSample() {
        B3Body ground = createBody(B3.StaticBody(), OFFSET, -1.0f, 0.0f, null);
        addBoxShape(ground, 400.0f, 1.0f, 400.0f, 0.0f, 0.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        dispose(ground);
        int baseCount = 40;
        float h = 0.5f;
        for(int row = 0; row < baseCount; row++) {
            float y = (2.0f * row + 1.0f) * h;
            for(int column = row; column < baseCount; column++) {
                float x = (row + 1.0f) * h + 2.0f * (column - row) * h - h * baseCount;
                dispose(addDynamicBox(OFFSET + x, y, 0.0f, h, h, h, null, 100.0f, 0.6f, 0.0f, 0.0f));
            }
        }
    }

    static float offset() {
        return OFFSET;
    }
}
