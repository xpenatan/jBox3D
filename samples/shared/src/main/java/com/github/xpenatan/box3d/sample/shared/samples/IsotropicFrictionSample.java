package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

final class IsotropicFrictionSample extends AbstractBox3DSample {
    IsotropicFrictionSample() {
        addGroundBox(100.0f);

        for(int i = 0; i < 32; i++) {
            float alpha = (float)Math.PI / 16.0f * i;
            float cosine = (float)Math.cos(alpha);
            float sine = (float)Math.sin(alpha);
            B3Quat rotation = rotationY(-alpha);
            B3Vec3 velocity = new B3Vec3(25.0f * cosine, 0.0f, 25.0f * sine);
            addDynamicBox(15.0f * cosine, 1.0f, 15.0f * sine, 1.0f, 1.0f, 1.0f, rotation, 1.0f, 0.6f, 0.0f,
                    0.0f, velocity, null);
            dispose(velocity, rotation);
        }
    }
}
