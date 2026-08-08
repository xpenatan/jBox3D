package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact release-build port of the Junkyard benchmark in {@code shared/benchmarks.c}. */
final class BenchmarkJunkyardSample extends AbstractBox3DSample {
    private static final float TIME_STEP = 1.0f / 60.0f;
    private static final float RADIUS = 35.0f;

    private long pusherId;
    private float degrees;

    BenchmarkJunkyardSample() {
        super(16, 20 * 20 * 24 + 1, 16, 20 * 20 * 24 + 1, 250 * 1024);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        addBoxShape(ground, 120.0f, 1.0f, 120.0f, 0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 1.0f, 8.0f, 50.0f, -50.0f, 8.0f, 0.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 1.0f, 8.0f, 50.0f, 50.0f, 8.0f, 0.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 50.0f, 8.0f, 1.0f, 0.0f, 8.0f, -50.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 50.0f, 8.0f, 1.0f, 0.0f, 8.0f, 50.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        dispose(ground);

        B3Hull rock = B3Hull.CreateRock(1.5f);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 position = new B3Vec3();
        for(int y = 0; y < 24; ++y) {
            for(int x = 0; x <= 20; ++x) {
                for(int z = 0; z <= 20; ++z) {
                    position.Set(-40.0f + 4.0f * x, 4.0f * y + 25.0f, -40.0f + 4.0f * z);
                    bodyDef.SetPosition(position);
                    B3Body body = world().CreateBody(bodyDef);
                    dispose(body.CreateHullShape(shapeDef, rock), body);
                }
            }
        }
        dispose(rock);

        B3Hull pusherHull = B3Hull.CreateCylinder(24.0f, 4.0f, 0.0f, 16);
        bodyDef.SetType(B3.KinematicBody());
        position.Set(RADIUS, 0.0f, 0.0f);
        bodyDef.SetPosition(position);
        B3Body pusher = world().CreateBody(bodyDef);
        pusherId = pusher.GetId();
        dispose(pusher.CreateHullShape(shapeDef, pusherHull), pusher, pusherHull,
                position, shapeDef, bodyDef);
    }

    @Override
    public void step(float deltaSeconds) {
        degrees += -6.0f * TIME_STEP;
        float radians = degrees * (float)Math.PI / 180.0f;
        B3Vec3 targetPosition = new B3Vec3(RADIUS * (float)Math.cos(radians), 0.0f,
                RADIUS * (float)Math.sin(radians));
        B3Quat targetRotation = new B3Quat();
        B3Body pusher = new B3Body(pusherId);
        pusher.SetTargetTransform(targetPosition, targetRotation, TIME_STEP, false);
        dispose(pusher, targetRotation, targetPosition);
        super.step(deltaSeconds);
    }
}
