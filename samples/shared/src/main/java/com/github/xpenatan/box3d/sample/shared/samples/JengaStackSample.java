package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;

final class JengaStackSample extends AbstractBox3DSample {
    JengaStackSample() {
        addGroundBox(60.0f);

        B3Hull block = B3Hull.CreateBox(2.5f, 0.25f, 0.25f);
        B3Quat straight = rotationY(0.0f);
        B3Quat crossed = rotationY(0.5f * (float)Math.PI);

        for(int i = 0; i < 40; i++) {
            B3Quat rotation = (i & 1) == 1 ? straight : crossed;
            float x = (i & 1) == 0 ? 1.75f : 0.0f;
            float z = (i & 1) == 0 ? 0.0f : 1.75f;
            float y = 0.5f * i + 0.25f;
            addHull(block, B3.DynamicBody(), x, y, z, rotation, 1.0f, 0.6f, 0.0f, 0.01f);
            addHull(block, B3.DynamicBody(), -x, y, -z, rotation, 1.0f, 0.6f, 0.0f, 0.01f);
        }

        dispose(crossed, straight, block);
    }
}
