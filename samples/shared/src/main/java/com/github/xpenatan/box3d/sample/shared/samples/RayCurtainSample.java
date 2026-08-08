package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact port of Collision / Ray Curtain. */
final class RayCurtainSample extends AbstractBox3DSample {
    private static final float ABS_SPEED = 0.015f;

    private final B3Mesh mesh;
    private float offset = 2.0f;
    private float speed = -ABS_SPEED;

    RayCurtainSample() {
        B3Hull box = B3Hull.CreateBox(0.6f, 0.6f, 0.6f);
        mesh = B3Mesh.CreateTorus(10, 12, 0.65f, 0.35f);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.KinematicBody());
        B3Vec3 angularVelocity = new B3Vec3(0.8f, 0.4f, 0.8f);
        bodyDef.SetAngularVelocity(angularVelocity);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 position = new B3Vec3();

        position.Set(-6.0f, 3.0f, 0.0f);
        bodyDef.SetPosition(position);
        B3Body sphereBody = world().CreateBody(bodyDef);
        B3Vec3 center = new B3Vec3();
        B3Sphere sphere = new B3Sphere(center, 0.9f);
        dispose(sphereBody.CreateSphereShape(shapeDef, sphere), sphereBody);

        position.Set(-2.0f, 3.0f, 0.0f);
        bodyDef.SetPosition(position);
        B3Body capsuleBody = world().CreateBody(bodyDef);
        B3Vec3 center1 = new B3Vec3(-0.5f, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.5f, 0.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, 0.8f);
        dispose(capsuleBody.CreateCapsuleShape(shapeDef, capsule), capsuleBody);

        position.Set(2.0f, 3.0f, 0.0f);
        bodyDef.SetPosition(position);
        B3Body hullBody = world().CreateBody(bodyDef);
        dispose(hullBody.CreateHullShape(shapeDef, box), hullBody);

        position.Set(6.0f, 3.0f, 0.0f);
        bodyDef.SetPosition(position);
        B3Body meshBody = world().CreateBody(bodyDef);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(meshBody.CreateMeshShape(shapeDef, mesh, one), meshBody);
        dispose(one, capsule, center2, center1, sphere, center, position, shapeDef, angularVelocity, bodyDef, box);
        drawCurtain(false);
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        drawCurtain(true);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }

    private void drawCurtain(boolean advance) {
        world().ClearDebugOverlay();
        addDebugGroundGrid(10);
        addDebugAxes(0.0f, 0.0f, 0.0f, 0.4f);
        B3QueryFilter filter = new B3QueryFilter();
        for(float x = -8.0f; x <= 8.0001f; x += 0.1f) {
            B3Vec3 origin = new B3Vec3(x, 8.0f, offset);
            B3Vec3 end = new B3Vec3(x, 0.0f, offset);
            B3Vec3 translation = new B3Vec3(0.0f, -8.0f, 0.0f);
            B3RayResult result = world().CastRayClosest(origin, translation, filter);
            if(result.GetHit()) {
                B3Vec3 point = result.GetPoint();
                B3Vec3 normal = result.GetNormal();
                B3Vec3 normalEnd = new B3Vec3(point.GetX() + 0.5f * normal.GetX(),
                        point.GetY() + 0.5f * normal.GetY(), point.GetZ() + 0.5f * normal.GetZ());
                world().AddDebugSegment(point, normalEnd, 0x008000);
                dispose(normalEnd, normal, point);
            }
            world().AddDebugPoint(origin, 4.0f, 0x008000);
            world().AddDebugPoint(end, 4.0f, 0xFF0000);
            world().AddDebugSegment(origin, end, 0xFFFF00);
            dispose(result, translation, end, origin);
        }
        dispose(filter);

        if(advance) {
            if(offset > 2.0f) {
                speed = -ABS_SPEED;
            }
            else if(offset < -2.0f) {
                speed = ABS_SPEED;
            }
            offset += speed;
        }
    }
}
