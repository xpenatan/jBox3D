package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3MeshDef;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class ContinuousHumpMeshSample extends AbstractBox3DSample {
    private B3Mesh hump;

    ContinuousHumpMeshSample() {
        addGroundBox(20.0f);
        hump = createHump(8.0f);

        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(shapeDef, hump, scale));

        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, 5.0f, 0.0f, null);
        B3Vec3 velocity = new B3Vec3(0.0f, -50.0f, 0.0f);
        bodyDef.SetLinearVelocity(velocity);
        B3Body body = world().CreateBody(bodyDef);
        B3Hull box = B3Hull.CreateBox(0.5f, 0.05f, 1.0f);
        dispose(body.CreateHullShape(shapeDef, box));
        dispose(box, velocity, bodyDef, scale, shapeDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(hump);
    }

    private static B3Mesh createHump(float cellWidth) {
        B3MeshDef meshDef = new B3MeshDef(6, 4);
        B3Vec3 vertex = new B3Vec3();
        float x = -0.5f * cellWidth;
        for(int ix = 0; ix <= 1; ix++) {
            float z = -cellWidth;
            for(int iz = 0; iz <= 2; iz++) {
                vertex.Set(x, iz == 1 ? 0.05f * cellWidth : 0.0f, z);
                meshDef.AddVertex(vertex);
                z += cellWidth;
            }
            x += cellWidth;
        }
        for(int iz = 0; iz < 2; iz++) {
            int index1 = iz;
            int index2 = index1 + 1;
            int index3 = index2 + 3;
            int index4 = index3 - 1;
            meshDef.AddTriangle(index1, index2, index3, 0);
            meshDef.AddTriangle(index3, index4, index1, 0);
        }
        meshDef.SetUseMedianSplit(true);
        meshDef.SetIdentifyEdges(true);
        B3Mesh mesh = B3Mesh.CreateFromDef(meshDef);
        dispose(vertex, meshDef);
        return mesh;
    }
}
