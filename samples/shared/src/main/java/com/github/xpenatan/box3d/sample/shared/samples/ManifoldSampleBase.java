package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3LocalManifold;
import com.github.xpenatan.box3d.B3LocalManifoldPoint;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Direct Java counterpart of the C sample's Manifold and TriangleManifold bases. */
abstract class ManifoldSampleBase extends AbstractBox3DSample {
    protected final B3Transform transformA;
    protected final B3Transform transformB;

    ManifoldSampleBase() {
        B3Vec3 positionA = new B3Vec3(3.5f, 0.5f, 0.0f);
        B3Quat rotationA = rotationY(0.5f * (float)Math.PI);
        transformA = new B3Transform(positionA, rotationA);
        B3Vec3 positionB = new B3Vec3(0.0f, 1.5f, 3.5f);
        B3Quat rotationB = new B3Quat();
        transformB = new B3Transform(positionB, rotationB);
        dispose(rotationB, positionB, rotationA, positionA);
    }

    protected void setTransform(B3Transform transform, float x, float y, float z, B3Quat rotation) {
        B3Vec3 position = new B3Vec3(x, y, z);
        transform.SetP(position);
        transform.SetQ(rotation);
        dispose(position);
    }

    protected void clearFrame() {
        world().ClearDebugOverlay();
    }

    protected void drawRegularManifold(B3LocalManifold manifold) {
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);
        drawContacts(manifold, transformA);
    }

    protected void drawTriangleManifold(B3LocalManifold manifold, B3Vec3[] triangle) {
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);
        drawContacts(manifold, transformB);

        B3Vec3 p1 = transformA.TransformPoint(triangle[0]);
        B3Vec3 p2 = transformA.TransformPoint(triangle[1]);
        B3Vec3 p3 = transformA.TransformPoint(triangle[2]);
        world().AddDebugTriangle(p1, p2, p3, 0x00FFFF);

        float ax = p2.GetX() - p1.GetX();
        float ay = p2.GetY() - p1.GetY();
        float az = p2.GetZ() - p1.GetZ();
        float bx = p3.GetX() - p1.GetX();
        float by = p3.GetY() - p1.GetY();
        float bz = p3.GetZ() - p1.GetZ();
        float nx = ay * bz - az * by;
        float ny = az * bx - ax * bz;
        float nz = ax * by - ay * bx;
        float length = (float)Math.sqrt(nx * nx + ny * ny + nz * nz);
        if(length > 0.0f) {
            nx /= length;
            ny /= length;
            nz /= length;
        }
        B3Vec3 center = new B3Vec3((p1.GetX() + p2.GetX() + p3.GetX()) / 3.0f,
                (p1.GetY() + p2.GetY() + p3.GetY()) / 3.0f,
                (p1.GetZ() + p2.GetZ() + p3.GetZ()) / 3.0f);
        B3Vec3 normalEnd = new B3Vec3(center.GetX() + 0.5f * nx, center.GetY() + 0.5f * ny,
                center.GetZ() + 0.5f * nz);
        world().AddDebugSegment(center, normalEnd, 0x9370DB);
        dispose(normalEnd, center, p3, p2, p1);
    }

    protected B3Vec3Array triangleInFrameB(B3Vec3[] triangle) {
        B3Transform transform = B3Transform.InvMul(transformB, transformA);
        B3Vec3Array localTriangle = new B3Vec3Array(3);
        for(int i = 0; i < 3; ++i) {
            B3Vec3 local = transform.TransformPoint(triangle[i]);
            localTriangle.SetValue(i, local);
            dispose(local);
        }
        dispose(transform);
        return localTriangle;
    }

    protected void drawSphere(B3Transform transform, B3Sphere sphere, int color, float alpha) {
        B3Vec3 localCenter = sphere.GetCenter();
        B3Vec3 center = transform.TransformPoint(localCenter);
        world().AddDebugSphere(center, sphere.GetRadius(), color, alpha);
        dispose(center, localCenter);
    }

    protected void drawCapsule(B3Transform transform, B3Capsule capsule, int color, float alpha) {
        B3Vec3 local1 = capsule.GetCenter1();
        B3Vec3 local2 = capsule.GetCenter2();
        B3Vec3 p1 = transform.TransformPoint(local1);
        B3Vec3 p2 = transform.TransformPoint(local2);
        world().AddDebugCapsule(p1, p2, capsule.GetRadius(), color, alpha);
        dispose(p2, p1, local2, local1);
    }

    protected void drawHull(B3Transform transform, B3Hull hull, int color) {
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        world().AddDebugHull(hull, transform, one, color);
        dispose(one);
    }

    protected void drawTransformAxes(B3Transform transform, float size) {
        B3Vec3 origin = transform.GetP();
        B3Quat rotation = transform.GetQ();
        B3Vec3 axisX = new B3Vec3(size, 0.0f, 0.0f);
        B3Vec3 axisY = new B3Vec3(0.0f, size, 0.0f);
        B3Vec3 axisZ = new B3Vec3(0.0f, 0.0f, size);
        B3Vec3 dx = rotation.RotateVector(axisX);
        B3Vec3 dy = rotation.RotateVector(axisY);
        B3Vec3 dz = rotation.RotateVector(axisZ);
        B3Vec3 px = new B3Vec3(origin.GetX() + dx.GetX(), origin.GetY() + dx.GetY(), origin.GetZ() + dx.GetZ());
        B3Vec3 py = new B3Vec3(origin.GetX() + dy.GetX(), origin.GetY() + dy.GetY(), origin.GetZ() + dy.GetZ());
        B3Vec3 pz = new B3Vec3(origin.GetX() + dz.GetX(), origin.GetY() + dz.GetY(), origin.GetZ() + dz.GetZ());
        world().AddDebugSegment(origin, px, 0xFF0000);
        world().AddDebugSegment(origin, py, 0x00FF00);
        world().AddDebugSegment(origin, pz, 0x0000FF);
        dispose(pz, py, px, dz, dy, dx, axisZ, axisY, axisX, rotation, origin);
    }

    private void drawContacts(B3LocalManifold manifold, B3Transform frame) {
        if(manifold.GetPointCount() == 0) {
            return;
        }
        B3Quat rotation = frame.GetQ();
        B3Vec3 localNormal = manifold.GetNormal();
        B3Vec3 normal = rotation.RotateVector(localNormal);
        for(int i = 0; i < manifold.GetPointCount(); ++i) {
            B3LocalManifoldPoint manifoldPoint = manifold.GetPoint(i);
            B3Vec3 localPoint = manifoldPoint.GetPoint();
            B3Vec3 point = frame.TransformPoint(localPoint);
            B3Vec3 normalEnd = new B3Vec3(point.GetX() + 0.5f * normal.GetX(),
                    point.GetY() + 0.5f * normal.GetY(), point.GetZ() + 0.5f * normal.GetZ());
            world().AddDebugSegment(point, normalEnd, 0xFFFFFF);
            world().AddDebugPoint(point, 10.0f, manifoldPoint.GetSeparation() > 0.0f ? 0xFFFFFF : 0xFFFF00);
            dispose(normalEnd, point, localPoint, manifoldPoint);
        }
        dispose(normal, localNormal, rotation);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(transformB, transformA);
    }
}
