package com.github.xpenatan.box3d.sample.shared.samples;

final class OverflowColorPileSample extends AbstractBox3DSample {
    OverflowColorPileSample() {
        addGroundBox(15.0f);
        int count = 7;
        float radius = 0.35f;
        for(int y = 0; y < count; y++) {
            for(int x = 0; x < count; x++) {
                for(int z = 0; z < count; z++) {
                    float px = (x - count * 0.5f) * radius * 1.65f;
                    float py = radius + y * radius * 1.45f;
                    float pz = (z - count * 0.5f) * radius * 1.65f;
                    addDynamicSphere(px, py, pz, radius, 1.0f, 0.6f, 0.0f, 0.0f);
                }
            }
        }
    }
}
