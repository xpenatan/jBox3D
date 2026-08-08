package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact port of {@code CreateLargePyramid} in {@code shared/benchmarks.c}. */
final class BenchmarkLargePyramidSample extends AbstractBox3DSample {
    BenchmarkLargePyramidSample() {
        world().EnableSleeping(false);
        addGroundBox(400.0f);

        int baseCount = 100;
        float h = 0.5f;
        float shift = h;
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetDensity(100.0f);
        B3Hull box = B3Hull.CreateBox(h, h, h);
        B3Vec3 position = new B3Vec3();

        for(int i = 0; i < baseCount; ++i) {
            float y = (2.0f * i + 1.0f) * shift;
            for(int j = i; j < baseCount; ++j) {
                float x = (i + 1.0f) * shift + 2.0f * (j - i) * shift - h * baseCount;
                position.Set(x, y, 0.0f);
                bodyDef.SetPosition(position);
                B3Body body = world().CreateBody(bodyDef);
                dispose(body.CreateHullShape(shapeDef, box), body);
            }
        }
        dispose(position, box, shapeDef, bodyDef);
    }
}
