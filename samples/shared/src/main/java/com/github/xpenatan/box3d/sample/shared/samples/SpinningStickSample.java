package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;

final class SpinningStickSample extends AbstractBox3DSample {
    SpinningStickSample() {
        addGroundBox(10.0f);

        B3Body wallBody = createBody(B3.StaticBody(), 0.0f, 0.5f, 0.0f, null);
        addBoxShape(wallBody, 0.125f, 0.5f, 10.0f, 0.0f, 0.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);

        B3Vec3 linearVelocity = new B3Vec3(0.0f, -100.0f, 0.0f);
        B3Vec3 angularVelocity = new B3Vec3(40.0f, -35.0f, 28.0f);
        addDynamicBox(0.0f, 20.0f, 0.5f, 2.0f, 0.1f, 0.1f, null, 1.0f, 0.6f, 0.0f, 0.1f,
                linearVelocity, angularVelocity);
        dispose(angularVelocity, linearVelocity);
    }
}
