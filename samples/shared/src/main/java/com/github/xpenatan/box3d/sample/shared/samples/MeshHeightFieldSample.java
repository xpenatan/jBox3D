package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3HeightField;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact release-build default scene from Mesh/Height Field. */
final class MeshHeightFieldSample extends AbstractBox3DSample {
    private final B3HeightField heightField;

    MeshHeightFieldSample() {
        int rowCount = 400;
        int columnCount = 400;
        B3Vec3 scale = new B3Vec3(2.0f, 1.5f, 2.0f);
        heightField = B3HeightField.CreateWave(rowCount, columnCount, scale, 0.1f, 0.03333f, false);
        B3Body ground = createBody(B3.StaticBody(), -399.0f, 0.0f, -399.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(ground.CreateHeightFieldShape(shapeDef, heightField), ground, shapeDef);

        B3Vec3 origin = new B3Vec3(5.5f, 4.0f, 1.01f);
        B3Vec3 translation = new B3Vec3(0.0f, -8.0f, 0.0f);
        B3QueryFilter filter = new B3QueryFilter();
        B3RayResult result = world().CastSphereClosest(origin, 0.2f, translation, filter);
        B3Vec3 end = new B3Vec3(5.5f, -4.0f, 1.01f);
        world().AddDebugPoint(origin, 2.0f, 0x008000);
        world().AddDebugPoint(end, 2.0f, 0xFF0000);
        world().AddDebugSegment(origin, end, 0xFFFF00);
        float fraction = result.GetFraction();
        B3Vec3 sphereCenter = new B3Vec3(5.5f, 4.0f - 8.0f * fraction, 1.01f);
        world().AddDebugSphere(sphereCenter, 0.2f, 0xFFA500, 1.0f);
        if(result.GetHit()) {
            B3Vec3 point = result.GetPoint();
            B3Vec3 normal = result.GetNormal();
            B3Vec3 normalEnd = new B3Vec3(point.GetX() + 0.5f * normal.GetX(),
                    point.GetY() + 0.5f * normal.GetY(), point.GetZ() + 0.5f * normal.GetZ());
            world().AddDebugSegment(point, normalEnd, 0x008000);
            world().AddDebugPoint(point, 6.0f, 0x800080);
            dispose(normalEnd, normal, point);
        }
        addDebugAxes(0.0f, 0.1f, 0.0f, 0.5f);
        dispose(sphereCenter, end, result, filter, translation, origin, scale);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(heightField);
    }
}
