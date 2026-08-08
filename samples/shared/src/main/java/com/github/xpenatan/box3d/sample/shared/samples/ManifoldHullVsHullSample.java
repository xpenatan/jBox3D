package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3LocalManifold;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default port of Manifold / Hull vs Hull. */
final class ManifoldHullVsHullSample extends ManifoldSampleBase {
    private final B3Hull hullA;
    private final B3Hull hullB;

    ManifoldHullVsHullSample() {
        B3Vec3 localPosition = new B3Vec3(1.0f, 0.5f, 0.0f);
        B3Quat identity = new B3Quat();
        B3Transform localTransform = new B3Transform(localPosition, identity);
        hullA = B3Hull.CreateTransformedBox(0.5f, 1.0f, 1.0f, localTransform);
        hullB = B3Hull.CreateBox(0.5f, 0.5f, 0.5f);
        setTransform(transformA, 0.0f, 0.0f, 0.0f, identity);
        setTransform(transformB, 0.0f, 0.0f, 0.0f, identity);
        dispose(localTransform, identity, localPosition);
        drawFrame();
    }

    @Override
    public void step(float deltaSeconds) {
        drawFrame();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(hullB, hullA);
    }

    private void drawFrame() {
        clearFrame();
        B3Transform transformBtoA = B3Transform.InvMul(transformA, transformB);
        B3LocalManifold manifold = B3Collision.CollideHulls(64, hullA, hullB, transformBtoA);
        drawHull(transformA, hullA, 0x008000);
        drawHull(transformB, hullB, 0x00FFFF);
        drawRegularManifold(manifold);
        dispose(manifold, transformBtoA);
    }
}
