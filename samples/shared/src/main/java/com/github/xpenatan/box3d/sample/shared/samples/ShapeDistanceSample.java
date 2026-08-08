package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3DistanceOutput;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeProxy;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact default port of Collision / Shape Distance from the pinned Box3D commit. */
final class ShapeDistanceSample extends AbstractBox3DSample {
    private final B3Vec3[] triangle = new B3Vec3[3];
    private final B3Hull box;
    private final B3ShapeProxy proxyA;
    private final B3ShapeProxy proxyB;
    private final B3Transform transformA;
    private final B3Transform transformB;

    ShapeDistanceSample() {
        triangle[0] = new B3Vec3(-1.5f, 0.0f, 0.0f);
        triangle[1] = new B3Vec3(1.5f, 0.0f, 0.0f);
        triangle[2] = new B3Vec3(0.0f, 0.0f, 2.0f);
        B3Vec3Array points = new B3Vec3Array(3);
        points.SetValue(0, triangle[0]);
        points.SetValue(1, triangle[1]);
        points.SetValue(2, triangle[2]);
        proxyA = new B3ShapeProxy(points, 3, 0.0f);
        box = B3Hull.CreateBox(0.125f, 0.25f, 0.5f);
        proxyB = new B3ShapeProxy(box, 0.0f);
        transformA = new B3Transform();
        B3Vec3 positionB = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Quat identity = new B3Quat();
        transformB = new B3Transform(positionB, identity);
        dispose(identity, positionB, points);
        drawDistance();
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        drawDistance();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(transformB, transformA, proxyB, proxyA, box, triangle[2], triangle[1], triangle[0]);
    }

    private void drawDistance() {
        world().ClearDebugOverlay();
        addDebugAxes(0.0f, 0.0f, 0.0f, 0.5f);
        B3Transform transformBtoA = B3Transform.InvMul(transformA, transformB);
        B3DistanceOutput output = B3Collision.ShapeDistance(proxyA, proxyB, transformBtoA, false);

        addTriangleEdges(triangle[0], triangle[1], triangle[2], 0x00FFFF);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        world().AddDebugHull(box, transformB, one, 0xFFE4C4);

        B3Vec3 localA = output.GetPointA();
        B3Vec3 localB = output.GetPointB();
        B3Vec3 pointA = transformA.TransformPoint(localA);
        B3Vec3 pointB = transformA.TransformPoint(localB);
        world().AddDebugSegment(pointA, pointB, 0x696969);
        world().AddDebugPoint(pointA, 10.0f, 0x90EE90);
        world().AddDebugPoint(pointB, 10.0f, 0xADD8E6);
        B3Quat rotationA = transformA.GetQ();
        B3Vec3 localNormal = output.GetNormal();
        B3Vec3 normal = rotationA.RotateVector(localNormal);
        B3Vec3 normalEnd = new B3Vec3(pointA.GetX() + 0.5f * normal.GetX(),
                pointA.GetY() + 0.5f * normal.GetY(), pointA.GetZ() + 0.5f * normal.GetZ());
        world().AddDebugSegment(pointA, normalEnd, 0xFFFF00);
        dispose(normalEnd, normal, localNormal, rotationA, pointB, pointA, localB, localA, one, output,
                transformBtoA);
    }

    private void addTriangleEdges(B3Vec3 p1, B3Vec3 p2, B3Vec3 p3, int color) {
        world().AddDebugSegment(p1, p2, color);
        world().AddDebugSegment(p2, p3, color);
        world().AddDebugSegment(p3, p1, color);
    }
}
