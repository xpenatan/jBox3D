package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Filter;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

final class ContinuousMeshDropSample extends AbstractBox3DSample {
    private static final int GRID_COUNT = 32;

    private B3Mesh groundMesh;

    ContinuousMeshDropSample() {
        createGround();
        generateBoxes();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(groundMesh);
    }

    private void createGround() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        groundMesh = B3Mesh.CreateWave(40, 40, 1.0f, 0.5f, 0.1f, 0.2f);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Filter filter = new B3Filter();
        filter.SetCategoryBits(1L);
        shapeDef.SetFilter(filter);
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(shapeDef, groundMesh, scale));

        float extent = 20.0f;
        float halfHeight = 1.0f;
        addWall(ground, shapeDef, 0.0f, halfHeight, -extent, extent, halfHeight, 0.1f);
        addWall(ground, shapeDef, 0.0f, halfHeight, extent, extent, halfHeight, 0.1f);
        addWall(ground, shapeDef, -extent, halfHeight, 0.0f, 0.1f, halfHeight, extent);
        addWall(ground, shapeDef, extent, halfHeight, 0.0f, 0.1f, halfHeight, extent);
        dispose(scale, filter, shapeDef);
    }

    private void generateBoxes() {
        SampleRandom random = new SampleRandom((int)System.nanoTime());
        B3Hull box = B3Hull.CreateBox(0.02f, 0.2f, 0.04f);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.1f);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3Vec3 position = new B3Vec3();
        B3Vec3 linearVelocity = new B3Vec3();
        B3Vec3 angularVelocity = new B3Vec3();
        for(int i = 0; i < GRID_COUNT; i++) {
            for(int j = 0; j < GRID_COUNT; j++) {
                linearVelocity.Set(random.nextFloat(-1.0f, 1.0f), random.nextFloat(-1.0f, 1.0f),
                        random.nextFloat(-1.0f, 1.0f));
                angularVelocity.Set(random.nextFloat(-5.0f, 5.0f), random.nextFloat(-5.0f, 5.0f),
                        random.nextFloat(-5.0f, 5.0f));
                position.Set(0.5f * (i - 0.5f * GRID_COUNT), 5.0f,
                        0.5f * (j - 0.5f * GRID_COUNT));
                bodyDef.SetPosition(position);
                bodyDef.SetLinearVelocity(linearVelocity);
                bodyDef.SetAngularVelocity(angularVelocity);
                B3Body body = world().CreateBody(bodyDef);
                dispose(body.CreateHullShape(shapeDef, box), body);
            }
        }
        dispose(angularVelocity, linearVelocity, position, bodyDef, shapeDef, box);
    }

    private static void addWall(B3Body body, B3ShapeDef shapeDef, float x, float y, float z, float halfWidth,
            float halfHeight, float halfDepth) {
        B3Vec3 position = new B3Vec3(x, y, z);
        B3Quat rotation = new B3Quat();
        B3Transform transform = new B3Transform(position, rotation);
        B3Hull wall = B3Hull.CreateTransformedBox(halfWidth, halfHeight, halfDepth, transform);
        dispose(body.CreateHullShape(shapeDef, wall), wall, transform, rotation, position);
    }
}
