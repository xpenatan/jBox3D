package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Vec3;

final class SpinningBookSample extends AbstractBox3DSample {
    SpinningBookSample() {
        addGroundBox(10.0f);

        addBook(-2.0f, 2.0f, 0.0f, new B3Vec3(5.0f, 0.01f, 0.01f));
        addBook(0.0f, 2.0f, 0.0f, new B3Vec3(0.01f, 5.0f, 0.01f));
        addBook(2.0f, 2.0f, 0.0f, new B3Vec3(0.01f, 0.01f, -5.0f));
    }

    private void addBook(float x, float y, float z, B3Vec3 angularVelocity) {
        addDynamicBox(x, y, z, 0.35f, 0.08f, 0.5f, null, 1.0f, 0.6f, 0.0f, 0.0f, null, angularVelocity)
                .SetGravityScale(0.0f);
        dispose(angularVelocity);
    }
}
