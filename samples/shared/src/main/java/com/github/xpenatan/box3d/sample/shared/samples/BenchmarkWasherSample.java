package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact release-build port of {@code CreateWasher} in {@code shared/benchmarks.c}. */
final class BenchmarkWasherSample extends AbstractBox3DSample {
    BenchmarkWasherSample() {
        super(16, 10_000, 16, 10_000, 60_000);
        addGroundBox(60.0f);

        B3BodyDef washerDef = new B3BodyDef();
        washerDef.SetType(B3.KinematicBody());
        B3Vec3 washerPosition = new B3Vec3(0.0f, 21.0f, 0.0f);
        B3Vec3 angularVelocity = new B3Vec3(0.0f, 0.0f, (float)Math.PI / 180.0f * 25.0f);
        B3Vec3 linearVelocity = new B3Vec3(0.001f, -0.002f, 0.0f);
        washerDef.SetPosition(washerPosition);
        washerDef.SetAngularVelocity(angularVelocity);
        washerDef.SetLinearVelocity(linearVelocity);
        B3Body washer = world().CreateBody(washerDef);
        B3ShapeDef shapeDef = new B3ShapeDef();

        float r0 = 14.0f;
        float r1 = 16.0f;
        float r2 = 18.0f;
        float angle = (float)Math.PI / 18.0f;
        B3Quat rotation = rotationZ(angle);
        B3Quat overlapRotation = rotationZ(0.1f * angle);
        B3Quat inverseOverlapRotation = rotationZ(-0.1f * angle);
        B3Vec3 direction1 = new B3Vec3(1.0f, 0.0f, 0.0f);
        for(int i = 0; i < 36; ++i) {
            B3Vec3 direction2 = i == 35
                    ? new B3Vec3(1.0f, 0.0f, 0.0f)
                    : rotatedVector(rotation, direction1);
            B3Vec3 overlapDirection1 = rotatedVector(inverseOverlapRotation, direction1);
            B3Vec3 overlapDirection2 = rotatedVector(overlapRotation, direction2);
            createRingSegment(washer, shapeDef, r1, r2, overlapDirection1, overlapDirection2);
            if(i % 9 == 0) {
                createRingSegment(washer, shapeDef, r0, r1, direction1, direction2);
            }
            dispose(overlapDirection2, overlapDirection1, direction1);
            direction1 = direction2;
        }
        dispose(direction1, inverseOverlapRotation, overlapRotation, rotation);
        dispose(washer, shapeDef, linearVelocity, angularVelocity, washerPosition, washerDef);

        int gridCount = 20;
        float a = 0.2f;
        B3Hull cube = B3Hull.CreateBox(a, a, a);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef cubeDef = new B3ShapeDef();
        B3Vec3 position = new B3Vec3();
        float x = -2.0f * a * gridCount;
        for(int i = 0; i < gridCount; ++i) {
            float y = -2.0f * a * gridCount + 21.0f;
            for(int j = 0; j < gridCount; ++j) {
                float z = -2.0f * a * gridCount;
                for(int k = 0; k < gridCount; ++k) {
                    position.Set(x, y, z);
                    bodyDef.SetPosition(position);
                    B3Body body = world().CreateBody(bodyDef);
                    dispose(body.CreateHullShape(cubeDef, cube), body);
                    z += 4.0f * a;
                }
                y += 4.0f * a;
            }
            x += 4.0f * a;
        }
        dispose(position, cubeDef, bodyDef, cube);
    }

    private void createRingSegment(B3Body washer, B3ShapeDef shapeDef, float innerRadius, float outerRadius,
            B3Vec3 direction1, B3Vec3 direction2) {
        float x1 = direction1.GetX();
        float y1 = direction1.GetY();
        float x2 = direction2.GetX();
        float y2 = direction2.GetY();
        float[][] points = {
                { innerRadius * x1, innerRadius * y1, -10.0f },
                { outerRadius * x1, outerRadius * y1, -10.0f },
                { innerRadius * x2, innerRadius * y2, -10.0f },
                { outerRadius * x2, outerRadius * y2, -10.0f },
                { innerRadius * x1, innerRadius * y1, 10.0f },
                { outerRadius * x1, outerRadius * y1, 10.0f },
                { innerRadius * x2, innerRadius * y2, 10.0f },
                { outerRadius * x2, outerRadius * y2, 10.0f }
        };
        B3Hull hull = createHull(points);
        dispose(washer.CreateHullShape(shapeDef, hull), hull);
    }
}
