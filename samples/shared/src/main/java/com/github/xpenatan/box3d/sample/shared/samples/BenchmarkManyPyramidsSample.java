package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact port of {@code CreateManyPyramids} in {@code shared/benchmarks.c}. */
final class BenchmarkManyPyramidsSample extends AbstractBox3DSample {
    BenchmarkManyPyramidsSample() {
        int baseCount = 10;
        float extent = 0.5f;
        int rowCount = 14;
        int columnCount = 14;
        float groundExtent = extent * columnCount * (baseCount + 1.0f);
        addGroundBox(groundExtent);

        float baseWidth = 2.0f * extent * baseCount;
        float baseZ = -groundExtent + 2.0f * extent;
        float deltaZ = 2.0f * (groundExtent - 2.0f * extent) / (rowCount - 1.0f);
        B3Hull box = B3Hull.CreateBox(extent, extent, extent);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        bodyDef.SetEnableSleep(false);
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetDensity(100.0f);
        B3Vec3 position = new B3Vec3();

        for(int i = 0; i < rowCount; ++i) {
            for(int j = 0; j < columnCount; ++j) {
                float centerX = -groundExtent + j * (baseWidth + 2.0f * extent) + 2.0f * extent;
                createSmallPyramid(baseCount, extent, centerX, baseZ, bodyDef, shapeDef, box, position);
            }
            baseZ += deltaZ;
        }
        dispose(position, shapeDef, bodyDef, box);
    }

    private void createSmallPyramid(int baseCount, float extent, float centerX, float baseZ, B3BodyDef bodyDef,
            B3ShapeDef shapeDef, B3Hull box, B3Vec3 position) {
        for(int i = 0; i < baseCount; ++i) {
            float y = (2.0f * i + 1.0f) * extent;
            for(int j = i; j < baseCount; ++j) {
                float x = (i + 1.0f) * extent + 2.0f * (j - i) * extent + centerX - 0.5f;
                position.Set(x, y, baseZ);
                bodyDef.SetPosition(position);
                B3Body body = world().CreateBody(bodyDef);
                dispose(body.CreateHullShape(shapeDef, box), body);
            }
        }
    }
}
