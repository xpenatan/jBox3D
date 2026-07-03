package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;

final class ArchSample extends AbstractBox3DSample {
    ArchSample() {
        addGroundBox(40.0f);

        B3Hull voussoir = B3Hull.CreateBox(0.42f, 0.78f, 0.5f);
        int count = 17;
        float radius = 5.0f;
        for(int i = 0; i < count; i++) {
            float t = (float)i / (count - 1);
            float angle = (float)Math.PI - t * (float)Math.PI;
            float x = radius * (float)Math.cos(angle);
            float y = 0.8f + radius * (float)Math.sin(angle);
            B3Quat rotation = rotationZ(angle - 0.5f * (float)Math.PI);
            addHull(voussoir, B3.DynamicBody(), x, y, 0.0f, rotation, 200.0f, 0.6f, 0.0f, 0.0f);
            dispose(rotation);
        }

        for(int i = 0; i < 4; i++) {
            addDynamicBox(0.0f, 6.25f + i, 0.0f, 2.0f, 0.5f, 0.5f, null, 200.0f, 0.6f, 0.0f, 0.0f);
        }

        dispose(voussoir);
    }
}
