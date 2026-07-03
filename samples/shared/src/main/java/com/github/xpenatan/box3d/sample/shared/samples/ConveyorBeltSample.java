package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

final class ConveyorBeltSample extends AbstractBox3DSample {
    ConveyorBeltSample() {
        addGroundBox(20.0f);

        B3Quat platformRotation = rotationY(0.2f);
        B3Vec3 tangentVelocity = new B3Vec3(2.0f, 0.0f, 0.0f);
        addConveyorBox(-5.0f, 5.0f, 0.0f, 10.0f, 0.25f, 2.0f, platformRotation, tangentVelocity);
        dispose(tangentVelocity, platformRotation);

        for(int i = 0; i < 5; i++) {
            addDynamicBox(-10.0f + 2.0f * i, 7.0f, 0.0f, 0.5f, 0.5f, 0.5f);
        }
    }
}
