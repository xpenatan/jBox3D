package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Filter;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact CreateMeshDrop scene from shared/stability.c. */
final class DeterminismMeshDropSample extends AbstractBox3DSample {
    private B3Mesh mesh;

    DeterminismMeshDropSample() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        mesh = B3Mesh.CreateWave(40, 40, 1.0f, 0.5f, 0.1f, 0.2f);
        B3ShapeDef groundShapeDef = new B3ShapeDef();
        B3Filter groundFilter = new B3Filter();
        groundFilter.SetCategoryBits(1L);
        groundShapeDef.SetFilter(groundFilter);
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(groundShapeDef, mesh, scale), scale, groundFilter, groundShapeDef, ground);

        SampleRandom random = new SampleRandom((int)3963634789L);
        B3Hull box = B3Hull.CreateBox(0.02f, 0.2f, 0.04f);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetRollingResistance(0.1f);
        shapeDef.SetBaseMaterial(material);
        B3Filter filter = new B3Filter();
        filter.SetCategoryBits(2L);
        filter.SetMaskBits(1L);
        shapeDef.SetFilter(filter);
        B3Vec3 position = new B3Vec3();
        B3Vec3 linearVelocity = new B3Vec3();
        B3Vec3 angularVelocity = new B3Vec3();
        for(int i = 0; i < 20; i++) {
            for(int j = 0; j < 20; j++) {
                linearVelocity.Set(random.nextFloat(-1.0f, 1.0f), random.nextFloat(-1.0f, 1.0f),
                        random.nextFloat(-1.0f, 1.0f));
                angularVelocity.Set(random.nextFloat(-5.0f, 5.0f), random.nextFloat(-5.0f, 5.0f),
                        random.nextFloat(-5.0f, 5.0f));
                position.Set(0.5f * (i - 10.0f), 5.0f, 0.5f * (j - 10.0f));
                bodyDef.SetPosition(position);
                bodyDef.SetLinearVelocity(linearVelocity);
                bodyDef.SetAngularVelocity(angularVelocity);
                B3Body body = world().CreateBody(bodyDef);
                dispose(body.CreateHullShape(shapeDef, box), body);
            }
        }
        dispose(angularVelocity, linearVelocity, position, filter, material, shapeDef, bodyDef, box);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }
}
