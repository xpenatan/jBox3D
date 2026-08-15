package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact port of {@code CandyCups} in {@code sample_benchmark.cpp}. */
final class CandyCupsSample extends AbstractBox3DSample {
    CandyCupsSample() {
        addGroundBox(60.0f);
        B3Hull convex = createConvex(0.6f, 0.0f, 0.95f, 1.0f);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 position = new B3Vec3();
        for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
                for(int k = 0; k < 16; ++k) {
                    position.Set(-10.0f + 2.5f * j, i, -10.0f + 2.5f * k);
                    bodyDef.SetPosition(position);
                    B3Body body = world().CreateBody(bodyDef);
                    dispose(body.CreateHullShape(shapeDef, convex), body);
                }
            }
        }
        dispose(position, shapeDef, bodyDef, convex);
    }

    private static B3Hull createConvex(float radius1, float height1, float radius2, float height2) {
        int sideCount = 8;
        B3Vec3Array vertices = new B3Vec3Array(2 * sideCount);
        float alpha = 0.0f;
        float deltaAlpha = 2.0f * (float)Math.PI / sideCount;
        for(int sideIndex = 0; sideIndex < sideCount; ++sideIndex) {
            long cosSin = SampleMath.computeCosSin(alpha);
            float cosine = SampleMath.cosine(cosSin);
            float sine = SampleMath.sine(cosSin);
            B3Vec3 lower = new B3Vec3(radius1 * cosine, height1, radius1 * sine);
            B3Vec3 upper = new B3Vec3(radius2 * cosine, height2, radius2 * sine);
            vertices.SetValue(2 * sideIndex, lower);
            vertices.SetValue(2 * sideIndex + 1, upper);
            dispose(upper, lower);
            alpha += deltaAlpha;
        }
        B3Hull hull = B3Hull.CreateFromPoints(vertices, 2 * sideCount);
        dispose(vertices);
        return hull;
    }
}
