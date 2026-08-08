package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3HeightField;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3ShapeProxy;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact default port of Collision / Overlap World from the pinned Box3D commit. */
final class OverlapWorldSample extends AbstractBox3DSample {
    private static final float PI = (float)Math.PI;
    private final B3Mesh mesh;
    private final B3HeightField heightField;

    OverlapWorldSample() {
        B3Vec3 zeroGravity = new B3Vec3();
        world().SetGravity(zeroGravity);

        B3Hull box = B3Hull.CreateBox(0.6f, 0.6f, 0.6f);
        mesh = B3Mesh.CreateTorus(10, 12, 0.65f, 0.35f);
        B3Vec3 heightScale = new B3Vec3(0.2f, 0.2f, 0.2f);
        heightField = B3HeightField.CreateWave(10, 10, heightScale, 0.03f, 0.09f, false);
        B3BodyDef bodyDef = new B3BodyDef();
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 position = new B3Vec3();

        for(int index = 0; index < 3; ++index) {
            int bodyType = index == 0 ? B3.StaticBody() : index == 1 ? B3.KinematicBody() : B3.DynamicBody();
            float y = 3.0f + 2.0f * index;
            bodyDef.SetType(bodyType);

            B3Quat sphereRotation = rotationX(0.5f * PI);
            position.Set(-6.0f, y, 0.0f);
            bodyDef.SetPosition(position);
            bodyDef.SetRotation(sphereRotation);
            B3Body sphereBody = world().CreateBody(bodyDef);
            B3Vec3 sphereCenter = new B3Vec3();
            B3Sphere sphere = new B3Sphere(sphereCenter, 0.8f);
            dispose(sphereBody.CreateSphereShape(shapeDef, sphere), sphereBody, sphere, sphereCenter, sphereRotation);

            B3Quat capsuleRotation = rotationZ(0.25f * PI);
            position.Set(-3.0f, y, 0.0f);
            bodyDef.SetPosition(position);
            bodyDef.SetRotation(capsuleRotation);
            B3Body capsuleBody = world().CreateBody(bodyDef);
            B3Vec3 center1 = new B3Vec3(-0.5f, 0.0f, 0.0f);
            B3Vec3 center2 = new B3Vec3(0.5f, 0.0f, 0.0f);
            B3Capsule capsule = new B3Capsule(center1, center2, 0.5f);
            dispose(capsuleBody.CreateCapsuleShape(shapeDef, capsule), capsuleBody, capsule, center2, center1,
                    capsuleRotation);

            B3Quat hullRotation = rotationZ(0.25f * PI);
            position.Set(0.0f, y, 0.0f);
            bodyDef.SetPosition(position);
            bodyDef.SetRotation(hullRotation);
            B3Body hullBody = world().CreateBody(bodyDef);
            dispose(hullBody.CreateHullShape(shapeDef, box), hullBody, hullRotation);

            B3Quat meshRotation = rotationX(0.5f * PI);
            position.Set(3.0f, y, 0.0f);
            bodyDef.SetPosition(position);
            bodyDef.SetRotation(meshRotation);
            B3Body meshBody = world().CreateBody(bodyDef);
            B3Vec3 meshScale = new B3Vec3(-0.5f, 1.5f, -1.0f);
            dispose(meshBody.CreateMeshShape(shapeDef, mesh, meshScale), meshBody, meshScale, meshRotation);

            bodyDef.SetType(B3.StaticBody());
            B3Quat heightRotation = rotationX(-0.5f * PI);
            position.Set(5.0f, 2.0f + 2.0f * index, 0.0f);
            bodyDef.SetPosition(position);
            bodyDef.SetRotation(heightRotation);
            B3Body heightBody = world().CreateBody(bodyDef);
            dispose(heightBody.CreateHeightFieldShape(shapeDef, heightField), heightBody, heightRotation);
        }

        dispose(position, shapeDef, box, heightScale, zeroGravity, bodyDef);
        drawOverlaps();
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        drawOverlaps();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(heightField, mesh);
    }

    private void drawOverlaps() {
        world().ClearDebugOverlay();
        B3Vec3 origin = new B3Vec3();
        B3QueryFilter filter = new B3QueryFilter();
        for(int i = 0; i < 5; ++i) {
            float x = -6.0f + 3.0f * i;
            drawSphereOverlap(x, origin, filter);
            drawCapsuleOverlap(x, origin, filter);
            drawHullOverlap(x, origin, filter);
        }
        addDebugGroundGrid(10);
        addDebugAxes(0.0f, 0.0f, 0.0f, 0.4f);
        dispose(filter, origin);
    }

    private void drawSphereOverlap(float x, B3Vec3 origin, B3QueryFilter filter) {
        B3Vec3 center = new B3Vec3(x, 3.0f, -5.0f);
        B3Vec3Array points = new B3Vec3Array(1);
        points.SetValue(0, center);
        B3ShapeProxy proxy = new B3ShapeProxy(points, 1, 0.3f);
        boolean overlap = world().OverlapShape(origin, proxy, filter);
        world().AddDebugSphere(center, 0.3f, overlap ? 0xFF0000 : 0x008000, 1.0f);
        dispose(proxy, points, center);
    }

    private void drawCapsuleOverlap(float x, B3Vec3 origin, B3QueryFilter filter) {
        B3Vec3 p1 = new B3Vec3(x - 0.2f, 4.8f, -5.2f);
        B3Vec3 p2 = new B3Vec3(x + 0.2f, 5.2f, -4.8f);
        B3Vec3Array points = new B3Vec3Array(2);
        points.SetValue(0, p1);
        points.SetValue(1, p2);
        B3ShapeProxy proxy = new B3ShapeProxy(points, 2, 0.2f);
        boolean overlap = world().OverlapShape(origin, proxy, filter);
        world().AddDebugCapsule(p1, p2, 0.2f, overlap ? 0xFF0000 : 0x008000, 1.0f);
        dispose(proxy, points, p2, p1);
    }

    private void drawHullOverlap(float x, B3Vec3 origin, B3QueryFilter filter) {
        B3Vec3 position = new B3Vec3(x, 7.0f, -5.0f);
        B3Quat identityRotation = new B3Quat();
        B3Transform bakedTransform = new B3Transform(position, identityRotation);
        B3Hull box = B3Hull.CreateTransformedBox(0.3f, 0.3f, 0.3f, bakedTransform);
        B3ShapeProxy proxy = new B3ShapeProxy(box, 0.0f);
        boolean overlap = world().OverlapShape(origin, proxy, filter);
        B3Transform identity = new B3Transform();
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        world().AddDebugHull(box, identity, one, overlap ? 0xFF0000 : 0x008000);
        dispose(one, identity, proxy, box, bakedTransform, identityRotation, position);
    }
}
