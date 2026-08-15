package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Four-mesh creation workload from Mesh/Creation Benchmark, with a rendered result preview. */
final class MeshCreationBenchmarkSample extends AbstractBox3DSample {
    private final String[] meshes = {
            SampleAssets.readUtf8("data/meshes/voxel_mesh_01.obj"),
            SampleAssets.readUtf8("data/meshes/voxel_mesh_02.obj"),
            SampleAssets.readUtf8("data/meshes/voxel_mesh_03.obj"),
            SampleAssets.readUtf8("data/meshes/voxel_mesh_04.obj")
    };
    private final B3Mesh previewMesh;

    MeshCreationBenchmarkSample() {
        previewMesh = B3Mesh.CreateFromObj(meshes[0], 0.01f, true, true, false, true, 0.0015f);
        if(!previewMesh.IsValid()) {
            throw new IllegalStateException("Box3D could not create the mesh benchmark preview");
        }
        B3Body previewBody = createBody(B3.StaticBody(), -16.0f, 0.0f, -8.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(previewBody.CreateMeshShape(shapeDef, previewMesh, scale), scale, shapeDef, previewBody);
    }

    @Override
    public void step(float deltaSeconds) {
        for(int iteration = 0; iteration < 10; iteration++) {
            for(String obj : meshes) {
                B3Mesh mesh = B3Mesh.CreateFromObj(obj, 0.01f, true, true, false, true, 0.0015f);
                dispose(mesh);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(previewMesh);
    }
}
