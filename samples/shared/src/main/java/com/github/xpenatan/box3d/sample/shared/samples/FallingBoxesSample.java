package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact port of {@code FallingBoxes} in {@code sample_benchmark.cpp}. */
final class FallingBoxesSample extends AbstractBox3DSample {
    FallingBoxesSample() {
        addGroundBox(100.0f);
        int n = 50;
        float a = 0.5f;
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Hull box = B3Hull.CreateBox(a, a, a);
        B3Vec3 position = new B3Vec3();
        for(int i = 0; i < n; ++i) {
            for(int j = 0; j < 8; ++j) {
                for(int k = 0; k < 8; ++k) {
                    position.Set(-16.0f * a + 4.0f * a * j, 4.0f * a * i + 5.0f * a,
                            -16.0f * a + 4.0f * a * k);
                    bodyDef.SetPosition(position);
                    B3Body body = world().CreateBody(bodyDef);
                    dispose(body.CreateHullShape(shapeDef, box), body);
                }
            }
        }
        dispose(position, box, shapeDef, bodyDef);
    }
}
