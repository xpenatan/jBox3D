package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3AABB;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact query-driven scene update from shared/determinism.c. */
final class DeterminismQuerySpawnSample extends AbstractBox3DSample {
    private final SampleRandom random = new SampleRandom(71689);
    private int frameCount;
    private int spawnCount;

    DeterminismQuerySpawnSample() {
        B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
        world().SetGravity(zero);
        dispose(zero);
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        if(spawnCount < 50) {
            frameCount++;
            if(frameCount % 10 == 1) {
                spawnOnce();
            }
        }
    }

    private void spawnOnce() {
        B3QueryFilter queryFilter = new B3QueryFilter();
        B3Vec3 rayOrigin = randomVector(-12.0f, 12.0f);
        B3Vec3 rayDirection = randomUnitVector();
        B3Vec3 rayTranslation = new B3Vec3(30.0f * rayDirection.GetX(), 30.0f * rayDirection.GetY(),
                30.0f * rayDirection.GetZ());
        B3RayResult ray = world().CastRayClosest(rayOrigin, rayTranslation, queryFilter);

        B3Vec3 spawnPosition;
        if(ray.GetHit()) {
            B3Vec3 point = ray.GetPoint();
            B3Vec3 normal = ray.GetNormal();
            spawnPosition = new B3Vec3(point.GetX() + 1.2f * normal.GetX(), point.GetY() + 1.2f * normal.GetY(),
                    point.GetZ() + 1.2f * normal.GetZ());
            dispose(normal, point);
        }
        else {
            spawnPosition = randomVector(-6.0f, 6.0f);
        }

        B3Vec3 center = randomVector(-10.0f, 10.0f);
        float extent = random.nextFloat(1.0f, 4.0f);
        B3Vec3 lower = new B3Vec3(center.GetX() - extent, center.GetY() - extent, center.GetZ() - extent);
        B3Vec3 upper = new B3Vec3(center.GetX() + extent, center.GetY() + extent, center.GetZ() + extent);
        B3AABB bounds = new B3AABB(lower, upper);
        int overlapCount = world().CountOverlapsAABB(bounds, queryFilter);
        float fraction = world().CastSphereClosestFraction(rayOrigin, 0.5f, rayTranslation, queryFilter);
        float size = 0.3f + 0.2f * fraction;

        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        bodyDef.SetPosition(spawnPosition);
        B3Quat rotation = random.nextQuaternion();
        B3Vec3 linearVelocity = randomVector(-0.2f, 0.2f);
        B3Vec3 angularVelocity = randomVector(-0.5f, 0.5f);
        bodyDef.SetRotation(rotation);
        bodyDef.SetLinearVelocity(linearVelocity);
        bodyDef.SetAngularVelocity(angularVelocity);
        bodyDef.SetLinearDamping(1.0f);
        bodyDef.SetAngularDamping(1.0f);
        B3Body body = world().CreateBody(bodyDef);

        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetRollingResistance(0.2f);
        shapeDef.SetBaseMaterial(material);
        switch((spawnCount + overlapCount) % 3) {
            case 0: {
                B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
                B3Sphere sphere = new B3Sphere(zero, size);
                dispose(body.CreateSphereShape(shapeDef, sphere), sphere, zero);
                break;
            }
            case 1: {
                B3Vec3 point1 = new B3Vec3(0.0f, -size, 0.0f);
                B3Vec3 point2 = new B3Vec3(0.0f, size, 0.0f);
                B3Capsule capsule = new B3Capsule(point1, point2, 0.7f * size);
                dispose(body.CreateCapsuleShape(shapeDef, capsule), capsule, point2, point1);
                break;
            }
            default: {
                B3Hull box = B3Hull.CreateBox(size, 0.7f * size, 0.5f * size);
                dispose(body.CreateHullShape(shapeDef, box), box);
                break;
            }
        }
        spawnCount++;

        dispose(material, shapeDef, body, angularVelocity, linearVelocity, rotation, bodyDef, bounds, upper, lower,
                center, spawnPosition, ray, rayTranslation, rayDirection, rayOrigin, queryFilter);
    }

    private B3Vec3 randomVector(float minimum, float maximum) {
        return new B3Vec3(random.nextFloat(minimum, maximum), random.nextFloat(minimum, maximum),
                random.nextFloat(minimum, maximum));
    }

    private B3Vec3 randomUnitVector() {
        return random.nextUnitVector();
    }
}
