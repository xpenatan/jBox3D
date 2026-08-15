package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

final class DominoesSample extends AbstractBox3DSample {
    private static final int RING_COUNT = 30;
    private static final float ANGLE_STEP_DEGREES = 2.0f;

    DominoesSample() {
        addGroundBox(80.0f);

        B3Hull domino = B3Hull.CreateBox(0.2f, 0.8f, 0.05f);
        for(int ring = 0; ring < RING_COUNT; ring++) {
            createRing(domino, 7.0f + 1.1f * ring);
        }
        dispose(domino);
    }

    private void createRing(B3Hull domino, float radius) {
        for(float degrees = 0.0f; degrees <= 360.0f; degrees += ANGLE_STEP_DEGREES) {
            float alpha = degrees * (float)Math.PI / 180.0f;
            long cosSin = SampleMath.computeCosSin(alpha);
            float cosine = SampleMath.cosine(cosSin);
            float sine = SampleMath.sine(cosSin);
            float x = radius * cosine - degrees / 630.0f * cosine;
            float z = radius * sine - degrees / 630.0f * sine;
            B3Quat rotation = rotationY(-alpha);
            B3Body body = addHull(domino, B3.DynamicBody(), x, 0.8f, z, rotation);
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
