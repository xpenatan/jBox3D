package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

final class SlideTwistSample extends AbstractBox3DSample {
    SlideTwistSample() {
        addGroundBox(50.0f);

        float angle = radians(20.0f);
        B3Quat orientation = rotationX(angle);
        addStaticBox(0.0f, 4.0f, 0.0f, 10.0f, 0.5f, 10.0f, orientation);

        B3Vec3 angularVelocity = new B3Vec3(0.0f, 25.0f * (float)Math.cos(angle), 25.0f * (float)Math.sin(angle));
        addDynamicBox(0.0f, 5.0f, 0.0f, 1.0f, 0.5f, 1.0f, orientation, 1.0f, 0.3f, 0.0f, 0.0f, null,
                angularVelocity);
        dispose(angularVelocity, orientation);
    }

    private static float radians(float degrees) {
        return degrees * (float)Math.PI / 180.0f;
    }
}
