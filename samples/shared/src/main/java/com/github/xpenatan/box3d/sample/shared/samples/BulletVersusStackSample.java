package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;

final class BulletVersusStackSample extends AbstractBox3DSample {
    BulletVersusStackSample() {
        addGroundBox(50.0f);
        B3Body wallBody = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        addBoxShape(wallBody, 0.1f, 5.0f, 10.0f, -1.0f, 5.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        for(int row = 0; row < 10; row++) {
            addDynamicBox(0.0f, 0.5f + 1.1f * row, 0.0f, 0.5f, 0.5f, 0.5f);
        }
    }
}
