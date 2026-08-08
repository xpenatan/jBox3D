package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact physical scene from Issues/Capsule Mesh. */
final class IssueCapsuleMeshSample extends AbstractBox3DSample {
    private B3Mesh building;

    IssueCapsuleMeshSample() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3Hull groundHull = B3Hull.CreateBox(50.0f, 0.1f, 50.0f);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(ground.CreateHullShape(shapeDef, groundHull), groundHull, ground);

        building = B3Mesh.CreateFromObj(SampleAssets.readUtf8("data/meshes/building.obj"),
                1.0f, false, false, true, true, 0.002f);
        if(!building.IsValid()) {
            dispose(building, shapeDef);
            throw new IllegalStateException("Box3D could not create data/meshes/building.obj");
        }
        B3Body buildingBody = createBody(B3.StaticBody(), 0.0f, 0.1f, 0.0f, null);
        B3Vec3 unitScale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(buildingBody.CreateMeshShape(shapeDef, building, unitScale), unitScale, buildingBody);

        B3BodyDef capsuleDef = bodyDef(B3.DynamicBody(), 0.0f, 4.0f, 10.0f, null);
        B3MotionLocks locks = motionLocks(false, false, false, true, true, true);
        capsuleDef.SetMotionLocks(locks);
        capsuleDef.SetEnableSleep(false);
        capsuleDef.SetEnableContactRecycling(false);
        B3Body capsuleBody = world().CreateBody(capsuleDef);

        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetFriction(0.3f);
        material.SetCustomColor(0xFF00FF);
        B3Vec3 center1 = new B3Vec3(0.0f, -0.5f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, 0.5f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, 0.3f);
        dispose(capsuleBody.CreateCapsuleShape(shapeDef, capsule), capsule, center2, center1,
                material, capsuleBody, locks, capsuleDef, shapeDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(building);
    }
}
