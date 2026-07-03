package com.github.xpenatan.box3d.sample.shared.samples;

final class BoxStackSample extends AbstractBox3DSample {
    BoxStackSample() {
        addGroundBox(40.0f);
        float halfSize = 0.5f;
        for(int i = 0; i < 40; i++) {
            addDynamicBox(0.0f, 1.5f * halfSize + 2.5f * halfSize * i, 0.0f, halfSize, halfSize, halfSize, null,
                    1.0f, 0.6f, 0.0f, 0.1f);
        }
    }
}
