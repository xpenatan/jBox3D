package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact CreateFallingRagdolls scene from shared/determinism.c. */
final class DeterminismFallingRagdollsSample extends AbstractBox3DSample {
    private static final int GRID_COUNT = 2;
    private static final int GROUP_SIZE = 2;
    private static final float GRID_SIZE = 15.0f;

    private B3Mesh gridMesh;
    private B3Mesh torusMesh;

    DeterminismFallingRagdollsSample() {
        int halfMeshGridRows = 4;
        float meshGridCellWidth = GRID_SIZE / (2.0f * halfMeshGridRows);
        gridMesh = B3Mesh.CreateGrid(2 * halfMeshGridRows, 2 * halfMeshGridRows, meshGridCellWidth, 0, true);
        torusMesh = B3Mesh.CreateTorus(16, 16, 0.25f * GRID_SIZE, 1.0f);

        float span = GRID_SIZE * GRID_COUNT;
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        float bodyX = -0.5f * span + 0.5f * GRID_SIZE;
        for(int rowIndex = 0; rowIndex < GRID_COUNT; rowIndex++) {
            float bodyZ = -0.5f * span + 0.5f * GRID_SIZE;
            for(int columnIndex = 0; columnIndex < GRID_COUNT; columnIndex++) {
                B3Body body = createBody(B3.StaticBody(), bodyX, 0.0f, bodyZ, null);
                dispose(body.CreateMeshShape(shapeDef, gridMesh, scale));
                dispose(body.CreateMeshShape(shapeDef, torusMesh, scale), body);

                int groupIndex = rowIndex * GRID_COUNT + columnIndex;
                float groupDistance = span / GRID_COUNT;
                float humanX = -0.5f * span + groupDistance * (columnIndex + 0.5f);
                float humanZ = -0.5f * span + groupDistance * (rowIndex + 0.5f);
                for(int humanIndex = 0; humanIndex < GROUP_SIZE; humanIndex++) {
                    ExactHuman.create(world(), humanX, 15.0f, humanZ,
                            5.0f, 1.0f, 0.7f, groupIndex, false);
                    humanX += 0.75f;
                }

                bodyZ += GRID_SIZE;
            }
            bodyX += GRID_SIZE;
        }
        dispose(scale, shapeDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(torusMesh, gridMesh);
    }
}
