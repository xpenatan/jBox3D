package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact initial physical scene from Issues/Crash. */
final class IssueCrashSample extends AbstractBox3DSample {
    private B3Mesh gridMesh;

    IssueCrashSample() {
        B3BodyDef groundDef = bodyDef(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        B3Body ground = world().CreateBody(groundDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        gridMesh = B3Mesh.CreateGrid(20, 20, 2.0f, 0, true);
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(shapeDef, gridMesh, scale), scale, ground, groundDef);

        B3Hull box = B3Hull.CreateBox(0.5f, 0.5f, 0.5f);
        B3Body body1 = createBody(B3.DynamicBody(), 2.0f, 4.0f, 0.0f, null);
        B3Body body2 = createBody(B3.DynamicBody(), -2.0f, 4.0f, 0.0f, null);
        dispose(body1.CreateHullShape(shapeDef, box), body2.CreateHullShape(shapeDef, box));
        dispose(body2, body1, box, shapeDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(gridMesh);
    }
}
