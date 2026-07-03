package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Quat;
import java.util.Random;

final class TileFloorSample extends AbstractBox3DSample {
    TileFloorSample() {
        B3Body floor = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        int gridCount = 20;
        float a = 2.0f;
        float start = -0.5f * gridCount * a;
        for(int x = 0; x < gridCount; x++) {
            for(int z = 0; z < gridCount; z++) {
                addBoxShape(floor, 0.48f * a, 0.1f, 0.48f * a, start + x * a, -0.1f, start + z * a, null,
                        0.0f, 0.7f, 0.0f, 0.0f);
            }
        }

        for(int i = 0; i < 12; i++) {
            addDynamicBox(-8.0f + i * 1.5f, 3.0f + i * 0.4f, 0.0f, 0.45f, 0.45f, 0.45f);
        }
    }
}
