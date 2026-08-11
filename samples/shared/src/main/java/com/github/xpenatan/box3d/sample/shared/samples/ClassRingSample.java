package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleSettings;

final class ClassRingSample extends AbstractBox3DSample {
    private static final float TIME_STEP = 1.0f / 960.0f;
    private static final int SUB_STEP_COUNT = 8;
    private static final int STEP_MULTIPLIER = 16;

    ClassRingSample() {
        addGroundBox(100.0f);

        int count = 24;
        float radius = 1.0f;
        float tubeRadius = 0.1f * radius;
        float axisRadius = radius - tubeRadius;
        float tilt = 13.0f * (float)Math.PI / 180.0f;
        B3Quat rotation = rotationX(tilt);
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, radius, 0.0f, rotation);
        bodyDef.SetAllowFastRotation(true);
        bodyDef.SetEnableContactRecycling(false);
        B3Body ring = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetDensity(1.0f);

        B3Vec3[] vertices = new B3Vec3[count];
        float deltaAngle = 2.0f * (float)Math.PI / count;
        float cosine = (float)Math.cos(deltaAngle);
        float sine = (float)Math.sin(deltaAngle);
        float x = axisRadius;
        float y = 0.0f;
        for(int i = 0; i < count; i++) {
            vertices[i] = new B3Vec3(x, y, 0.0f);
            float nextX = cosine * x - sine * y;
            float nextY = sine * x + cosine * y;
            x = nextX;
            y = nextY;
        }
        for(int i = 0; i < count; i++) {
            B3Capsule capsule = new B3Capsule(vertices[i], vertices[(i + 1) % count], tubeRadius);
            dispose(ring.CreateCapsuleShape(shapeDef, capsule), capsule);
        }

        shapeDef.SetDensity(2.0f);
        B3Vec3 gemCenter = new B3Vec3(0.0f, -0.65f * radius, 0.0f);
        B3Sphere gem = new B3Sphere(gemCenter, 0.3f);
        dispose(ring.CreateSphereShape(shapeDef, gem));
        B3Vec3 angularVelocity = new B3Vec3(0.0f, 100.0f * (float)Math.cos(tilt),
                100.0f * (float)Math.sin(tilt));
        ring.SetAngularVelocity(angularVelocity);

        for(B3Vec3 vertex : vertices) {
            dispose(vertex);
        }
        dispose(angularVelocity, gem, gemCenter, shapeDef, bodyDef, rotation);
    }

    @Override
    public void step(float deltaSeconds) {
        for(int i = 0; i < STEP_MULTIPLIER; i++) {
            world().Step(TIME_STEP, SUB_STEP_COUNT);
        }
    }

    @Override
    public void step(float deltaSeconds, Box3DSampleSettings settings) {
        for(int i = 0; i < STEP_MULTIPLIER - 1; i++) {
            world().Step(TIME_STEP, SUB_STEP_COUNT);
        }
        world().EnableSleeping(settings.sleepEnabled());
        world().EnableWarmStarting(settings.warmStartingEnabled());
        world().EnableContinuous(settings.continuousEnabled());
        world().SetContactRecycleDistance(settings.recycleDistance());
        world().Step(TIME_STEP, SUB_STEP_COUNT);
    }
}
