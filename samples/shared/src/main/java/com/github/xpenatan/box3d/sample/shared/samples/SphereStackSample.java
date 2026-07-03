package com.github.xpenatan.box3d.sample.shared.samples;

final class SphereStackSample extends AbstractBox3DSample {
    SphereStackSample() {
        addGroundBox(15.0f);
        float radius = 0.5f;
        for(int i = 0; i < 30; i++) {
            addDynamicSphere(0.0f, 1.5f * radius + 3.0f * radius * i, 0.0f, radius, 1.0f, 0.6f, 0.0f, 0.1f);
        }
    }
}
