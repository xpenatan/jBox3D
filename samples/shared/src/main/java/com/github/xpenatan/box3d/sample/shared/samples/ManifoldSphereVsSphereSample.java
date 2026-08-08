package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3LocalManifold;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default port of Manifold / Sphere vs Sphere. */
final class ManifoldSphereVsSphereSample extends ManifoldSampleBase {
    private final B3Sphere sphere;

    ManifoldSphereVsSphereSample() {
        B3Vec3 center = new B3Vec3(0.5f, 0.0f, -0.25f);
        sphere = new B3Sphere(center, 2.0f);
        dispose(center);
        drawFrame();
    }

    @Override
    public void step(float deltaSeconds) {
        drawFrame();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(sphere);
    }

    private void drawFrame() {
        clearFrame();
        B3Transform transformBtoA = B3Transform.InvMul(transformA, transformB);
        B3LocalManifold manifold = B3Collision.CollideSpheres(64, sphere, sphere, transformBtoA);
        drawSphere(transformA, sphere, 0x008000, 1.0f);
        drawSphere(transformB, sphere, 0x00FFFF, 1.0f);
        drawRegularManifold(manifold);
        dispose(manifold, transformBtoA);
    }
}
