package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact release scene from Ragdoll/Pile. */
final class RagdollPileSample extends AbstractBox3DSample {
    private static final int COUNT = 20;
    private B3Mesh groundMesh;

    RagdollPileSample() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        groundMesh = B3Mesh.CreateGrid(20, 20, 1.0f, 1, true);
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(shapeDef, groundMesh, scale), scale, shapeDef, ground);

        SampleRandom random = new SampleRandom(42);
        float extent = 0.1f * COUNT;
        for(int i = 0; i < COUNT; i++) {
            float x = random.nextFloat(-extent, extent);
            random.nextFloat(-extent, extent);
            float z = random.nextFloat(-extent, extent);
            ExactHuman.create(world(), x, 2.0f, z, 10.0f, 0.5f, 0.7f, i, false);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(groundMesh);
    }
}
