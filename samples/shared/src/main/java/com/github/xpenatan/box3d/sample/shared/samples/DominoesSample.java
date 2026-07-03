package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

final class DominoesSample extends AbstractBox3DSample {
    DominoesSample() {
        addGroundBox(80.0f);

        B3Hull domino = B3Hull.CreateBox(0.2f, 0.8f, 0.05f);
        for(int ring = 0; ring < 8; ring++) {
            createRing(domino, 7.0f + 1.1f * ring);
        }
        dispose(domino);
    }

    private void createRing(B3Hull domino, float radius) {
        for(float degrees = 0.0f; degrees <= 360.0f; degrees += 4.0f) {
            float alpha = degrees * (float)Math.PI / 180.0f;
            float cosine = (float)Math.cos(alpha);
            float sine = (float)Math.sin(alpha);
            float x = radius * cosine - degrees / 630.0f * cosine;
            float z = radius * sine - degrees / 630.0f * sine;
            B3Quat rotation = rotationY(-alpha);
            B3Body body = addHull(domino, B3.DynamicBody(), x, 0.8f, z, rotation, 1.0f, 0.6f, 0.0f, 0.0f);
            if(degrees == 0.0f) {
                B3Vec3 impulse = new B3Vec3(0.0f, 0.0f, 25.0f);
                B3Vec3 point = new B3Vec3(x, 1.6f, z);
                body.ApplyLinearImpulse(impulse, point, true);
                dispose(point, impulse);
            }
            dispose(rotation);
        }
    }
}
