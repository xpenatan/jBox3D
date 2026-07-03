package com.github.xpenatan.box3d.sample.shared.samples;

final class OverlapRecoverySample extends AbstractBox3DSample {
    OverlapRecoverySample() {
        addGroundBox(20.0f);
        float extent = 0.5f;
        int baseCount = 6;
        float overlap = 0.25f;
        float fraction = 1.0f - overlap;
        float y = extent;
        for(int row = 0; row < baseCount; row++) {
            float x = fraction * extent * (row - baseCount);
            for(int column = row; column < baseCount; column++) {
                addDynamicBox(x, y, 0.0f, extent, extent, extent);
                x += 2.0f * fraction * extent;
            }
            y += 2.0f * fraction * extent;
        }
    }
}
