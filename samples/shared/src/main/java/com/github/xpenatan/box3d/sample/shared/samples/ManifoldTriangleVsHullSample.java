package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3LocalManifold;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact default port of Manifold / Triangle vs Hull. */
final class ManifoldTriangleVsHullSample extends ManifoldSampleBase {
    private final B3Vec3[] triangle = new B3Vec3[3];
    private final B3Hull hull;

    ManifoldTriangleVsHullSample() {
        triangle[0] = new B3Vec3(0.299769998f, -1.01549578f, -0.744717002f);
        triangle[1] = new B3Vec3(0.299769998f, -1.01549578f, 1.28728306f);
        triangle[2] = new B3Vec3(0.299769998f, -0.913895786f, 0.271283031f);
        hull = B3Hull.CreateBox(0.304800004f, 0.914399981f, 0.304800004f);
        B3Quat identity = new B3Quat();
        setTransform(transformA, 0.0f, 0.0f, 0.0f, identity);
        setTransform(transformB, 0.0f, 0.0f, 0.0f, identity);
        dispose(identity);
        drawFrame();
    }

    @Override
    public void step(float deltaSeconds) {
        drawFrame();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(hull, triangle[2], triangle[1], triangle[0]);
    }

    private void drawFrame() {
        clearFrame();
        B3Vec3Array localTriangle = triangleInFrameB(triangle);
        B3LocalManifold manifold = B3Collision.CollideTriangleAndHull(8, localTriangle, 0, hull, true);
        drawHull(transformB, hull, 0x008000);
        drawTransformAxes(transformB, 0.1f);
        drawTriangleManifold(manifold, triangle);
        dispose(manifold, localTriangle);
    }
}
