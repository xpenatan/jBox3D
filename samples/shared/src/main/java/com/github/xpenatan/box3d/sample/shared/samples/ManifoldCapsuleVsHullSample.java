package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3LocalManifold;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default port of Manifold / Capsule vs Hull. */
final class ManifoldCapsuleVsHullSample extends ManifoldSampleBase {
    private final B3Hull hull;
    private final B3Capsule capsule;

    ManifoldCapsuleVsHullSample() {
        hull = B3Hull.CreateBox(1.0f, 0.5f, 0.5f);
        B3Vec3 center1 = new B3Vec3(-1.0f, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(1.0f, 0.0f, 0.0f);
        capsule = new B3Capsule(center1, center2, 0.15f);
        B3Quat identity = new B3Quat();
        setTransform(transformA, 0.0f, 0.0f, 0.0f, identity);
        B3Quat rotationB = new B3Quat(-0.00256555085f, -0.0201825816f, 0.126076236f, 0.991811991f);
        setTransform(transformB, 1.58523774f, 0.729615569f, 0.451690674f, rotationB);
        dispose(rotationB, identity, center2, center1);
        drawFrame();
    }

    @Override
    public void step(float deltaSeconds) {
        drawFrame();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(capsule, hull);
    }

    private void drawFrame() {
        clearFrame();
        B3Transform transformBtoA = B3Transform.InvMul(transformA, transformB);
        B3LocalManifold manifold = B3Collision.CollideHullAndCapsule(64, hull, capsule, transformBtoA);
        drawHull(transformA, hull, 0x00FFFF);
        drawCapsule(transformB, capsule, 0x008000, 1.0f);
        drawRegularManifold(manifold);
        dispose(manifold, transformBtoA);
    }
}
