package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3SurfaceMaterialArray;
import com.github.xpenatan.box3d.B3Vec3;

final class ConveyorMeshSample extends AbstractBox3DSample {
    private static final int[] COLORS = {
            0x008000, 0xADFF2F, 0xF0FFF0, 0xFF69B4, 0xCD5C5C, 0x4B0082, 0xFFFFF0
    };

    private B3Mesh mesh;

    ConveyorMeshSample() {
        addGroundBox(20.0f);
        createConveyor();
        createCylinders();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }

    private void createConveyor() {
        mesh = B3Mesh.CreateFromObj(SampleAssets.readUtf8("data/meshes/conveyor.obj"),
                1.0f, false, true, true, true, 0.002f);
        if(!mesh.IsValid() || mesh.GetTriangleCount() <= 46) {
            dispose(mesh);
            mesh = null;
            throw new IllegalStateException("Box3D could not create data/meshes/conveyor.obj");
        }

        for(int i = 0; i < mesh.GetTriangleCount(); i++) {
            mesh.SetTriangleMaterialIndex(i, 0);
        }
        setMaterial(1, 0, 4);
        setMaterial(2, 9, 12);
        setMaterial(3, 21, 38);
        setMaterial(4, 43, 46);
        setMaterial(5, 30, 33);
        setMaterial(6, 18, 24);

        float[][] velocities = {
                { 0.0f, 0.0f, 0.0f },
                { 0.7f, 0.0f, -0.2f },
                { 0.6f, 0.0f, 0.4f },
                { 0.0f, 0.0f, 1.3f },
                { -0.6f, 0.0f, 0.4f },
                { -0.75f, 0.0f, -0.4f },
                { 0.0f, 0.0f, -1.3f }
        };
        B3SurfaceMaterialArray materials = new B3SurfaceMaterialArray(velocities.length);
        for(int i = 0; i < velocities.length; i++) {
            B3SurfaceMaterial material = new B3SurfaceMaterial();
            B3Vec3 tangentVelocity = new B3Vec3(2.0f * velocities[i][0], 2.0f * velocities[i][1],
                    2.0f * velocities[i][2]);
            material.SetFriction(0.8f);
            material.SetTangentVelocity(tangentVelocity);
            material.SetCustomColor(COLORS[i]);
            materials.SetValue(i, material);
            dispose(tangentVelocity, material);
        }

        B3Quat rotation = rotationY(0.5f * (float)Math.PI);
        B3BodyDef bodyDef = bodyDef(B3.StaticBody(), 0.0f, 0.5f, 6.0f, rotation);
        B3Body body = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(body.CreateMeshShapeWithMaterials(shapeDef, mesh, scale, materials));
        dispose(scale, shapeDef, bodyDef, rotation, materials);
    }

    private void setMaterial(int materialIndex, int triangle1, int triangle2) {
        mesh.SetTriangleMaterialIndex(triangle1, materialIndex);
        mesh.SetTriangleMaterialIndex(triangle2, materialIndex);
    }

    private void createCylinders() {
        B3Hull cylinder = B3Hull.CreateCylinder(0.3f, 0.15f, 0.0f, 32);
        B3ShapeDef shapeDef = new B3ShapeDef();
        for(int i = 0; i < 20; i++) {
            B3Body body = createBody(B3.DynamicBody(), -8.5f + 0.9f * i, 1.5f, -5.5f, null);
            dispose(body.CreateHullShape(shapeDef, cylinder));
        }
        dispose(shapeDef, cylinder);
    }
}
