package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Vec3;

final class DoubleDominoSample extends AbstractBox3DSample {
    DoubleDominoSample() {
        addGroundBox(20.0f);

        B3Hull domino = B3Hull.CreateBox(0.125f, 0.5f, 0.25f);
        int count = 15;
        float x = -0.5f * count;
        for(int i = 0; i < count; i++) {
            B3Body body = addHull(domino, B3.DynamicBody(), x, 0.5f, 0.0f, null, 4.0f, 0.6f, 0.0f, 0.0f);
            if(i == 0) {
                B3Vec3 impulse = new B3Vec3(0.2f, 0.0f, 0.0f);
                B3Vec3 point = new B3Vec3(x, 1.0f, 0.0f);
                body.ApplyLinearImpulse(impulse, point, true);
                dispose(point, impulse);
            }
            x += 1.01f;
        }
        dispose(domino);
    }
}
