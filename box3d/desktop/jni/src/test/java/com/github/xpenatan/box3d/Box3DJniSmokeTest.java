package com.github.xpenatan.box3d;

import com.github.xpenatan.jParser.loader.JParserLibraryLoaderListener;
import com.github.xpenatan.jparser.runtime.helper.NativeString;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class Box3DJniSmokeTest {

    private static final float EPSILON = 0.0001f;

    @Test
    public void expandedBindingsRoundTripThroughJni() throws Exception {
        loadBox3D();

        B3WorldDef worldDef = new B3WorldDef();
        B3World world = new B3World(worldDef);
        B3BodyDef groundDef = new B3BodyDef();
        B3Body ground = world.CreateBody(groundDef);
        B3BodyDef bodyDef = new B3BodyDef();
        B3Vec3 bodyPosition = new B3Vec3(0.0f, 2.0f, 0.0f);
        bodyDef.SetType(B3.DynamicBody());
        bodyDef.SetPosition(bodyPosition);
        B3Body body = world.CreateBody(bodyDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetDensity(2.0f);
        B3Vec3 sphereCenter = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(sphereCenter, 0.5f);
        B3Shape shape = body.CreateSphereShape(shapeDef, sphere);
        NativeString bodyName = new NativeString();
        NativeString shapeName = new NativeString();
        B3MassData bodyMassData = null;
        B3MassData shapeMassData = null;
        B3MassData computedMassData = null;
        B3WheelJointDef wheelDef = new B3WheelJointDef();
        B3Joint wheel = null;

        try {
            assertEquals(0, B3.GetVersionMajor());
            assertEquals(1, B3.GetVersionMinor());
            assertEquals(0, B3.GetVersionRevision());
            assertEquals((float)(Math.PI * 0.5), B3.Atan2(1.0f, 0.0f), 0.0001f);
            assertTrue(B3.IsValidFloat(1.0f));
            assertTrue(!B3.IsValidFloat(Float.NaN));
            assertTrue(B3.IsValidVec3(bodyPosition));
            assertTrue(B3.GetGraphColor(0) != 0);

            body.SetName("binding-body");
            body.GetName(bodyName);
            assertEquals("binding-body", bodyName.c_str());
            shape.SetName("binding-shape");
            shape.GetName(shapeName);
            assertEquals("binding-shape", shapeName.c_str());

            assertEquals(world.GetId(), body.GetWorldId());
            assertEquals(world.GetId(), shape.GetWorldId());
            assertEquals(shape.GetId(), body.GetShapeId(0));

            bodyMassData = body.GetMassData();
            shapeMassData = shape.ComputeMassData();
            computedMassData = B3Collision.ComputeSphereMass(sphere, 2.0f);
            assertTrue(bodyMassData.GetMass() > 0.0f);
            assertEquals(shapeMassData.GetMass(), bodyMassData.GetMass(), EPSILON);
            assertEquals(shapeMassData.GetMass(), computedMassData.GetMass(), EPSILON);

            body.SetSleepThreshold(0.25f);
            assertEquals(0.25f, body.GetSleepThreshold(), EPSILON);
            shape.EnablePreSolveEvents(true);
            assertTrue(shape.ArePreSolveEventsEnabled());

            wheelDef.SetBodyIdA(ground.GetId());
            wheelDef.SetBodyIdB(body.GetId());
            wheel = world.CreateWheelJoint(wheelDef);
            wheel.EnableWheelSteering(true);
            wheel.SetWheelTargetSteeringAngle(0.35f);
            wheel.EnableWheelSpinMotor(true);
            wheel.SetWheelSpinMotorSpeed(4.5f);
            assertTrue(wheel.IsWheelSteeringEnabled());
            assertEquals(0.35f, wheel.GetWheelTargetSteeringAngle(), EPSILON);
            assertTrue(wheel.IsWheelSpinMotorEnabled());
            assertEquals(4.5f, wheel.GetWheelSpinMotorSpeed(), EPSILON);
        }
        finally {
            if(world.IsValid()) {
                world.Destroy();
            }
            dispose(wheel, wheelDef, computedMassData, shapeMassData, bodyMassData, shapeName, bodyName,
                    shape, sphere, sphereCenter, shapeDef, body, bodyPosition, bodyDef, ground, groundDef,
                    world, worldDef);
        }
    }

    @Test
    public void createStepAndDestroyWorld() throws Exception {
        loadBox3D();

        B3WorldDef worldDef = new B3WorldDef();
        B3Vec3 gravity = new B3Vec3(0f, -10f, 0f);
        B3Vec3 startPosition = new B3Vec3(0f, 5f, 0f);
        B3Vec3 sphereCenter = new B3Vec3(0f, 0f, 0f);
        B3BodyDef bodyDef = new B3BodyDef();
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Sphere sphere = new B3Sphere(sphereCenter, 0.5f);
        B3World world = null;

        try {
            worldDef.SetGravity(gravity);

            world = new B3World(worldDef);
            assertTrue(world.IsValid());

            bodyDef.SetType(B3.DynamicBody());
            bodyDef.SetPosition(startPosition);

            B3Body body = world.CreateBody(bodyDef);
            assertTrue(body.IsValid());

            shapeDef.SetDensity(1f);
            B3Shape shape = body.CreateSphereShape(shapeDef, sphere);
            assertTrue(shape.IsValid());
            assertEquals(1, body.GetShapeCount());

            float beforeY = body.GetPosition().GetY();
            world.Step(1f / 60f, 4);
            float afterY = body.GetPosition().GetY();

            assertTrue("dynamic body should move under gravity", afterY < beforeY);
        }
        finally {
            if(world != null && world.IsValid()) {
                world.Destroy();
            }
            dispose(world, sphere, shapeDef, bodyDef, sphereCenter, startPosition, gravity, worldDef);
        }
    }

    @Test
    public void boxHullDebugGeometryMatchesItsCDefinition() throws Exception {
        loadBox3D();

        B3WorldDef worldDef = new B3WorldDef();
        B3BodyDef bodyDef = new B3BodyDef();
        B3Vec3 position = new B3Vec3(4.0f, 5.0f, 6.0f);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Hull hull = B3Hull.CreateBox(1.0f, 2.0f, 3.0f);
        B3Hull otherHull = B3Hull.CreateBox(7.0f, 8.0f, 9.0f);
        HullCaptureDraw draw = new HullCaptureDraw();
        B3World world = null;
        B3Body body = null;
        B3Shape shape = null;

        try {
            assertNotSame("each native hull result needs an independent Java handle", hull, otherHull);
            world = new B3World(worldDef);
            bodyDef.SetPosition(position);
            body = world.CreateBody(bodyDef);
            shape = body.CreateHullShape(shapeDef, hull);

            draw.SetDrawJoints(false);
            draw.DrawWorld(world, B3.DefaultMaskBits());

            assertTrue("the hull should be submitted to debug draw", draw.called);
            assertEquals(12, draw.edgeCount);
            assertEquals(-1.0f, draw.minX, EPSILON);
            assertEquals(1.0f, draw.maxX, EPSILON);
            assertEquals(-2.0f, draw.minY, EPSILON);
            assertEquals(2.0f, draw.maxY, EPSILON);
            assertEquals(-3.0f, draw.minZ, EPSILON);
            assertEquals(3.0f, draw.maxZ, EPSILON);
            assertEquals(4.0f, draw.transformX, EPSILON);
            assertEquals(5.0f, draw.transformY, EPSILON);
            assertEquals(6.0f, draw.transformZ, EPSILON);
        }
        finally {
            if(world != null && world.IsValid()) {
                world.Destroy();
            }
            dispose(shape, body, draw, otherHull, hull, shapeDef, position, bodyDef, world, worldDef);
        }
    }

    @Test
    public void dominoMassAndStarterImpulseMatchTheCExample() throws Exception {
        loadBox3D();

        B3WorldDef worldDef = new B3WorldDef();
        B3World world = new B3World(worldDef);
        B3BodyDef bodyDef = new B3BodyDef();
        B3Vec3 position = new B3Vec3(7.0f, 0.8f, 0.0f);
        bodyDef.SetType(B3.DynamicBody());
        bodyDef.SetPosition(position);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Hull domino = B3Hull.CreateBox(0.2f, 0.8f, 0.05f);
        B3Body body = world.CreateBody(bodyDef);
        B3Shape shape = body.CreateHullShape(shapeDef, domino);
        B3Vec3 impulse = new B3Vec3(0.0f, 0.0f, 25.0f);
        B3Vec3 impulsePoint = new B3Vec3(7.0f, 1.6f, 0.0f);

        try {
            assertEquals("exact commit default density", 1000.0f, shapeDef.GetDensity(), EPSILON);
            assertEquals("exact C domino mass", 64.0f, body.GetMass(), EPSILON);

            body.ApplyLinearImpulse(impulse, impulsePoint, true);
            B3Vec3 velocity = body.GetLinearVelocity();
            assertEquals(0.0f, velocity.GetX(), EPSILON);
            assertEquals(0.0f, velocity.GetY(), EPSILON);
            assertEquals("25 N*s / 64 kg", 0.390625f, velocity.GetZ(), EPSILON);
            dispose(velocity);

            for(int frame = 0; frame < 3; frame++) {
                world.Step(1.0f / 60.0f, 4);
            }

            B3Vec3 steppedPosition = body.GetPosition();
            assertTrue("the starter domino must not be launched across the scene",
                    Math.abs(steppedPosition.GetZ()) < 0.1f);
            dispose(steppedPosition);
        }
        finally {
            if(world.IsValid()) {
                world.Destroy();
            }
            dispose(impulsePoint, impulse, shape, body, domino, shapeDef, position, bodyDef, world, worldDef);
        }
    }

    @Test
    public void bridgeJointChainStartsFromTheSameStateAsTheCExample() throws Exception {
        loadBox3D();

        final int count = 150;
        final float halfWidth = 0.125f;
        final float xBase = -160.0f * halfWidth;
        B3WorldDef worldDef = new B3WorldDef();
        B3World world = new B3World(worldDef);
        B3BodyDef groundDef = new B3BodyDef();
        B3Body ground = world.CreateBody(groundDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetDensity(20.0f);
        B3Hull box = B3Hull.CreateBox(halfWidth, 0.125f, 0.5f);
        B3SphericalJointDef jointDef = new B3SphericalJointDef();
        jointDef.SetConstraintHertz(1000.0f);
        jointDef.SetEnableSpring(true);
        jointDef.SetHertz(2.0f);
        jointDef.SetDampingRatio(1.0f);
        B3Body[] bodies = new B3Body[count];
        B3Body previous = ground;
        float previousX = 0.0f;
        float previousY = 0.0f;

        try {
            for(int i = 0; i < count; i++) {
                float bodyX = xBase + halfWidth * (1.0f + 2.0f * i);
                B3BodyDef bodyDef = new B3BodyDef();
                B3Vec3 bodyPosition = new B3Vec3(bodyX, 20.0f, 0.0f);
                bodyDef.SetType(B3.DynamicBody());
                bodyDef.SetPosition(bodyPosition);
                bodyDef.SetLinearDamping(0.1f);
                bodyDef.SetAngularDamping(0.1f);
                B3Body body = world.CreateBody(bodyDef);
                B3Shape shape = body.CreateHullShape(shapeDef, box);
                bodies[i] = body;

                float pivotX = xBase + 2.0f * halfWidth * i;
                assertBridgeJoint(world, jointDef, previous, body, pivotX - previousX, 20.0f - previousY, -0.5f,
                        pivotX - bodyX, 0.0f, -0.5f);
                assertBridgeJoint(world, jointDef, previous, body, pivotX - previousX, 20.0f - previousY, 0.5f,
                        pivotX - bodyX, 0.0f, 0.5f);

                previous = body;
                previousX = bodyX;
                previousY = 20.0f;
                dispose(shape, bodyPosition, bodyDef);
            }

            float finalPivotX = xBase + 2.0f * halfWidth * count;
            assertBridgeJoint(world, jointDef, previous, ground, finalPivotX - previousX, 0.0f, -0.5f,
                    finalPivotX, 20.0f, -0.5f);
            assertBridgeJoint(world, jointDef, previous, ground, finalPivotX - previousX, 0.0f, 0.5f,
                    finalPivotX, 20.0f, 0.5f);

            for(int step = 0; step < 3; step++) {
                world.Step(1.0f / 60.0f, 4);
            }

            for(int i = 0; i < count; i++) {
                float expectedX = xBase + halfWidth * (1.0f + 2.0f * i);
                B3Vec3 actual = bodies[i].GetPosition();
                assertEquals("bridge body " + i + " x", expectedX, actual.GetX(), 0.25f);
                assertEquals("bridge body " + i + " y", 20.0f, actual.GetY(), 0.25f);
                assertEquals("bridge body " + i + " z", 0.0f, actual.GetZ(), 0.25f);
                dispose(actual);
            }
        }
        finally {
            if(world.IsValid()) {
                world.Destroy();
            }
            dispose(bodies);
            dispose(jointDef, box, shapeDef, ground, groundDef, world, worldDef);
        }
    }

    private static void assertBridgeJoint(B3World world, B3SphericalJointDef jointDef, B3Body bodyA, B3Body bodyB,
            float localAX, float localAY, float localAZ, float localBX, float localBY, float localBZ) {
        B3Vec3 localA = new B3Vec3(localAX, localAY, localAZ);
        B3Vec3 localB = new B3Vec3(localBX, localBY, localBZ);
        jointDef.SetBodyIdA(bodyA.GetId());
        jointDef.SetBodyIdB(bodyB.GetId());
        jointDef.SetLocalPositionA(localA);
        jointDef.SetLocalPositionB(localB);
        B3Transform storedFrameA = jointDef.GetLocalFrameA();
        B3Vec3 storedA = storedFrameA.GetP();
        assertEquals(localAX, storedA.GetX(), EPSILON);
        assertEquals(localAY, storedA.GetY(), EPSILON);
        assertEquals(localAZ, storedA.GetZ(), EPSILON);
        B3Transform storedFrameB = jointDef.GetLocalFrameB();
        B3Vec3 storedB = storedFrameB.GetP();
        assertEquals(localBX, storedB.GetX(), EPSILON);
        assertEquals(localBY, storedB.GetY(), EPSILON);
        assertEquals(localBZ, storedB.GetZ(), EPSILON);
        B3Joint joint = world.CreateSphericalJoint(jointDef);
        assertEquals("joint should start without translation error", 0.0f, joint.GetLinearSeparation(), EPSILON);
        dispose(joint, localB, localA);
    }

    private static final class HullCaptureDraw extends B3DebugDrawEm {
        boolean called;
        int edgeCount;
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float minZ = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        float maxZ = Float.NEGATIVE_INFINITY;
        float transformX;
        float transformY;
        float transformZ;

        @Override
        protected void DrawShape(B3DebugShape shape, B3Transform transform, int color) {
            called = true;
            edgeCount = shape.GetHullEdgeCount();
            for(int i = 0; i < edgeCount; i++) {
                include(shape.GetHullEdgeVertex0(i));
                include(shape.GetHullEdgeVertex1(i));
            }
            B3Vec3 translation = transform.GetP();
            transformX = translation.GetX();
            transformY = translation.GetY();
            transformZ = translation.GetZ();
            Box3DJniSmokeTest.dispose(translation);
        }

        private void include(B3Vec3 vertex) {
            minX = Math.min(minX, vertex.GetX());
            minY = Math.min(minY, vertex.GetY());
            minZ = Math.min(minZ, vertex.GetZ());
            maxX = Math.max(maxX, vertex.GetX());
            maxY = Math.max(maxY, vertex.GetY());
            maxZ = Math.max(maxZ, vertex.GetZ());
            Box3DJniSmokeTest.dispose(vertex);
        }
    }

    private static void loadBox3D() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();

        JBox3DLoader.init(new JParserLibraryLoaderListener() {
            @Override
            public void onLoad(boolean isSuccess, Throwable t) {
                if(!isSuccess) {
                    error.set(t != null ? t : new RuntimeException("Box3D JNI loader returned false"));
                }
                latch.countDown();
            }
        });

        assertTrue("Box3D JNI loader did not finish", latch.await(10, TimeUnit.SECONDS));
        if(error.get() != null) {
            throw new AssertionError("Box3D JNI loader failed", error.get());
        }
    }

    private static void dispose(com.github.xpenatan.jParser.api.NativeObject... objects) {
        for(com.github.xpenatan.jParser.api.NativeObject object : objects) {
            if(object != null && object.native_hasOwnership() && !object.isDisposed()) {
                object.dispose();
            }
        }
    }
}
