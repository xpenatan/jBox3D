package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact Benchmark/Hull scene and per-step hull workload. */
final class BenchmarkHullSample extends AbstractBox3DSample {
    private final B3Vec3Array points;
    private final B3Hull hull;
    private final B3Transform identityTransform;
    private final B3Vec3 reflectionScale;

    BenchmarkHullSample() {
        SampleRandom random = new SampleRandom(42);
        points = new B3Vec3Array(64);
        for(int i = 0; i < 64; i++) {
            B3Vec3 point = new B3Vec3(random.nextFloat(-1.0f, 1.0f), random.nextFloat(-1.0f, 1.0f),
                    random.nextFloat(-1.0f, 1.0f));
            points.SetValue(i, point);
            dispose(point);
        }
        hull = B3Hull.CreateFromPoints(points, 64);
        reflectionScale = new B3Vec3(-1.0f, 1.0f, 1.0f);
        B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Quat identity = new B3Quat();
        identityTransform = new B3Transform(zero, identity);
        B3Hull transformed = B3Hull.CloneAndTransform(hull, identityTransform, reflectionScale);

        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        B3Vec3 left = new B3Vec3(-2.0f, 0.0f, 0.0f);
        B3Vec3 right = new B3Vec3(2.0f, 0.0f, 0.0f);
        B3Transform transform1 = new B3Transform(left, identity);
        B3Transform transform2 = new B3Transform(right, identity);
        world().AddDebugHull(hull, transform1, one, 0x008000);
        world().AddDebugHull(transformed, transform2, one, 0xFFFF00);
        dispose(transform2, transform1, right, left, one, transformed, identity, zero);
    }

    @Override
    public void step(float deltaSeconds) {
        for(int i = 0; i < 2000; i++) {
            B3Hull created = B3Hull.CreateFromPoints(points, 64);
            B3Hull cloned = B3Hull.CloneAndTransform(hull, identityTransform, reflectionScale);
            dispose(cloned, created);
        }
        super.step(deltaSeconds);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(reflectionScale, identityTransform, hull, points);
    }
}
