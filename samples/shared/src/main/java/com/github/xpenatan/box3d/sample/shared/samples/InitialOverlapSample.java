package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3MeshDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3ShapeProxy;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact default port of Collision / Initial Overlap from the pinned Box3D commit. */
final class InitialOverlapSample extends AbstractBox3DSample {
    private final B3Mesh mesh;

    InitialOverlapSample() {
        B3MeshDef meshDef = new B3MeshDef(4, 2);
        addVertex(meshDef, -0.5f, 0.5f, 0.5f);
        addVertex(meshDef, -0.5f, 0.5f, -0.5f);
        addVertex(meshDef, -0.5f, -0.5f, -0.5f);
        addVertex(meshDef, -0.5f, -0.5f, 0.5f);
        meshDef.AddTriangle(0, 1, 2, 0);
        meshDef.AddTriangle(2, 3, 0, 0);
        meshDef.SetUseMedianSplit(false);
        mesh = B3Mesh.CreateFromDef(meshDef);

        B3Quat rotation = rotationZ(10.0f * (float)Math.PI / 180.0f);
        B3Body body = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, rotation);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(4.0f, 4.0f, 4.0f);
        dispose(body.CreateMeshShape(shapeDef, mesh, scale), body);
        dispose(scale, shapeDef, rotation, meshDef);
        drawQuery();
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        drawQuery();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }

    private void drawQuery() {
        world().ClearDebugOverlay();
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);

        B3Vec3 p1 = new B3Vec3(-2.1f, -0.8f, 0.95f);
        B3Vec3 p2 = new B3Vec3(-2.1f, 0.2f, 0.95f);
        B3Capsule capsule = new B3Capsule(p1, p2, 0.25f);
        B3Vec3Array points = new B3Vec3Array(2);
        points.SetValue(0, p1);
        points.SetValue(1, p2);
        B3ShapeProxy proxy = new B3ShapeProxy(points, 2, 0.25f);
        B3Vec3 origin = new B3Vec3();
        B3Vec3 translation = new B3Vec3();
        B3QueryFilter filter = new B3QueryFilter();

        world().AddDebugCapsule(p1, p2, capsule.GetRadius(), 0x008000, 1.0f);
        B3RayResult result = world().CastShapeClosest(origin, proxy, translation, filter, true);
        world().AddDebugCapsule(p1, p2, capsule.GetRadius(), result.GetHit() ? 0xFF0000 : 0x008000, 1.0f);
        if(result.GetHit()) {
            B3Vec3 point = result.GetPoint();
            B3Vec3 normal = result.GetNormal();
            B3Vec3 normalEnd = new B3Vec3(point.GetX() + 0.5f * normal.GetX(),
                    point.GetY() + 0.5f * normal.GetY(), point.GetZ() + 0.5f * normal.GetZ());
            world().AddDebugSegment(point, normalEnd, 0xF0F8FF);
            world().AddDebugPoint(point, 8.0f, 0xF0F8FF);
            dispose(normalEnd, normal, point);
        }
        dispose(result, filter, translation, origin, proxy, points, capsule, p2, p1);
    }

    private static void addVertex(B3MeshDef meshDef, float x, float y, float z) {
        B3Vec3 vertex = new B3Vec3(x, y, z);
        meshDef.AddVertex(vertex);
        dispose(vertex);
    }
}
