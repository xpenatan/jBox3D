package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3LocalManifold;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default port of Manifold / Capsule vs Sphere. */
final class ManifoldCapsuleVsSphereSample extends ManifoldSampleBase {
    private final B3Capsule capsule;
    private final B3Sphere sphere;

    ManifoldCapsuleVsSphereSample() {
        B3Vec3 center1 = new B3Vec3(-2.0f, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(2.0f, 0.0f, 0.0f);
        capsule = new B3Capsule(center1, center2, 1.0f);
        B3Vec3 center = new B3Vec3();
        sphere = new B3Sphere(center, 2.0f);
        B3Quat identity = new B3Quat();
        setTransform(transformA, 0.0f, 0.0f, 0.0f, identity);
        setTransform(transformB, -4.0f, 0.0f, 0.0f, identity);
        dispose(identity, center, center2, center1);
        drawFrame();
    }

    @Override
    public void step(float deltaSeconds) {
        drawFrame();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(sphere, capsule);
    }

    private void drawFrame() {
        clearFrame();
        B3Transform transformBtoA = B3Transform.InvMul(transformA, transformB);
        B3LocalManifold manifold = B3Collision.CollideCapsuleAndSphere(64, capsule, sphere, transformBtoA);
        drawCapsule(transformA, capsule, 0x00FFFF, 1.0f);
        drawSphere(transformB, sphere, 0x008000, 1.0f);
        drawRegularManifold(manifold);
        dispose(manifold, transformBtoA);
    }
}
