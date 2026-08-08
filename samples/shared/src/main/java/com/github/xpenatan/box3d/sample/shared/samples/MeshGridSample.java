package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene from Mesh/Grid. */
final class MeshGridSample extends AbstractBox3DSample {
    private final B3Mesh gridMesh;

    MeshGridSample() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        gridMesh = B3Mesh.CreateGrid(20, 20, 1.0f, 0, true);
        B3ShapeDef groundDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(2.0f, 2.0f, 2.0f);
        dispose(ground.CreateMeshShape(groundDef, gridMesh, scale), ground, groundDef);

        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.1f, 1.0f, -0.1f, null);
        bodyDef.SetAngularDamping(0.1f);
        B3Body body = world().CreateBody(bodyDef);
        B3Hull cylinder = B3Hull.CreateCylinder(1.0f, 0.25f, 0.0f, 15);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetRollingResistance(0.02f);
        shapeDef.SetBaseMaterial(material);
        dispose(body.CreateHullShape(shapeDef, cylinder), body, material, shapeDef, cylinder, bodyDef, scale);
        addDebugAxes(0.0f, 0.01f, 0.0f, 1.0f);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(gridMesh);
    }
}
