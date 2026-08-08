package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact large-coordinate regression scene from Mesh/Voxel. */
final class MeshVoxelSample extends AbstractBox3DSample {
    private final B3Mesh mesh;

    MeshVoxelSample() {
        mesh = B3Mesh.CreateFromObj(SampleAssets.readUtf8("data/meshes/collision_mesh_01.obj"),
                0.01f, true, true, true, true, 0.002f);
        if(!mesh.IsValid()) {
            throw new IllegalStateException("Box3D could not create data/meshes/collision_mesh_01.obj");
        }
        B3Body ground = createBody(B3.StaticBody(), 5000.0f, 3500.0f, -7000.0f, null);
        B3ShapeDef groundDef = new B3ShapeDef();
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(groundDef, mesh, one), ground, groundDef, one);

        float[][] source = {
                {-3.13548756f, 3.81141949f, 237.289047f}, {-16.2333279f, -23.4977913f, 235.486603f},
                {-13.8834839f, 6.20244455f, 23.7760544f}, {14.0794125f, 4.63170528f, 24.9530792f},
                {3.98322797f, -16.4192238f, 236.704071f}, {-23.3520412f, -3.26714420f, 236.071594f},
                {13.4517860f, -6.94963741f, 24.4085312f}, {-5.24953651f, 13.9316301f, 24.5058060f},
                {-4.65071201f, -24.1484108f, 235.974121f}, {-14.5111103f, -5.37889385f, 23.2315063f},
                {6.33307076f, 13.2810068f, 24.9935150f}, {4.81784487f, -14.6788225f, 23.6787796f},
                {-14.7180958f, 4.46204281f, 236.801331f}, {-23.9796677f, -14.8484812f, 235.527039f},
                {4.61085415f, -4.83788204f, 237.248611f}, {-6.76476669f, -14.0281992f, 23.1910706f}
        };
        B3Vec3Array points = new B3Vec3Array(source.length);
        for(int i = 0; i < source.length; i++) {
            B3Vec3 point = new B3Vec3(0.01f * source[i][1], 0.01f * source[i][2], 0.01f * source[i][0]);
            points.SetValue(i, point);
            dispose(point);
        }
        B3Hull hull = B3Hull.CreateFromPoints(points, 16);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3Vec3 position = new B3Vec3(5020.27734f, 3506.22559f, -6986.48584f);
        B3Quat rotation = new B3Quat(0.664546967f, 0.669287264f, 0.135021493f, 0.303646326f);
        bodyDef.SetPosition(position);
        bodyDef.SetRotation(rotation);
        B3Body body = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetRollingResistance(0.1f);
        shapeDef.SetBaseMaterial(material);
        dispose(body.CreateHullShape(shapeDef, hull), body, material, shapeDef, bodyDef,
                rotation, position, hull, points);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }
}
