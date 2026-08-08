package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default mesh selection and creation flags from Mesh/Viewer. */
final class MeshViewerSample extends AbstractBox3DSample {
    private final B3Mesh mesh;

    MeshViewerSample() {
        mesh = B3Mesh.CreateFromObj(SampleAssets.readUtf8("data/meshes/voxel_mesh_01.obj"),
                0.01f, true, true, true, true, 0.0015f);
        if(!mesh.IsValid()) {
            throw new IllegalStateException("Box3D could not create data/meshes/voxel_mesh_01.obj");
        }
        B3Body body = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(body.CreateMeshShape(shapeDef, mesh, one), body, one, shapeDef);
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }
}
