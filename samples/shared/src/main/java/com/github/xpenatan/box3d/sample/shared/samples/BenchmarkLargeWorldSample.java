package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact release-build port of the Static Floor/Large World benchmark. */
final class BenchmarkLargeWorldSample extends AbstractBox3DSample {
    private static final float CELL_SIZE = 10.0f;
    private static final int GRID_COUNT = 1000;
    private static final int SPHERE_COUNT = 100;
    private static final int DROP_INTERVAL = 5;

    private int spheresDropped;
    private int stepCount;

    BenchmarkLargeWorldSample() {
        super(GRID_COUNT * GRID_COUNT, SPHERE_COUNT, GRID_COUNT * GRID_COUNT, SPHERE_COUNT, 1024);

        float halfSpan = 0.5f * CELL_SIZE * GRID_COUNT;
        B3Hull box = B3Hull.CreateBox(0.5f * CELL_SIZE, 0.25f, 0.5f * CELL_SIZE);
        B3BodyDef bodyDef = new B3BodyDef();
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetInvokeContactCreation(true);
        B3Vec3 position = new B3Vec3();
        for(int i = 0; i < GRID_COUNT; ++i) {
            float x = -halfSpan + (i + 0.5f) * CELL_SIZE;
            for(int j = 0; j < GRID_COUNT; ++j) {
                float z = -halfSpan + (j + 0.5f) * CELL_SIZE;
                position.Set(x, 0.0f, z);
                bodyDef.SetPosition(position);
                B3Body body = world().CreateBody(bodyDef);
                dispose(body.CreateHullShape(shapeDef, box), body);
            }
        }
        dispose(position, shapeDef, bodyDef, box);
    }

    @Override
    public void step(float deltaSeconds) {
        stepLargeWorld(stepCount);
        super.step(deltaSeconds);
        stepCount += 1;
    }

    private void stepLargeWorld(int currentStep) {
        if(spheresDropped >= SPHERE_COUNT || currentStep == 0 || currentStep % DROP_INTERVAL != 0) {
            return;
        }
        int side = 1;
        while(side * side < SPHERE_COUNT) {
            side += 1;
        }
        int gridX = spheresDropped % side;
        int gridZ = spheresDropped / side;
        float halfSpan = 0.5f * CELL_SIZE * GRID_COUNT;
        float inset = 0.2f * halfSpan;
        float usable = 2.0f * halfSpan - 2.0f * inset;
        float x = -halfSpan + inset + (gridX + 0.5f) * (usable / side);
        float z = -halfSpan + inset + (gridZ + 0.5f) * (usable / side);

        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3Vec3 position = new B3Vec3(x, 1.5f, z);
        bodyDef.SetPosition(position);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.5f);
        B3Body body = world().CreateBody(bodyDef);
        dispose(body.CreateSphereShape(shapeDef, sphere), body, sphere, center, shapeDef, position, bodyDef);
        spheresDropped += 1;
    }
}
