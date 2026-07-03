package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Quat;
import java.util.Random;

final class CompoundSpheresSample extends AbstractBox3DSample {
    CompoundSpheresSample() {
        Random random = new Random(0xB03D);
        B3Body body = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        for(int i = 0; i < 20; i++) {
            float radius = randomRange(random, 0.1f, 0.5f);
            addSphereShape(body, randomRange(random, -10.0f, 10.0f), randomRange(random, -10.0f, 10.0f),
                    randomRange(random, -10.0f, 10.0f), radius, 0.0f, 0.6f, 0.0f, 0.0f);
        }
    }

    private float randomRange(Random random, float min, float max) {
        return min + random.nextFloat() * (max - min);
    }
}
