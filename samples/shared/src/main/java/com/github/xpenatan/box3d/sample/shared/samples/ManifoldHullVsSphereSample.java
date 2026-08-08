package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3LocalManifold;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default port of Manifold / Hull vs Sphere. */
final class ManifoldHullVsSphereSample extends ManifoldSampleBase {
    private final B3Hull hull;
    private final B3Sphere sphere;

    ManifoldHullVsSphereSample() {
        hull = B3Hull.CreateBox(2.0f, 0.5f, 0.5f);
        B3Vec3 center = new B3Vec3();
        sphere = new B3Sphere(center, 1.0f);
        B3Quat identity = new B3Quat();
        setTransform(transformA, 0.0f, 0.0f, 0.0f, identity);
        setTransform(transformB, 1.5f, 0.0f, 0.0f, identity);
        dispose(identity, center);
        drawFrame();
    }

    @Override
    public void step(float deltaSeconds) {
        drawFrame();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(sphere, hull);
    }

    private void drawFrame() {
        clearFrame();
        B3Transform transformBtoA = B3Transform.InvMul(transformA, transformB);
        B3LocalManifold manifold = B3Collision.CollideHullAndSphere(64, hull, sphere, transformBtoA);
        drawHull(transformA, hull, 0x00FFFF);
        drawSphere(transformB, sphere, 0x008000, 1.0f);
        drawRegularManifold(manifold);
        dispose(manifold, transformBtoA);
    }
}
