package com.github.xpenatan.box3d.sample.shared.samples;

final class CapsuleStackSample extends AbstractBox3DSample {
    CapsuleStackSample() {
        addGroundBox(40.0f);
        float radius = 0.5f;
        for(int i = 0; i < 20; i++) {
            addDynamicCapsule(0.0f, 1.5f * radius + 2.0f * radius * i, 0.0f, 1.0f, radius);
        }
    }
}
