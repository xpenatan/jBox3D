package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default ({@code CreateTrees100}) port from {@code shared/benchmarks.c}. */
final class BenchmarkFallingTreesSample extends AbstractBox3DSample {
    private B3Mesh mesh;

    BenchmarkFallingTreesSample() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        mesh = B3Mesh.CreateWave(150, 200, 1.0f, 0.4f, 0.05f, 0.1f);
        B3ShapeDef groundShapeDef = new B3ShapeDef();
        B3Vec3 unitScale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(groundShapeDef, mesh, unitScale), ground, unitScale, groundShapeDef);

        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        bodyDef.SetSleepThreshold(0.2f);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetFriction(0.9f);
        material.SetRollingResistance(0.05f);
        shapeDef.SetBaseMaterial(material);
        shapeDef.SetUpdateBodyMass(false);
        shapeDef.SetDensity(1.0f);

        B3Hull[] hulls = new B3Hull[22];
        float y = 1.0f;
        float radius = 0.75f;
        float length = 1.5f;
        for(int i = 0; i < hulls.length; ++i) {
            hulls[i] = B3Hull.CreateCylinder(length + 2.0f * radius, radius, y - radius, 6);
            y += length + 2.0f * radius;
            radius *= 0.95f;
        }

        float angularSpeed = -0.5f;
        float z = -70.0f;
        B3Vec3 position = new B3Vec3();
        for(int bodyIndex = 0; bodyIndex < 50; ++bodyIndex) {
            position.Set(0.0f, 1.0f, z);
            bodyDef.SetPosition(position);
            B3Body body = world().CreateBody(bodyDef);
            for(B3Hull hull : hulls) {
                dispose(body.CreateHullShape(shapeDef, hull));
            }

            float velocityScale = 0.5f + 0.5f * bodyIndex / 50.0f;
            float omegaZ = velocityScale * angularSpeed;
            body.ApplyMassFromShapes();
            B3Vec3 center = body.GetWorldCenter();
            B3Vec3 omega = new B3Vec3(0.0f, 0.0f, omegaZ);
            B3Vec3 velocity = new B3Vec3(-omegaZ * (center.GetY() - 1.0f),
                    omegaZ * center.GetX(), 0.0f);
            body.SetAngularVelocity(omega);
            body.SetLinearVelocity(velocity);
            dispose(velocity, omega, center, body);

            z += 3.0f;
            angularSpeed = -angularSpeed;
        }

        for(B3Hull hull : hulls) {
            dispose(hull);
        }
        dispose(position, material, shapeDef, bodyDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }
}
