package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;

final class WedgeSample extends AbstractBox3DSample {
    WedgeSample() {
        addGroundBox(20.0f);

        B3Hull wedgeLike = B3Hull.CreateCone(1.0f, 0.9f, 0.25f, 4);
        B3Quat rotation = rotationZ(0.5f * (float)Math.PI);
        addHull(wedgeLike, B3.DynamicBody(), 0.0f, 1.0f, 0.0f, rotation, 1.0f, 0.6f, 0.0f, 0.0f);
        dispose(rotation, wedgeLike);
    }
}
