package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene from {@code BenchmarkExplosion}; C does not explode automatically. */
final class BenchmarkExplosionSample extends AbstractBox3DSample {
    private B3Mesh gridMesh;
    private B3Hull cylinder;

    BenchmarkExplosionSample() {
        B3ShapeDef shapeDef = new B3ShapeDef();
        gridMesh = B3Mesh.CreateGrid(40, 40, 1.0f, 0, true);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3Vec3 unitScale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(shapeDef, gridMesh, unitScale));
        addBoxShape(ground, 20.0f, 1.0f, 0.1f, 0.0f, 1.0f, -20.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 20.0f, 1.0f, 0.1f, 0.0f, 1.0f, 20.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 0.1f, 1.0f, 20.0f, -20.0f, 1.0f, 0.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 0.1f, 1.0f, 20.0f, 20.0f, 1.0f, 0.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        dispose(ground, unitScale);

        cylinder = B3Hull.CreateCylinder(0.5f, 0.2f, 0.0f, 15);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        shapeDef.SetExplosionScale(2.0f);
        B3Vec3 position = new B3Vec3();
        for(int i = -16; i <= 16; ++i) {
            for(int k = -16; k <= 16; ++k) {
                position.Set(i, 0.0f, k);
                bodyDef.SetPosition(position);
                B3Body body = world().CreateBody(bodyDef);
                dispose(body.CreateHullShape(shapeDef, cylinder), body);
            }
        }
        dispose(position, bodyDef, shapeDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(cylinder, gridMesh);
    }
}
