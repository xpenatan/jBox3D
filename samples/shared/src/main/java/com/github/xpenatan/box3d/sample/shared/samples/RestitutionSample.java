package com.github.xpenatan.box3d.sample.shared.samples;

final class RestitutionSample extends AbstractBox3DSample {
    RestitutionSample() {
        addGroundBox(50.0f);
        int count = 40;
        for(int i = 0; i < count; i++) {
            float restitution = i / (float)(count - 1);
            float x = -1.0f * (count - 1) + 2.0f * i;
            addDynamicSphere(x, 40.0f, 0.0f, 0.5f, 1.0f, 0.2f, restitution, 0.0f);
        }
    }
}
