package com.github.xpenatan.box3d;

import com.github.xpenatan.jParser.loader.JParserLibraryLoaderListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Box3DJniSmokeTest {

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
