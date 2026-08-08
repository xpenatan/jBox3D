package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene from Mesh/Big Box. */
final class MeshBigBoxSample extends AbstractBox3DSample {
    private final B3Mesh boxMesh;

    MeshBigBoxSample() {
        B3Vec3 center = new B3Vec3(0.0f, -1.0f, 0.0f);
        B3Vec3 extents = new B3Vec3(50.0f, 1.0f, 50.0f);
        boxMesh = B3Mesh.CreateBox(center, extents, true);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef groundDef = new B3ShapeDef();
        B3SurfaceMaterial groundMaterial = groundDef.GetBaseMaterial();
        groundMaterial.SetFriction(0.5f);
        groundDef.SetBaseMaterial(groundMaterial);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(groundDef, boxMesh, one), ground, groundMaterial, groundDef);

        B3Body body = createBody(B3.DynamicBody(), 0.5f, 0.0f, 0.0f, null);
        B3Hull cylinder = B3Hull.CreateCylinder(0.3f, 0.15f, 0.0f, 32);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetRollingResistance(0.05f);
        shapeDef.SetBaseMaterial(material);
        dispose(body.CreateHullShape(shapeDef, cylinder), body, material, shapeDef, cylinder, one, extents, center);
        addDebugAxes(0.0f, 0.01f, 0.0f, 1.0f);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(boxMesh);
    }
}
