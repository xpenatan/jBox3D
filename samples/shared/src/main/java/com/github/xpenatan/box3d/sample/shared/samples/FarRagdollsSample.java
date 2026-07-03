package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Quat;

final class FarRagdollsSample extends AbstractBox3DSample {
    private static final float OFFSET = 10000.0f;

    FarRagdollsSample() {
        B3Body ground = createBody(B3.StaticBody(), OFFSET, -1.0f, 0.0f, null);
        addBoxShape(ground, 20.0f, 1.0f, 20.0f, 0.0f, 0.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        for(int i = 0; i < 14; i++) {
            float x = OFFSET + 0.15f * (i - 7.0f);
            float y = 2.0f + 0.25f * i;
            float z = 0.15f * (7.0f - i);
            createSimpleRagdoll(x, y, z);
        }
    }

    private void createSimpleRagdoll(float x, float y, float z) {
        B3Quat torsoRotation = rotationZ(0.5f * (float)Math.PI);
        addDynamicCapsule(x, y + 0.8f, z, 0.35f, 0.16f, torsoRotation, 1.0f, 0.6f, 0.0f, 0.02f);
        addDynamicSphere(x, y + 1.35f, z, 0.22f);
        B3Quat leftRotation = rotationZ(0.3f * (float)Math.PI);
        addDynamicCapsule(x - 0.45f, y + 0.8f, z, 0.28f, 0.08f, leftRotation, 1.0f, 0.6f, 0.0f,
                0.02f);
        B3Quat rightRotation = rotationZ(-0.3f * (float)Math.PI);
        addDynamicCapsule(x + 0.45f, y + 0.8f, z, 0.28f, 0.08f, rightRotation, 1.0f, 0.6f, 0.0f,
                0.02f);
        dispose(rightRotation, leftRotation, torsoRotation);
    }

    static float offset() {
        return OFFSET;
    }
}
