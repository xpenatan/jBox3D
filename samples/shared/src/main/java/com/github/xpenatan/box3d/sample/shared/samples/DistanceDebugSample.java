package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3DistanceOutput;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeProxy;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default port of the pinned Collision / Distance Debug regression case. */
final class DistanceDebugSample extends AbstractBox3DSample {
    private final B3Hull boxA;
    private final B3Hull boxB;
    private final B3ShapeProxy proxyA;
    private final B3ShapeProxy proxyB;
    private final B3Transform transformA;
    private final B3Transform transformB;

    DistanceDebugSample() {
        boxA = B3Hull.CreateBox(40.0f, 1.0f, 40.0f);
        B3Vec3 localPosition = new B3Vec3(0.0f, 10.0f, 0.0f);
        B3Quat localRotation = new B3Quat();
        B3Transform localTransform = new B3Transform(localPosition, localRotation);
        boxB = B3Hull.CreateTransformedBox(0.5f, 10.0f, 0.5f, localTransform);
        proxyA = new B3ShapeProxy(boxA, 0.0f);
        proxyB = new B3ShapeProxy(boxB, 0.0f);
        transformA = new B3Transform();
        B3Vec3 positionB = new B3Vec3(-1.64657831e-6f, 1.00989532471f, 0.0f);
        B3Quat rotationB = new B3Quat(0.0f, 0.0f, 0.00494779600f, 0.999987781f);
        transformB = new B3Transform(positionB, rotationB);
        dispose(rotationB, positionB, localTransform, localRotation, localPosition);
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
        dispose(transformB, transformA, proxyB, proxyA, boxB, boxA);
    }

    private void drawDistance() {
        world().ClearDebugOverlay();
        addDebugGroundGrid(10);
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);

        B3Transform transformBtoA = B3Transform.InvMul(transformA, transformB);
        B3DistanceOutput output = B3Collision.ShapeDistance(proxyA, proxyB, transformBtoA, false);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        world().AddDebugHull(boxA, transformA, one, 0x008000);
        world().AddDebugHull(boxB, transformB, one, 0x00FFFF);

        B3Vec3 localA = output.GetPointA();
        B3Vec3 localB = output.GetPointB();
        B3Vec3 pointA = transformA.TransformPoint(localA);
        B3Vec3 pointB = transformA.TransformPoint(localB);
        B3Quat rotationA = transformA.GetQ();
        B3Vec3 localNormal = output.GetNormal();
        B3Vec3 normal = rotationA.RotateVector(localNormal);
        B3Vec3 normalEnd = new B3Vec3(pointA.GetX() + normal.GetX(), pointA.GetY() + normal.GetY(),
                pointA.GetZ() + normal.GetZ());
        world().AddDebugPoint(pointA, 5.0f, 0xFFFFFF);
        world().AddDebugPoint(pointB, 5.0f, 0xFFFFFF);
        world().AddDebugSegment(pointA, normalEnd, 0xFFFFFF);
        dispose(normalEnd, normal, localNormal, rotationA, pointB, pointA, localB, localA, one, output,
                transformBtoA);
    }
}
