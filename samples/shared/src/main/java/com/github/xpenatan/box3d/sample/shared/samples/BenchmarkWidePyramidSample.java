package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact port of {@code CreateWidePyramid} in {@code shared/benchmarks.c}. */
final class BenchmarkWidePyramidSample extends AbstractBox3DSample {
    BenchmarkWidePyramidSample() {
        addGroundBox(100.0f);

        float boxSize = 2.0f;
        float boxSeparation = 0.5f;
        float halfBoxSize = 0.5f * boxSize;
        int pyramidHeight = 15;
        float h = halfBoxSize - 0.025f;
        B3Hull box = B3Hull.CreateBox(h, h, h);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 position = new B3Vec3();

        for(int i = 0; i < pyramidHeight; ++i) {
            for(int j = i / 2; j < pyramidHeight - (i + 1) / 2; ++j) {
                for(int k = i / 2; k < pyramidHeight - (i + 1) / 2; ++k) {
                    float x = -pyramidHeight + boxSize * j + ((i & 1) != 0 ? halfBoxSize : 0.0f);
                    float y = 1.0f + (boxSize + boxSeparation) * i;
                    float z = -pyramidHeight + boxSize * k + ((i & 1) != 0 ? halfBoxSize : 0.0f);
                    position.Set(x, y, z);
                    bodyDef.SetPosition(position);
                    B3Body body = world().CreateBody(bodyDef);
                    dispose(body.CreateHullShape(shapeDef, box), body);
                }
            }
        }
        dispose(position, shapeDef, bodyDef, box);
    }
}
