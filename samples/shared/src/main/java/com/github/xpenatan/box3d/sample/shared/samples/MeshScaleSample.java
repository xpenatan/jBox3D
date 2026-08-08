package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default state of Collision / Mesh Scale. */
final class MeshScaleSample extends AbstractBox3DSample {
    private final B3Mesh mesh;

    MeshScaleSample() {
        B3Vec3 center = new B3Vec3();
        B3Vec3 extents = new B3Vec3(0.5f, 0.5f, 0.5f);
        mesh = B3Mesh.CreateBox(center, extents, true);
        B3Body body = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(body.CreateMeshShape(shapeDef, mesh, scale), body);

        B3Vec3 origin = new B3Vec3(-2.0f, 0.0f, 0.0f);
        B3Vec3 translation = new B3Vec3(4.0f, 0.0f, 0.0f);
        B3Vec3 end = new B3Vec3(2.0f, 0.0f, 0.0f);
        B3QueryFilter filter = new B3QueryFilter();
        B3RayResult result = world().CastSphereClosest(origin, 0.25f, translation, filter);
        world().AddDebugPoint(origin, 8.0f, 0x008000);
        world().AddDebugPoint(end, 8.0f, 0xFF0000);
        world().AddDebugSegment(origin, end, 0xFFFFFF);
        if(result.GetHit()) {
            B3Vec3 hitCenter = new B3Vec3(result.GetFraction() * 4.0f, 0.0f, 0.0f);
            B3Vec3 point = result.GetPoint();
            B3Vec3 normal = result.GetNormal();
            B3Vec3 normalEnd = new B3Vec3(point.GetX() + 0.5f * normal.GetX(),
                    point.GetY() + 0.5f * normal.GetY(), point.GetZ() + 0.5f * normal.GetZ());
            world().AddDebugSphere(hitCenter, 0.25f, 0xFFFF00, 1.0f);
            world().AddDebugSegment(point, normalEnd, 0x008000);
            world().AddDebugPoint(point, 5.0f, 0xFFFF00);
            dispose(normalEnd, normal, point, hitCenter);
        }
        else {
            B3Vec3 missCenter = new B3Vec3(4.0f, 0.0f, 0.0f);
            world().AddDebugSphere(missCenter, 0.25f, 0x808080, 1.0f);
            dispose(missCenter);
        }
        dispose(result, filter, end, translation, origin, scale, shapeDef, extents, center);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }
}
