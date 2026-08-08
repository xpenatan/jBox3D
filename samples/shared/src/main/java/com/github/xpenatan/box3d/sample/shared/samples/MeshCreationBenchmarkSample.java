package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Mesh;

/** Exact empty scene and four-mesh creation workload from Mesh/Creation Benchmark. */
final class MeshCreationBenchmarkSample extends AbstractBox3DSample {
    private final String[] meshes = {
            SampleAssets.readUtf8("data/meshes/voxel_mesh_01.obj"),
            SampleAssets.readUtf8("data/meshes/voxel_mesh_02.obj"),
            SampleAssets.readUtf8("data/meshes/voxel_mesh_03.obj"),
            SampleAssets.readUtf8("data/meshes/voxel_mesh_04.obj")
    };

    @Override
    public void step(float deltaSeconds) {
        for(int iteration = 0; iteration < 10; iteration++) {
            for(String obj : meshes) {
                B3Mesh mesh = B3Mesh.CreateFromObj(obj, 0.01f, true, true, false, true, 0.0015f);
                dispose(mesh);
            }
        }
    }
}
