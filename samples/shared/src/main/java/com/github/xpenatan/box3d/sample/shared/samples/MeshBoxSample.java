package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene from Mesh/Box. */
final class MeshBoxSample extends AbstractBox3DSample {
    private final B3Mesh boxMesh;

    MeshBoxSample() {
        addGroundBox(20.0f);
        B3Quat rotation = rotationY(0.25f * (float)Math.PI);
        B3Body meshBody = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, rotation);
        B3Vec3 center = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Vec3 extents = new B3Vec3(1.0f, 1.0f, 1.0f);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        boxMesh = B3Mesh.CreateBox(center, extents, true);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(meshBody.CreateMeshShape(shapeDef, boxMesh, one), meshBody, shapeDef);
        addDynamicBox(0.0f, 1.5f, 0.0f, 0.5f, 0.5f, 0.5f);
        dispose(one, extents, center, rotation);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(boxMesh);
    }
}
