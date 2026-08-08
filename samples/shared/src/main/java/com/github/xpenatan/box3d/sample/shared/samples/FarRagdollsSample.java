package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene from World/Far Ragdolls. */
final class FarRagdollsSample extends AbstractBox3DSample {
    private static final float OFFSET = 1_000_000.0f;
    private B3Mesh groundMesh;

    FarRagdollsSample() {
        B3Body ground = createBody(B3.StaticBody(), OFFSET, -1.0f, 0.0f, null);
        groundMesh = B3Mesh.CreateGrid(20, 20, 1.0f, 1, true);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(shapeDef, groundMesh, scale), scale, shapeDef, ground);

        for(int i = 0; i < 20; i++) {
            float x = OFFSET + 0.15f * (i - 10.0f);
            float y = 2.0f + 0.25f * i;
            float z = 0.15f * (10.0f - i);
            ExactHuman.create(world(), x, y, z, 10.0f, 0.5f, 0.7f, i, false);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(groundMesh);
    }

    static float offset() {
        return OFFSET;
    }
}
