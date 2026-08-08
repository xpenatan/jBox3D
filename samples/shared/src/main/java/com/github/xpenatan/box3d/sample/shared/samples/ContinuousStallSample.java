package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class ContinuousStallSample extends AbstractBox3DSample {
    private final float savedThreshold;
    private B3Mesh mesh;

    ContinuousStallSample() {
        addGroundBox(500.0f);
        mesh = B3Mesh.CreateTorus(200, 200, 2.0f, 1.0f);
        B3Body torusBody = createBody(B3.StaticBody(), 0.0f, 2.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(torusBody.CreateMeshShape(shapeDef, mesh, scale));

        savedThreshold = B3.GetStallThreshold();
        B3.SetStallThreshold(0.001f);

        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, 1.0f, -10.0f, null);
        bodyDef.SetIsBullet(true);
        B3Vec3 linearVelocity = new B3Vec3(0.0f, 0.0f, 600.0f);
        B3Vec3 angularVelocity = new B3Vec3(0.0f, 0.0f, 20.0f);
        bodyDef.SetLinearVelocity(linearVelocity);
        bodyDef.SetAngularVelocity(angularVelocity);
        B3Body bullet = world().CreateBody(bodyDef);
        B3Hull rock = B3Hull.CreateRock(0.25f);
        dispose(bullet.CreateHullShape(shapeDef, rock));
        dispose(rock, angularVelocity, linearVelocity, bodyDef, scale, shapeDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
        B3.SetStallThreshold(savedThreshold);
    }
}
