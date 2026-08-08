package com.github.xpenatan.box3d.sample.shared.samples;

final class SingleBoxSample extends AbstractBox3DSample {
    SingleBoxSample() {
        addGroundBox(20.0f);
        addDynamicBox(0.0f, 0.5f, 0.0f, 0.5f, 0.5f, 0.5f);
    }
}
