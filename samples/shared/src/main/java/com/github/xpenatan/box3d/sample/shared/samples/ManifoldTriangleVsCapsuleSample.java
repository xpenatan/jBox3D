package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3LocalManifold;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact default port of Manifold / Triangle vs Capsule. */
final class ManifoldTriangleVsCapsuleSample extends ManifoldSampleBase {
    private final B3Vec3[] triangle = new B3Vec3[3];
    private final B3Capsule capsule;

    ManifoldTriangleVsCapsuleSample() {
        triangle[0] = new B3Vec3(-4.0f, 0.0f, -4.0f);
        triangle[1] = new B3Vec3(-4.0f, 0.0f, 0.0f);
        triangle[2] = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 center1 = new B3Vec3(-0.5f, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.5f, 0.0f, 0.0f);
        capsule = new B3Capsule(center1, center2, 0.05f);
        B3Quat identity = new B3Quat();
        setTransform(transformA, 0.0f, 0.0f, 0.0f, identity);
        setTransform(transformB, -1.0f, 0.0f, -1.0f, identity);
        dispose(identity, center2, center1);
        drawFrame();
    }

    @Override
    public void step(float deltaSeconds) {
        drawFrame();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(capsule, triangle[2], triangle[1], triangle[0]);
    }

    private void drawFrame() {
        clearFrame();
        B3Vec3Array localTriangle = triangleInFrameB(triangle);
        B3LocalManifold manifold = B3Collision.CollideTriangleAndCapsule(8, localTriangle, capsule);
        drawCapsule(transformB, capsule, 0x008000, 0.5f);
        drawTransformAxes(transformB, 0.1f);
        drawTriangleManifold(manifold, triangle);
        dispose(manifold, localTriangle);
    }
}
