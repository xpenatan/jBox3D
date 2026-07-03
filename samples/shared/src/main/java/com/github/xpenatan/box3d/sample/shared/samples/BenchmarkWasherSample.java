package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class BenchmarkWasherSample extends AbstractBox3DSample {
    BenchmarkWasherSample() {
        addGroundBox(40.0f);
        for(int i = 0; i < 18; i++) {
            createWasher(-10.0f + (i % 6) * 4.0f, 4.0f + (i / 6) * 3.0f, -6.0f + (i % 3) * 6.0f);
        }
    }

    private void createWasher(float x, float y, float z) {
        B3Body body = createBody(B3.DynamicBody(), x, y, z, rotationY((x + z) * 0.07f));
        for(int i = 0; i < 12; i++) {
            float angle = i * 2.0f * (float)Math.PI / 12.0f;
            float px = (float)Math.cos(angle) * 0.8f;
            float pz = (float)Math.sin(angle) * 0.8f;
            B3Quat rotation = rotationY(-angle);
            addBoxShape(body, 0.28f, 0.18f, 0.12f, px, 0.0f, pz, rotation, 1.0f, 0.6f, 0.0f, 0.02f);
            dispose(rotation);
        }
        body.ApplyMassFromShapes();
    }
}
