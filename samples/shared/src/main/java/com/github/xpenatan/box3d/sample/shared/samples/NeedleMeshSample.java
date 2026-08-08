package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3MeshDef;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class NeedleMeshSample extends AbstractBox3DSample {
    private final B3Mesh[] needles = new B3Mesh[4];

    NeedleMeshSample() {
        int slices = 8;
        needles[0] = createNeedle(0.99f, 0.1f, 0.2f, 0.0f, 0.2f, slices);
        needles[1] = createNeedle(1.01f, 0.1f, 0.2f, 0.0f, -0.2f, slices);
        needles[2] = createNeedle(0.98f, 0.1f, -0.2f, 0.0f, -0.2f, slices);
        needles[3] = createNeedle(1.02f, 0.1f, -0.2f, 0.0f, 0.2f, slices);

        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        for(B3Mesh needle : needles) {
            dispose(ground.CreateMeshShape(shapeDef, needle, scale));
        }

        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, 5.0f, 0.0f, null);
        B3Vec3 velocity = new B3Vec3(0.0f, -10.0f, 0.0f);
        bodyDef.SetLinearVelocity(velocity);
        B3Body body = world().CreateBody(bodyDef);
        B3Hull box = B3Hull.CreateBox(0.3f, 0.01f, 0.3f);
        dispose(body.CreateHullShape(shapeDef, box));
        dispose(box, velocity, bodyDef, scale, shapeDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(needles[3], needles[2], needles[1], needles[0]);
    }

    private static B3Mesh createNeedle(float height, float radius, float centerX, float centerY, float centerZ,
            int slices) {
        B3MeshDef meshDef = new B3MeshDef(slices + 1, slices);
        B3Vec3 vertex = new B3Vec3(centerX, centerY + height, centerZ);
        meshDef.AddVertex(vertex);
        float deltaAlpha = 2.0f * (float)Math.PI / slices;
        for(int index = 1; index <= slices; index++) {
            float alpha = (index - 1) * deltaAlpha;
            vertex.Set(centerX + radius * (float)Math.cos(alpha), centerY,
                    centerZ + radius * (float)Math.sin(alpha));
            meshDef.AddVertex(vertex);
        }
        int index1 = slices;
        for(int index = 0; index < slices; index++) {
            int index2 = index + 1;
            meshDef.AddTriangle(0, index2, index1, 0);
            index1 = index2;
        }
        meshDef.SetUseMedianSplit(true);
        B3Mesh mesh = B3Mesh.CreateFromDef(meshDef);
        dispose(vertex, meshDef);
        return mesh;
    }
}
