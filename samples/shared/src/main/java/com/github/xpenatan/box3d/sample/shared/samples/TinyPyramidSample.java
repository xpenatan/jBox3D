package com.github.xpenatan.box3d.sample.shared.samples;

final class TinyPyramidSample extends AbstractBox3DSample {
    TinyPyramidSample() {
        addGroundBox(20.0f);
        float extent = 0.025f;
        int baseCount = 30;
        for(int row = 0; row < baseCount; row++) {
            float y = (2.0f * row + 1.0f) * extent;
            for(int column = row; column < baseCount; column++) {
                float x = (row + 1.0f) * extent + 2.0f * (column - row) * extent - baseCount * extent;
                addDynamicBox(x, y, 0.0f, extent, extent, extent);
            }
        }
    }
}
