package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3HeightField;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact physical scene from Issues/s&amp;box mover. */
final class IssueSandboxMoverSample extends AbstractBox3DSample {
    private B3Mesh boxMesh;
    private B3HeightField heightField;
    private B3Mesh gridMesh;

    IssueSandboxMoverSample() {
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Body heightFieldBody = createBody(B3.StaticBody(), -10.0f, 0.0f, -10.0f, null);
        B3Vec3 heightScale = new B3Vec3(0.5f, 1.0f, 0.5f);
        heightField = B3HeightField.CreateGrid(40, 40, heightScale, false);
        dispose(heightFieldBody.CreateHeightFieldShape(shapeDef, heightField), heightScale, heightFieldBody);

        // The C diagnostic creates this mesh for comparison but intentionally does not attach it.
        gridMesh = B3Mesh.CreateGrid(40, 40, 0.5f, 1, true);

        B3Body platformBody = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3Vec3 platformCenter = new B3Vec3(0.0f, 0.5f, 0.0f);
        boxMesh = B3Mesh.CreatePlatform(platformCenter, 1.0f, 2.0f, 5.0f);
        B3Vec3 unitScale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(platformBody.CreateMeshShape(shapeDef, boxMesh, unitScale), unitScale, platformCenter, platformBody);

        B3BodyDef moverDef = bodyDef(B3.DynamicBody(), 0.0f, 3.5f, 0.0f, null);
        B3MotionLocks locks = motionLocks(false, false, false, true, true, true);
        moverDef.SetMotionLocks(locks);
        moverDef.SetEnableContactRecycling(false);
        B3Body mover = world().CreateBody(moverDef);
        B3Hull box = B3Hull.CreateBox(0.25f, 1.0f, 0.25f);
        dispose(mover.CreateHullShape(shapeDef, box), box, mover, locks, moverDef, shapeDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(gridMesh, heightField, boxMesh);
    }
}
