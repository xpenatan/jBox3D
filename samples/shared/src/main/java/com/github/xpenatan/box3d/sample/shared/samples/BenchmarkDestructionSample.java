package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3ExplosionDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact release-build port of {@code BenchmarkDestruction}. */
final class BenchmarkDestructionSample extends AbstractBox3DSample {
    private static final int GRID_COUNT = 20;
    private static final float EXTENT = 2.5f;
    private static final int BODY_CAPACITY = GRID_COUNT * GRID_COUNT * GRID_COUNT;

    private final SampleRandom random = new SampleRandom();
    private final long[] bodyIds = new long[BODY_CAPACITY];
    private final B3ExplosionDef explosionDef;
    private B3Mesh gridMesh;
    private int bodyCount;
    private int stepCount;

    BenchmarkDestructionSample() {
        super(0, BODY_CAPACITY, 0, BODY_CAPACITY, 50_000);
        B3ShapeDef shapeDef = new B3ShapeDef();
        gridMesh = B3Mesh.CreateGrid(40, 40, 1.0f, 0, true);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3Vec3 unitScale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(shapeDef, gridMesh, unitScale), ground, unitScale, shapeDef);

        explosionDef = new B3ExplosionDef();
        explosionDef.SetRadius(EXTENT);
        explosionDef.SetFalloff(0.5f * EXTENT);
        B3Vec3 explosionPosition = new B3Vec3(0.0f, 2.0f * EXTENT, 0.0f);
        explosionDef.SetPosition(explosionPosition);
        explosionDef.SetImpulsePerArea(1000.0f);
        dispose(explosionPosition);
        spawn();
    }

    private void destroyBodies() {
        for(int i = 0; i < bodyCount; ++i) {
            B3Body body = new B3Body(bodyIds[i]);
            if(body.IsValid()) {
                body.Destroy();
            }
            dispose(body);
            bodyIds[i] = 0L;
        }
    }

    private void spawn() {
        float a = EXTENT / GRID_COUNT;
        B3Hull box = B3Hull.CreateBox(0.8f * a, 0.8f * a, 0.8f * a);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 position = new B3Vec3();
        bodyCount = 0;
        for(int i = 0; i < GRID_COUNT; ++i) {
            for(int j = 0; j < GRID_COUNT; ++j) {
                for(int k = 0; k < GRID_COUNT; ++k) {
                    if(random.nextInt(1, 2) == 1) {
                        continue;
                    }
                    position.Set((2.0f * i - GRID_COUNT + 1.0f) * a, (2.0f * j + 1.0f) * a,
                            (2.0f * k - GRID_COUNT + 1.0f) * a);
                    bodyDef.SetPosition(position);
                    B3Body body = world().CreateBody(bodyDef);
                    bodyIds[bodyCount++] = body.GetId();
                    dispose(body.CreateHullShape(shapeDef, box), body);
                }
            }
        }
        world().Explode(explosionDef);
        dispose(position, shapeDef, bodyDef, box);
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        stepCount += 1;
        if(stepCount % 140 == 0) {
            destroyBodies();
            spawn();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(explosionDef, gridMesh);
    }
}
