package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3World;
import com.github.xpenatan.box3d.B3WorldDef;
import com.github.xpenatan.box3d.sample.shared.Box3DLaunchShape;
import com.github.xpenatan.box3d.sample.shared.Box3DSample;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleSettings;
import com.github.xpenatan.jParser.api.NativeObject;

abstract class AbstractBox3DSample implements Box3DSample {
    private final B3World world;
    private final int fallbackSubStepCount;
    private Box3DSampleSettings activeSettings;

    AbstractBox3DSample() {
        this(4);
    }

    AbstractBox3DSample(int subStepCount) {
        B3WorldDef worldDef = new B3WorldDef();
        B3Vec3 gravity = new B3Vec3(0.0f, -10.0f, 0.0f);
        worldDef.SetGravity(gravity);
        world = new B3World(worldDef);
        this.fallbackSubStepCount = subStepCount;
        dispose(gravity, worldDef);
    }

    @Override
    public void step(float deltaSeconds, Box3DSampleSettings settings) {
        activeSettings = settings;
        try {
            step(deltaSeconds);
        }
        finally {
            activeSettings = null;
        }
    }

    @Override
    public void step(float deltaSeconds) {
        Box3DSampleSettings settings = activeSettings;
        int subStepCount = fallbackSubStepCount;
        float step = Math.max(1.0f / 240.0f, Math.min(deltaSeconds, 1.0f / 30.0f));
        if(settings != null) {
            world.EnableSleeping(settings.sleepEnabled());
            world.EnableWarmStarting(settings.warmStartingEnabled());
            world.EnableContinuous(settings.continuousEnabled());
            world.SetWorkerCount(settings.workerCount());
            world.SetContactRecycleDistance(settings.recycleDistance());
            subStepCount = settings.subStepCount();
            step = Math.max(1.0f / Box3DSampleSettings.MAX_HERTZ,
                    Math.min(deltaSeconds, 1.0f / Box3DSampleSettings.MIN_HERTZ));
        }
        world.Step(step, subStepCount);
    }

    @Override
    public void launchShape(Box3DLaunchShape shape, float originX, float originY, float originZ, float directionX,
            float directionY, float directionZ, float speed) {
        if(world == null || !world.IsValid()) {
            return;
        }
        B3Body body = createLaunchedBody(originX, originY, originZ, directionX, directionY, directionZ, speed);
        switch(shape) {
            case BOX:
                attachLaunchBox(body);
                break;
            case CAPSULE:
                attachLaunchCapsule(body);
                break;
            case CYLINDER:
                attachLaunchCylinder(body);
                break;
            case SPHERE:
            default:
                attachLaunchSphere(body);
                break;
        }
    }

    @Override
    public B3World world() {
        return world;
    }

    @Override
    public void dispose() {
        if(world != null && world.IsValid()) {
            world.Destroy();
        }
        dispose(world);
    }

    protected void addGroundBox(float halfWidth, float halfHeight, float halfDepth) {
        addBox(B3.StaticBody(), 0.0f, -halfHeight, 0.0f, halfWidth, halfHeight, halfDepth, null, 0.0f, 0.6f,
                0.0f, 0.0f);
    }

    protected void addGroundBox(float halfWidth) {
        addGroundBox(halfWidth, 0.25f, halfWidth);
    }

    protected B3Body addStaticBox(float x, float y, float z, float halfWidth, float halfHeight, float halfDepth,
            B3Quat rotation) {
        return addBox(B3.StaticBody(), x, y, z, halfWidth, halfHeight, halfDepth, rotation, 0.0f, 0.6f, 0.0f,
                0.0f);
    }

    protected B3Body addDynamicBox(float x, float y, float z, float halfWidth, float halfHeight, float halfDepth) {
        return addBox(B3.DynamicBody(), x, y, z, halfWidth, halfHeight, halfDepth, null, 1.0f, 0.6f, 0.0f,
                0.0f);
    }

    protected B3Body addDynamicBox(float x, float y, float z, float halfWidth, float halfHeight, float halfDepth,
            B3Quat rotation, float density, float friction, float restitution, float rollingResistance) {
        return addBox(B3.DynamicBody(), x, y, z, halfWidth, halfHeight, halfDepth, rotation, density, friction,
                restitution, rollingResistance);
    }

    protected B3Body addDynamicBox(float x, float y, float z, float halfWidth, float halfHeight, float halfDepth,
            B3Quat rotation, float density, float friction, float restitution, float rollingResistance,
            B3Vec3 linearVelocity, B3Vec3 angularVelocity) {
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), x, y, z, rotation);
        if(linearVelocity != null) {
            bodyDef.SetLinearVelocity(linearVelocity);
        }
        if(angularVelocity != null) {
            bodyDef.SetAngularVelocity(angularVelocity);
        }
        B3ShapeDef shapeDef = shapeDef(density, friction, restitution, rollingResistance);
        B3Hull hull = B3Hull.CreateBox(halfWidth, halfHeight, halfDepth);
        B3Body body = world.CreateBody(bodyDef);
        dispose(body.CreateHullShape(shapeDef, hull));
        dispose(hull, shapeDef, bodyDef);
        return body;
    }

    protected B3Body addDynamicCylinder(float x, float y, float z, float height, float radius, int sides) {
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), x, y, z, null);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        B3Hull hull = B3Hull.CreateCylinder(height, radius, 0.0f, sides);
        B3Body body = world.CreateBody(bodyDef);
        dispose(body.CreateHullShape(shapeDef, hull));
        dispose(hull, shapeDef, bodyDef);
        return body;
    }

    protected B3Body addDynamicSphere(float x, float y, float z, float radius) {
        return addDynamicSphere(x, y, z, radius, 1.0f, 0.6f, 0.0f, 0.0f);
    }

    protected B3Body addDynamicSphere(float x, float y, float z, float radius, float density, float friction,
            float restitution, float rollingResistance) {
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), x, y, z, null);
        B3ShapeDef shapeDef = shapeDef(density, friction, restitution, rollingResistance);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, radius);
        B3Body body = world.CreateBody(bodyDef);
        dispose(body.CreateSphereShape(shapeDef, sphere));
        dispose(sphere, center, shapeDef, bodyDef);
        return body;
    }

    protected B3Body addDynamicCapsule(float x, float y, float z, float halfLength, float radius) {
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), x, y, z, null);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        B3Vec3 center1 = new B3Vec3(0.0f, -halfLength, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, halfLength, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, radius);
        B3Body body = world.CreateBody(bodyDef);
        dispose(body.CreateCapsuleShape(shapeDef, capsule));
        dispose(capsule, center2, center1, shapeDef, bodyDef);
        return body;
    }

    protected B3Body addDynamicCapsule(float x, float y, float z, float halfLength, float radius, B3Quat rotation,
            float density, float friction, float restitution, float rollingResistance) {
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), x, y, z, rotation);
        B3ShapeDef shapeDef = shapeDef(density, friction, restitution, rollingResistance);
        B3Vec3 center1 = new B3Vec3(-halfLength, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(halfLength, 0.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, radius);
        B3Body body = world.CreateBody(bodyDef);
        dispose(body.CreateCapsuleShape(shapeDef, capsule));
        dispose(capsule, center2, center1, shapeDef, bodyDef);
        return body;
    }

    protected B3Body addHull(B3Hull hull, int bodyType, float x, float y, float z, B3Quat rotation, float density,
            float friction, float restitution, float rollingResistance) {
        B3BodyDef bodyDef = bodyDef(bodyType, x, y, z, rotation);
        B3ShapeDef shapeDef = shapeDef(density, friction, restitution, rollingResistance);
        B3Body body = world.CreateBody(bodyDef);
        dispose(body.CreateHullShape(shapeDef, hull));
        dispose(shapeDef, bodyDef);
        return body;
    }

    protected B3Body addConveyorBox(float x, float y, float z, float halfWidth, float halfHeight, float halfDepth,
            B3Quat rotation, B3Vec3 tangentVelocity) {
        B3BodyDef bodyDef = bodyDef(B3.StaticBody(), x, y, z, rotation);
        B3ShapeDef shapeDef = shapeDef(0.0f, 0.8f, 0.0f, 0.0f, tangentVelocity);
        B3Hull hull = B3Hull.CreateBox(halfWidth, halfHeight, halfDepth);
        B3Body body = world.CreateBody(bodyDef);
        dispose(body.CreateHullShape(shapeDef, hull));
        dispose(hull, shapeDef, bodyDef);
        return body;
    }

    protected B3Body createBody(int bodyType, float x, float y, float z, B3Quat rotation) {
        B3BodyDef bodyDef = bodyDef(bodyType, x, y, z, rotation);
        B3Body body = world.CreateBody(bodyDef);
        dispose(bodyDef);
        return body;
    }

    protected void addBoxShape(B3Body body, float halfWidth, float halfHeight, float halfDepth, float density,
            float friction, float restitution, float rollingResistance) {
        addBoxShape(body, halfWidth, halfHeight, halfDepth, 0.0f, 0.0f, 0.0f, null, density, friction,
                restitution, rollingResistance);
    }

    protected void addBoxShape(B3Body body, float halfWidth, float halfHeight, float halfDepth, float offsetX,
            float offsetY, float offsetZ, B3Quat rotation, float density, float friction, float restitution,
            float rollingResistance) {
        dispose(createBoxShape(body, halfWidth, halfHeight, halfDepth, offsetX, offsetY, offsetZ, rotation, density,
                friction, restitution, rollingResistance));
    }

    protected B3Shape createBoxShape(B3Body body, float halfWidth, float halfHeight, float halfDepth, float density,
            float friction, float restitution, float rollingResistance) {
        return createBoxShape(body, halfWidth, halfHeight, halfDepth, 0.0f, 0.0f, 0.0f, null, density, friction,
                restitution, rollingResistance);
    }

    protected B3Shape createBoxShape(B3Body body, float halfWidth, float halfHeight, float halfDepth, float offsetX,
            float offsetY, float offsetZ, B3Quat rotation, float density, float friction, float restitution,
            float rollingResistance) {
        B3ShapeDef shapeDef = shapeDef(density, friction, restitution, rollingResistance);
        B3Vec3 offset = new B3Vec3(offsetX, offsetY, offsetZ);
        B3Quat localRotation = rotation != null ? rotation : new B3Quat();
        B3Transform transform = new B3Transform(offset, localRotation);
        B3Hull hull = B3Hull.CreateTransformedBox(halfWidth, halfHeight, halfDepth, transform);
        B3Shape shape = body.CreateHullShape(shapeDef, hull);
        dispose(hull, transform, offset, shapeDef);
        if(rotation == null) {
            dispose(localRotation);
        }
        return shape;
    }

    protected void addSphereShape(B3Body body, float x, float y, float z, float radius, float density,
            float friction, float restitution, float rollingResistance) {
        B3ShapeDef shapeDef = shapeDef(density, friction, restitution, rollingResistance);
        B3Vec3 center = new B3Vec3(x, y, z);
        B3Sphere sphere = new B3Sphere(center, radius);
        B3Shape shape = body.CreateSphereShape(shapeDef, sphere);
        dispose(shape, sphere, center, shapeDef);
    }

    protected void addCapsuleShape(B3Body body, float x1, float y1, float z1, float x2, float y2, float z2,
            float radius, float density, float friction, float restitution, float rollingResistance) {
        B3ShapeDef shapeDef = shapeDef(density, friction, restitution, rollingResistance);
        B3Vec3 center1 = new B3Vec3(x1, y1, z1);
        B3Vec3 center2 = new B3Vec3(x2, y2, z2);
        B3Capsule capsule = new B3Capsule(center1, center2, radius);
        B3Shape shape = body.CreateCapsuleShape(shapeDef, capsule);
        dispose(shape, capsule, center2, center1, shapeDef);
    }

    protected B3Shape createCapsuleShape(B3Body body, float x1, float y1, float z1, float x2, float y2, float z2,
            float radius, float density, float friction, float restitution, float rollingResistance) {
        B3ShapeDef shapeDef = shapeDef(density, friction, restitution, rollingResistance);
        B3Vec3 center1 = new B3Vec3(x1, y1, z1);
        B3Vec3 center2 = new B3Vec3(x2, y2, z2);
        B3Capsule capsule = new B3Capsule(center1, center2, radius);
        B3Shape shape = body.CreateCapsuleShape(shapeDef, capsule);
        dispose(capsule, center2, center1, shapeDef);
        return shape;
    }

    protected B3BodyDef bodyDef(int bodyType, float x, float y, float z, B3Quat rotation) {
        B3BodyDef bodyDef = new B3BodyDef();
        B3Vec3 position = new B3Vec3(x, y, z);
        bodyDef.SetType(bodyType);
        bodyDef.SetPosition(position);
        if(rotation != null) {
            bodyDef.SetRotation(rotation);
        }
        dispose(position);
        return bodyDef;
    }

    protected B3MotionLocks motionLocks(boolean linearX, boolean linearY, boolean linearZ, boolean angularX,
            boolean angularY, boolean angularZ) {
        B3MotionLocks locks = new B3MotionLocks();
        locks.SetLinearX(linearX);
        locks.SetLinearY(linearY);
        locks.SetLinearZ(linearZ);
        locks.SetAngularX(angularX);
        locks.SetAngularY(angularY);
        locks.SetAngularZ(angularZ);
        return locks;
    }

    protected B3ShapeDef shapeDef(float density, float friction, float restitution, float rollingResistance) {
        return shapeDef(density, friction, restitution, rollingResistance, null);
    }

    protected B3ShapeDef shapeDef(float density, float friction, float restitution, float rollingResistance,
            B3Vec3 tangentVelocity) {
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetDensity(density);
        B3SurfaceMaterial material = new B3SurfaceMaterial();
        material.SetFriction(friction);
        material.SetRestitution(restitution);
        material.SetRollingResistance(rollingResistance);
        if(tangentVelocity != null) {
            material.SetTangentVelocity(tangentVelocity);
        }
        shapeDef.SetBaseMaterial(material);
        dispose(material);
        return shapeDef;
    }

    private B3Body createLaunchedBody(float originX, float originY, float originZ, float directionX, float directionY,
            float directionZ, float speed) {
        float spawnDistance = 2.0f;
        B3Vec3 position = new B3Vec3(originX + directionX * spawnDistance, originY + directionY * spawnDistance,
                originZ + directionZ * spawnDistance);
        B3Vec3 velocity = new B3Vec3(directionX * speed, directionY * speed, directionZ * speed);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        bodyDef.SetPosition(position);
        bodyDef.SetLinearVelocity(velocity);
        bodyDef.SetIsBullet(true);
        B3Body body = world.CreateBody(bodyDef);
        dispose(velocity, position, bodyDef);
        return body;
    }

    private void attachLaunchSphere(B3Body body) {
        B3ShapeDef shapeDef = shapeDef(4.0f, 0.6f, 0.0f, 0.0f);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.25f);
        dispose(body.CreateSphereShape(shapeDef, sphere));
        dispose(sphere, center, shapeDef);
    }

    private void attachLaunchBox(B3Body body) {
        B3ShapeDef shapeDef = shapeDef(4.0f, 0.6f, 0.0f, 0.0f);
        B3Hull hull = B3Hull.CreateBox(0.28f, 0.28f, 0.28f);
        dispose(body.CreateHullShape(shapeDef, hull));
        dispose(hull, shapeDef);
    }

    private void attachLaunchCapsule(B3Body body) {
        B3ShapeDef shapeDef = shapeDef(4.0f, 0.6f, 0.0f, 0.0f);
        B3Vec3 center1 = new B3Vec3(-0.45f, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.45f, 0.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, 0.16f);
        dispose(body.CreateCapsuleShape(shapeDef, capsule));
        dispose(capsule, center2, center1, shapeDef);
    }

    private void attachLaunchCylinder(B3Body body) {
        B3ShapeDef shapeDef = shapeDef(4.0f, 0.6f, 0.0f, 0.0f);
        B3Hull hull = B3Hull.CreateCylinder(1.1f, 0.18f, 0.0f, 12);
        dispose(body.CreateHullShape(shapeDef, hull));
        dispose(hull, shapeDef);
    }

    protected B3Quat rotationX(float radians) {
        float half = radians * 0.5f;
        return new B3Quat((float)Math.sin(half), 0.0f, 0.0f, (float)Math.cos(half));
    }

    protected B3Quat rotationY(float radians) {
        float half = radians * 0.5f;
        return new B3Quat(0.0f, (float)Math.sin(half), 0.0f, (float)Math.cos(half));
    }

    protected B3Quat rotationZ(float radians) {
        float half = radians * 0.5f;
        return new B3Quat(0.0f, 0.0f, (float)Math.sin(half), (float)Math.cos(half));
    }

    private B3Body addBox(int bodyType, float x, float y, float z, float halfWidth, float halfHeight, float halfDepth,
            B3Quat rotation, float density, float friction, float restitution, float rollingResistance) {
        B3BodyDef bodyDef = bodyDef(bodyType, x, y, z, rotation);
        B3ShapeDef shapeDef = shapeDef(density, friction, restitution, rollingResistance);
        B3Hull hull = B3Hull.CreateBox(halfWidth, halfHeight, halfDepth);
        B3Body body = world.CreateBody(bodyDef);
        dispose(body.CreateHullShape(shapeDef, hull));
        dispose(hull, shapeDef, bodyDef);
        return body;
    }

    protected static void dispose(NativeObject... objects) {
        for(NativeObject object : objects) {
            if(object != null && object.native_hasOwnership() && !object.isDisposed()) {
                object.dispose();
            }
        }
    }
}
