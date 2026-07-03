package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Vec3;

final class SingleBoxSample extends AbstractBox3DSample {
    SingleBoxSample() {
        addGroundBox(20.0f);

        B3Vec3 angularVelocity = new B3Vec3(0.0f, 10.0f, 0.0f);
        addDynamicBox(0.0f, 0.5f, 0.0f, 0.5f, 0.5f, 0.5f, null, 1.0f, 0.6f, 0.0f, 0.0f, null,
                angularVelocity);
        dispose(angularVelocity);
    }
}
