package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Quat;
import java.util.Random;

final class CompoundHullsSample extends AbstractBox3DSample {
    CompoundHullsSample() {
        Random random = new Random(0xC0FFEE);
        B3Body body = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        for(int i = 0; i < 20; i++) {
            B3Quat rotation = rotationY(random.nextFloat() * 2.0f * (float)Math.PI);
            addBoxShape(body, randomRange(random, 0.1f, 0.5f), randomRange(random, 0.1f, 0.5f),
                    randomRange(random, 0.1f, 0.5f), randomRange(random, -10.0f, 10.0f),
                    randomRange(random, -10.0f, 10.0f), randomRange(random, -10.0f, 10.0f), rotation, 0.0f,
                    0.6f, 0.0f, 0.0f);
            dispose(rotation);
        }
    }

    private float randomRange(Random random, float min, float max) {
        return min + random.nextFloat() * (max - min);
    }
}
