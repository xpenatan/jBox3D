package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact release-build port of the Rain benchmark in {@code shared/benchmarks.c}. */
final class BenchmarkRainSample extends AbstractBox3DSample {
    private static final int GRID_COUNT = 10;
    private static final int GROUP_SIZE = 3;
    private static final float GRID_SIZE = 15.0f;

    private final ExactHuman[][] groups = new ExactHuman[GRID_COUNT * GRID_COUNT][GROUP_SIZE];
    private B3Mesh gridMesh;
    private B3Mesh torusMesh;
    private int columnCount;
    private int columnIndex;
    private int stepCount;

    BenchmarkRainSample() {
        int halfMeshGridRows = 4;
        float meshGridCellWidth = GRID_SIZE / (2.0f * halfMeshGridRows);
        gridMesh = B3Mesh.CreateGrid(2 * halfMeshGridRows, 2 * halfMeshGridRows, meshGridCellWidth, 1, true);
        torusMesh = B3Mesh.CreateTorus(16, 16, 0.25f * GRID_SIZE, 1.0f);

        float span = GRID_SIZE * GRID_COUNT;
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        float x = -0.5f * span + 0.5f * GRID_SIZE;
        for(int i = 0; i < GRID_COUNT; ++i) {
            float z = -0.5f * span + 0.5f * GRID_SIZE;
            for(int j = 0; j < GRID_COUNT; ++j) {
                B3Body body = createBody(B3.StaticBody(), x, 0.0f, z, null);
                dispose(body.CreateMeshShape(shapeDef, gridMesh, scale));
                dispose(body.CreateMeshShape(shapeDef, torusMesh, scale), body);
                z += GRID_SIZE;
            }
            x += GRID_SIZE;
        }
        dispose(scale, shapeDef);
    }

    @Override
    public void step(float deltaSeconds) {
        stepRain(stepCount);
        super.step(deltaSeconds);
        stepCount += 1;
    }

    private void stepRain(int currentStep) {
        int delay = 0x2F;
        if((currentStep & delay) != 0) {
            return;
        }
        if(columnCount < GRID_COUNT) {
            for(int row = 0; row < GRID_COUNT; ++row) {
                createGroup(row, columnCount);
            }
            columnCount = Math.min(columnCount + 1, GRID_COUNT);
        }
        else {
            for(int row = 0; row < GRID_COUNT; ++row) {
                destroyGroup(row, columnIndex);
                createGroup(row, columnIndex);
            }
            columnIndex += 1;
            if(columnIndex >= GRID_COUNT) {
                columnIndex = 0;
            }
        }
    }

    private void createGroup(int rowIndex, int groupColumn) {
        int groupIndex = rowIndex * GRID_COUNT + groupColumn;
        float span = GRID_COUNT * GRID_SIZE;
        float groupDistance = span / GRID_COUNT;
        float x = -0.5f * span + groupDistance * (groupColumn + 0.5f);
        float z = -0.5f * span + groupDistance * (rowIndex + 0.5f);
        for(int i = 0; i < GROUP_SIZE; ++i) {
            groups[groupIndex][i] = ExactHuman.create(world(), x, 20.0f, z, 5.0f, 1.0f, 0.7f,
                    groupIndex, false);
            x += 0.75f;
        }
    }

    private void destroyGroup(int rowIndex, int groupColumn) {
        int groupIndex = rowIndex * GRID_COUNT + groupColumn;
        for(int i = 0; i < GROUP_SIZE; ++i) {
            ExactHuman human = groups[groupIndex][i];
            if(human != null) {
                human.destroy();
                groups[groupIndex][i] = null;
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(torusMesh, gridMesh);
    }
}
