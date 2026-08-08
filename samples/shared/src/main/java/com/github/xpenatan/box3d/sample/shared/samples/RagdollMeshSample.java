package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene from Ragdoll/Mesh. */
final class RagdollMeshSample extends AbstractBox3DSample {
    private B3Mesh groundMesh;

    RagdollMeshSample() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        groundMesh = B3Mesh.CreateGrid(20, 20, 2.0f, 2, true);
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(shapeDef, groundMesh, scale), scale);
        addBoxShape(ground, 20.0f, 5.0f, 0.1f, 0.0f, 5.0f, -20.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 20.0f, 5.0f, 0.1f, 0.0f, 5.0f, 20.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 0.1f, 5.0f, 20.0f, -20.0f, 5.0f, 0.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(ground, 0.1f, 5.0f, 20.0f, 20.0f, 5.0f, 0.0f, null,
                0.0f, 0.6f, 0.0f, 0.0f);

        ExactHuman human = ExactHuman.create(world(), 0.0f, 1.0f, 0.0f,
                5.0f, 2.0f, 0.7f, 1, false);
        human.createParallelAnchors();
        dispose(shapeDef, ground);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(groundMesh);
    }
}
