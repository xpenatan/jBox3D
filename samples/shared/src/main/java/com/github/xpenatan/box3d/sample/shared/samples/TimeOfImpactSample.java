package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeProxy;
import com.github.xpenatan.box3d.B3Sweep;
import com.github.xpenatan.box3d.B3TOIOutput;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact default port of Collision / Time of Impact from the pinned Box3D commit. */
final class TimeOfImpactSample extends AbstractBox3DSample {
    private final B3Vec3[] triangle = new B3Vec3[3];
    private final B3Capsule capsule;
    private final B3ShapeProxy proxyA;
    private final B3ShapeProxy proxyB;
    private final B3Sweep sweepA;
    private final B3Sweep sweepB;

    TimeOfImpactSample() {
        triangle[0] = new B3Vec3(-4.0f, 0.0f, -4.0f);
        triangle[1] = new B3Vec3(-4.0f, 0.0f, -8.0f);
        triangle[2] = new B3Vec3(-8.0f, 0.0f, -8.0f);
        B3Vec3Array trianglePoints = new B3Vec3Array(3);
        trianglePoints.SetValue(0, triangle[0]);
        trianglePoints.SetValue(1, triangle[1]);
        trianglePoints.SetValue(2, triangle[2]);
        proxyA = new B3ShapeProxy(trianglePoints, 3, 0.0f);

        B3Vec3 center1 = new B3Vec3(0.0f, -0.2f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, 0.2f, 0.0f);
        capsule = new B3Capsule(center1, center2, 0.02f);
        B3Vec3Array capsulePoints = new B3Vec3Array(2);
        capsulePoints.SetValue(0, center1);
        capsulePoints.SetValue(1, center2);
        proxyB = new B3ShapeProxy(capsulePoints, 2, 0.02f);

        sweepA = new B3Sweep();
        B3Quat identity = new B3Quat();
        sweepA.SetQ1(identity);
        sweepA.SetQ2(identity);
        sweepB = new B3Sweep();
        B3Vec3 c1 = new B3Vec3(-4.06512070f, 0.101333618f, -7.87591267f);
        B3Vec3 c2 = new B3Vec3(-4.15895557f, 0.0356027633f, -7.69682646f);
        B3Quat q1 = new B3Quat(-0.860495985f, -0.272824734f, 0.0724888667f, 0.424097389f);
        B3Quat q2 = new B3Quat(-0.604184389f, -0.424355596f, 0.0457959622f, 0.672894001f);
        sweepB.SetC1(c1);
        sweepB.SetC2(c2);
        sweepB.SetQ1(q1);
        sweepB.SetQ2(q2);
        dispose(q2, q1, c2, c1, identity, capsulePoints, center2, center1, trianglePoints);
        drawCase();
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        drawCase();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(sweepB, sweepA, proxyB, proxyA, capsule, triangle[2], triangle[1], triangle[0]);
    }

    private void drawCase() {
        world().ClearDebugOverlay();
        addDebugAxes(0.0f, 0.0f, 0.0f, 0.5f);
        B3TOIOutput output = B3Collision.TimeOfImpact(proxyA, proxyB, sweepA, sweepB, 1.0f);
        addTriangleEdges(0x00FFFF);

        B3Transform transform1 = sweepB.GetTransform(0.0f);
        B3Transform transform2 = sweepB.GetTransform(1.0f);
        drawCapsule(transform1, 0x90EE90);
        drawCapsule(transform2, 0xF08080);
        if(output.GetFraction() < 1.0f) {
            B3Transform hitTransform = sweepB.GetTransform(output.GetFraction());
            drawCapsule(hitTransform, 0xE0FFFF);
            dispose(hitTransform);
        }

        int state = output.GetState();
        if(state == 1 || state == 3) {
            B3Vec3 point = output.GetPoint();
            B3Vec3 normal = output.GetNormal();
            B3Vec3 normalEnd = new B3Vec3(point.GetX() + 0.5f * normal.GetX(),
                    point.GetY() + 0.5f * normal.GetY(), point.GetZ() + 0.5f * normal.GetZ());
            world().AddDebugSegment(point, normalEnd, 0x696969);
            world().AddDebugPoint(point, 10.0f, 0x90EE90);
            dispose(normalEnd, normal, point);
        }
        dispose(transform2, transform1, output);
    }

    private void drawCapsule(B3Transform transform, int color) {
        B3Vec3 local1 = capsule.GetCenter1();
        B3Vec3 local2 = capsule.GetCenter2();
        B3Vec3 world1 = transform.TransformPoint(local1);
        B3Vec3 world2 = transform.TransformPoint(local2);
        world().AddDebugCapsule(world1, world2, capsule.GetRadius(), color, 1.0f);

        B3Vec3 localCenter = new B3Vec3(0.5f * (local1.GetX() + local2.GetX()),
                0.5f * (local1.GetY() + local2.GetY()), 0.5f * (local1.GetZ() + local2.GetZ()));
        B3Vec3 center = transform.TransformPoint(localCenter);
        B3Quat rotation = transform.GetQ();
        drawAxes(center, rotation, 0.025f);
        dispose(rotation, center, localCenter, world2, world1, local2, local1);
    }

    private void drawAxes(B3Vec3 origin, B3Quat rotation, float size) {
        B3Vec3 x = new B3Vec3(size, 0.0f, 0.0f);
        B3Vec3 y = new B3Vec3(0.0f, size, 0.0f);
        B3Vec3 z = new B3Vec3(0.0f, 0.0f, size);
        B3Vec3 rx = rotation.RotateVector(x);
        B3Vec3 ry = rotation.RotateVector(y);
        B3Vec3 rz = rotation.RotateVector(z);
        B3Vec3 px = new B3Vec3(origin.GetX() + rx.GetX(), origin.GetY() + rx.GetY(), origin.GetZ() + rx.GetZ());
        B3Vec3 py = new B3Vec3(origin.GetX() + ry.GetX(), origin.GetY() + ry.GetY(), origin.GetZ() + ry.GetZ());
        B3Vec3 pz = new B3Vec3(origin.GetX() + rz.GetX(), origin.GetY() + rz.GetY(), origin.GetZ() + rz.GetZ());
        world().AddDebugSegment(origin, px, 0xFF0000);
        world().AddDebugSegment(origin, py, 0x00FF00);
        world().AddDebugSegment(origin, pz, 0x0000FF);
        dispose(pz, py, px, rz, ry, rx, z, y, x);
    }

    private void addTriangleEdges(int color) {
        world().AddDebugSegment(triangle[0], triangle[1], color);
        world().AddDebugSegment(triangle[1], triangle[2], color);
        world().AddDebugSegment(triangle[2], triangle[0], color);
    }
}
