package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default axis-contact scene from Mesh/Hollow Box. */
final class MeshHollowBoxSample extends AbstractBox3DSample {
    private final B3Mesh mesh;

    MeshHollowBoxSample() {
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 extents = new B3Vec3(10.0f, 10.0f, 10.0f);
        mesh = B3Mesh.CreateHollowBox(center, extents);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShape(shapeDef, mesh, one), ground, one);

        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        bodyDef.SetGravityScale(0.0f);
        bodyDef.SetEnableSleep(false);
        B3Hull cylinder = B3Hull.CreateCylinder(1.0f, 0.25f, 0.0f, 8);
        float[][] cylinderPositions = {
                {0.0f, -10.2f, 0.0f}, {0.0f, 9.2f, 0.0f}, {-9.8f, 0.0f, 0.0f},
                {9.8f, 0.0f, 0.0f}, {0.0f, 0.0f, -9.8f}, {0.0f, 0.0f, 9.8f}
        };
        for(float[] value : cylinderPositions) {
            B3Vec3 position = new B3Vec3(value[0], value[1], value[2]);
            bodyDef.SetPosition(position);
            B3Body body = world().CreateBody(bodyDef);
            dispose(body.CreateHullShape(shapeDef, cylinder), body, position);
        }

        B3Vec3 p1 = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 p2 = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(p1, p2, 0.25f);
        float[][] capsulePositions = {
                {0.0f, -10.2f, 2.0f}, {0.0f, 9.2f, 2.0f},
                {0.0f, -9.9f, 4.0f}, {0.0f, 8.9f, 4.0f},
                {-9.8f, 2.0f, 0.0f}, {9.8f, 2.0f, 0.0f},
                {0.0f, 2.0f, -9.8f}, {0.0f, 2.0f, 9.8f}
        };
        for(float[] value : capsulePositions) {
            B3Vec3 position = new B3Vec3(value[0], value[1], value[2]);
            bodyDef.SetPosition(position);
            B3Body body = world().CreateBody(bodyDef);
            dispose(body.CreateCapsuleShape(shapeDef, capsule), body, position);
        }
        addDebugAxes(0.0f, 0.01f, 0.0f, 1.0f);
        dispose(capsule, p2, p1, cylinder, bodyDef, shapeDef, extents, center);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }
}
