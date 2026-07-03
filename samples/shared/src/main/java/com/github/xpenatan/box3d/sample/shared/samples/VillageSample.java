package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Quat;
import java.util.Random;

final class VillageSample extends AbstractBox3DSample {
    VillageSample() {
        addGroundBox(40.0f);
        for(int x = -3; x <= 3; x++) {
            for(int z = -2; z <= 2; z++) {
                createHouse(x * 5.0f, z * 5.0f, (x + z) * 0.17f);
            }
        }
        for(int i = 0; i < 20; i++) {
            addDynamicSphere(-12.0f + i * 1.2f, 12.0f + i * 0.25f, -12.0f + (i % 5) * 3.0f, 0.35f);
        }
    }

    private void createHouse(float x, float z, float angle) {
        B3Quat rotation = rotationY(angle);
        B3Body house = createBody(B3.StaticBody(), x, 0.0f, z, rotation);
        addBoxShape(house, 1.2f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        B3Quat roofLeft = rotationZ(0.25f * (float)Math.PI);
        addBoxShape(house, 0.9f, 0.2f, 1.05f, -0.45f, 2.05f, 0.0f, roofLeft, 0.0f, 0.6f, 0.0f, 0.0f);
        B3Quat roofRight = rotationZ(-0.25f * (float)Math.PI);
        addBoxShape(house, 0.9f, 0.2f, 1.05f, 0.45f, 2.05f, 0.0f, roofRight, 0.0f, 0.6f, 0.0f, 0.0f);
        dispose(roofRight, roofLeft, rotation);
    }
}
