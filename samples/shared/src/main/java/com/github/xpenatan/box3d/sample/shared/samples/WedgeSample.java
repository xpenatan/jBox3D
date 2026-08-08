package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Hull;

final class WedgeSample extends AbstractBox3DSample {
    WedgeSample() {
        addGroundBox(20.0f);

        float[][] vertices = {
                {-1.0f, 1.0f, -0.1f},
                {1.0f, 1.0f, -0.1f},
                {-1.0f, 1.0f, 0.1f},
                {1.0f, 1.0f, 0.1f},
                {-0.5f, 0.5f, 0.0f},
                {0.5f, 0.5f, 0.0f}
        };
        B3Hull wedge = createHull(vertices);
        addHull(wedge, B3.DynamicBody(), 0.0f, 1.0f, 0.0f, null);
        dispose(wedge);
    }
}
