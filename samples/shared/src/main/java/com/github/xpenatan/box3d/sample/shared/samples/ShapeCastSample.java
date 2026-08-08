package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3ShapeProxy;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact default port of Collision / Shape Cast from the pinned Box3D commit. */
final class ShapeCastSample extends AbstractBox3DSample {
    private static final float PI = (float)Math.PI;
    private final B3Mesh mesh;

    ShapeCastSample() {
        B3Hull box = B3Hull.CreateBox(0.6f, 0.6f, 0.6f);
        mesh = B3Mesh.CreateTorus(10, 12, 0.65f, 0.35f);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.StaticBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 position = new B3Vec3();

        for(int index = 0; index < 3; ++index) {
            float y = 3.0f + 2.0f * index;

            B3Quat sphereRotation = rotationX(0.5f * PI);
            position.Set(-6.0f, y, 0.0f);
            bodyDef.SetPosition(position);
            bodyDef.SetRotation(sphereRotation);
            B3Body sphereBody = world().CreateBody(bodyDef);
            B3Vec3 sphereCenter = new B3Vec3();
            B3Sphere sphere = new B3Sphere(sphereCenter, 0.9f);
            dispose(sphereBody.CreateSphereShape(shapeDef, sphere), sphereBody, sphere, sphereCenter, sphereRotation);

            B3Quat capsuleRotation = rotationZ(0.25f * PI);
            position.Set(-2.0f, y, 0.0f);
            bodyDef.SetPosition(position);
            bodyDef.SetRotation(capsuleRotation);
            B3Body capsuleBody = world().CreateBody(bodyDef);
            B3Vec3 center1 = new B3Vec3(-0.5f, 0.0f, 0.0f);
            B3Vec3 center2 = new B3Vec3(0.5f, 0.0f, 0.0f);
            B3Capsule capsule = new B3Capsule(center1, center2, 0.7f);
            dispose(capsuleBody.CreateCapsuleShape(shapeDef, capsule), capsuleBody, capsule, center2, center1,
                    capsuleRotation);

            B3Quat hullRotation = rotationZ(0.25f * PI);
            position.Set(2.0f, y, 0.0f);
            bodyDef.SetPosition(position);
            bodyDef.SetRotation(hullRotation);
            B3Body hullBody = world().CreateBody(bodyDef);
            dispose(hullBody.CreateHullShape(shapeDef, box), hullBody, hullRotation);

            B3Quat meshRotation = rotationX(0.5f * PI);
            position.Set(6.0f, y, 0.0f);
            bodyDef.SetPosition(position);
            bodyDef.SetRotation(meshRotation);
            B3Body meshBody = world().CreateBody(bodyDef);
            B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
            dispose(meshBody.CreateMeshShape(shapeDef, mesh, one), meshBody, one, meshRotation);
        }

        dispose(position, shapeDef, bodyDef, box);
        drawCasts();
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        drawCasts();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }

    private void drawCasts() {
        world().ClearDebugOverlay();
        B3QueryFilter filter = new B3QueryFilter();
        B3Vec3 origin = new B3Vec3();
        B3Vec3 translation = new B3Vec3(0.0f, 0.0f, 10.0f);

        for(int castIndex = 0; castIndex < 4; ++castIndex) {
            float x = -6.0f + 4.0f * castIndex;
            drawSphereCast(x, origin, translation, filter);
            drawCapsuleCast(x, origin, translation, filter);
            drawHullCast(x, origin, translation, filter);
        }

        addDebugGroundGrid(10);
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);
        dispose(translation, origin, filter);
    }

    private void drawSphereCast(float x, B3Vec3 origin, B3Vec3 translation, B3QueryFilter filter) {
        B3Vec3 center = new B3Vec3(x, 3.0f, -5.0f);
        B3Vec3Array points = new B3Vec3Array(1);
        points.SetValue(0, center);
        B3ShapeProxy proxy = new B3ShapeProxy(points, 1, 0.3f);
        B3RayResult result = world().CastShapeClosest(origin, proxy, translation, filter, false);
        world().AddDebugSphere(center, 0.3f, 0x008000, 1.0f);

        float fraction = result.GetHit() ? result.GetFraction() : 1.0f;
        B3Vec3 endCenter = new B3Vec3(x, 3.0f, -5.0f + 10.0f * fraction);
        world().AddDebugSphere(endCenter, 0.3f, result.GetHit() ? 0xFF0000 : 0x808080, 1.0f);
        if(result.GetHit()) {
            drawHit(result, 0.2f);
        }
        dispose(endCenter, result, proxy, points, center);
    }

    private void drawCapsuleCast(float x, B3Vec3 origin, B3Vec3 translation, B3QueryFilter filter) {
        B3Vec3 p1 = new B3Vec3(x - 0.2f, 4.8f, -5.2f);
        B3Vec3 p2 = new B3Vec3(x + 0.2f, 5.2f, -4.8f);
        B3Vec3Array points = new B3Vec3Array(2);
        points.SetValue(0, p1);
        points.SetValue(1, p2);
        B3ShapeProxy proxy = new B3ShapeProxy(points, 2, 0.2f);
        B3RayResult result = world().CastShapeClosest(origin, proxy, translation, filter, false);
        world().AddDebugCapsule(p1, p2, 0.2f, 0x008000, 1.0f);

        float z = 10.0f * (result.GetHit() ? result.GetFraction() : 1.0f);
        B3Vec3 end1 = new B3Vec3(p1.GetX(), p1.GetY(), p1.GetZ() + z);
        B3Vec3 end2 = new B3Vec3(p2.GetX(), p2.GetY(), p2.GetZ() + z);
        world().AddDebugCapsule(end1, end2, 0.2f, result.GetHit() ? 0xFF0000 : 0x808080, 1.0f);
        if(result.GetHit()) {
            drawHit(result, 0.2f);
        }
        dispose(end2, end1, result, proxy, points, p2, p1);
    }

    private void drawHullCast(float x, B3Vec3 origin, B3Vec3 translation, B3QueryFilter filter) {
        B3Quat qx = rotationX(0.25f * PI);
        B3Quat qy = rotationY(0.25f * PI);
        B3Quat qz = rotationZ(0.25f * PI);
        B3Quat qyz = B3Quat.Mul(qy, qz);
        B3Quat rotation = B3Quat.Mul(qx, qyz);
        B3Vec3 offset = new B3Vec3(x, 7.0f, -5.0f);
        B3Transform bakedTransform = new B3Transform(offset, rotation);
        B3Hull box = B3Hull.CreateTransformedBox(0.3f, 0.3f, 0.3f, bakedTransform);
        B3ShapeProxy proxy = new B3ShapeProxy(box, 0.0f);
        B3RayResult result = world().CastShapeClosest(origin, proxy, translation, filter, false);
        B3Transform identity = new B3Transform();
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        world().AddDebugHull(box, identity, one, 0x008000);

        float z = 10.0f * (result.GetHit() ? result.GetFraction() : 1.0f);
        B3Vec3 endOffset = new B3Vec3(0.0f, 0.0f, z);
        B3Quat identityRotation = new B3Quat();
        B3Transform endTransform = new B3Transform(endOffset, identityRotation);
        world().AddDebugHull(box, endTransform, one, result.GetHit() ? 0xFF0000 : 0x808080);
        if(result.GetHit()) {
            drawHit(result, 0.2f);
        }

        dispose(endTransform, identityRotation, endOffset, one, identity, result, proxy, box, bakedTransform,
                offset, rotation, qyz, qz, qy, qx);
    }

    private void drawHit(B3RayResult result, float normalLength) {
        B3Vec3 point = result.GetPoint();
        B3Vec3 normal = result.GetNormal();
        B3Vec3 normalEnd = new B3Vec3(point.GetX() + normalLength * normal.GetX(),
                point.GetY() + normalLength * normal.GetY(), point.GetZ() + normalLength * normal.GetZ());
        world().AddDebugPoint(point, 2.0f, 0xFF0000);
        world().AddDebugSegment(point, normalEnd, 0xFFFF00);
        dispose(normalEnd, normal, point);
    }
}
