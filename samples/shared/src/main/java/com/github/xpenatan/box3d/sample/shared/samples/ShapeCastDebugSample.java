package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3CastOutput;
import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeProxy;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact port of the pinned Collision / Shape Cast Debug regression case. */
final class ShapeCastDebugSample extends AbstractBox3DSample {
    private final B3Vec3[] triangle = new B3Vec3[3];
    private final B3Capsule capsule;
    private final B3Transform transform;
    private final B3Vec3 translation;
    private final B3Hull box;
    private final B3ShapeProxy triangleProxy;
    private final B3ShapeProxy capsuleProxy;

    ShapeCastDebugSample() {
        float scale = 0.01f;
        triangle[0] = scaled(scale, 0.0f, 0.0f, 0.0f);
        triangle[1] = scaled(scale, 0.0f, -6400.0f, 0.0f);
        triangle[2] = scaled(scale, 6400.0f, 0.0f, 22.609375f);
        float ox = triangle[0].GetX();
        float oy = triangle[0].GetY();
        float oz = triangle[0].GetZ();
        triangle[0].Set(0.0f, 0.0f, 0.0f);
        triangle[1].Set(triangle[1].GetX() - ox, triangle[1].GetY() - oy, triangle[1].GetZ() - oz);
        triangle[2].Set(triangle[2].GetX() - ox, triangle[2].GetY() - oy, triangle[2].GetZ() - oz);

        B3Vec3Array hullPoints = new B3Vec3Array(8);
        setScaled(hullPoints, 0, scale, 200.305283f, 200.460999f, 9.53760529f);
        setScaled(hullPoints, 1, scale, -200.305283f, 200.460999f, 9.53760529f);
        setScaled(hullPoints, 2, scale, -200.305283f, -200.460999f, 9.53760529f);
        setScaled(hullPoints, 3, scale, 200.305283f, -200.460999f, 9.53760529f);
        setScaled(hullPoints, 4, scale, 200.305283f, 200.460999f, -9.53760529f);
        setScaled(hullPoints, 5, scale, -200.305283f, 200.460999f, -9.53760529f);
        setScaled(hullPoints, 6, scale, -200.305283f, -200.460999f, -9.53760529f);
        setScaled(hullPoints, 7, scale, 200.305283f, -200.460999f, -9.53760529f);
        box = B3Hull.CreateFromPoints(hullPoints, 8);

        B3Vec3 center1 = scaled(scale, 43616.2109375f, -100213.0f, 132631.8125f);
        B3Vec3 center2 = scaled(scale, 342231.96875f, 359711.6875f, 132631.8125f);
        capsule = new B3Capsule(center1, center2, scale);
        B3Vec3Array capsulePoints = new B3Vec3Array(2);
        capsulePoints.SetValue(0, center1);
        capsulePoints.SetValue(1, center2);
        capsuleProxy = new B3ShapeProxy(capsulePoints, 2, scale);

        B3Vec3 position = new B3Vec3(scale * -115200.0f - ox, scale * -19200.0f - oy,
                scale * -202755.0f - oz);
        B3Quat identity = new B3Quat();
        transform = new B3Transform(position, identity);
        translation = scaled(scale, 0.008614914f, 0.0f, 72267.1171875f);

        B3Vec3Array trianglePoints = new B3Vec3Array(3);
        trianglePoints.SetValue(0, triangle[0]);
        trianglePoints.SetValue(1, triangle[1]);
        trianglePoints.SetValue(2, triangle[2]);
        triangleProxy = new B3ShapeProxy(trianglePoints, 3, 0.0f);
        dispose(trianglePoints, identity, position, capsulePoints, center2, center1, hullPoints);
        drawCase();
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        drawCase();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(capsuleProxy, triangleProxy, box, translation, transform, capsule,
                triangle[2], triangle[1], triangle[0]);
    }

    private void drawCase() {
        world().ClearDebugOverlay();
        addDebugGroundGrid(10);
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);

        B3CastOutput output = B3Collision.ShapeCast(triangleProxy, capsuleProxy, transform, translation,
                0.970617533f, false);
        world().AddDebugTriangle(triangle[0], triangle[1], triangle[2], 0x00FFFF);
        drawCapsule(transform, 0x008000);

        B3Vec3 start = transform.GetP();
        B3Quat rotation = transform.GetQ();
        if(output.GetHit()) {
            float fraction = output.GetFraction();
            B3Vec3 hitPosition = new B3Vec3(start.GetX() + fraction * translation.GetX(),
                    start.GetY() + fraction * translation.GetY(), start.GetZ() + fraction * translation.GetZ());
            B3Transform hitTransform = new B3Transform(hitPosition, rotation);
            drawCapsule(hitTransform, 0xFF0000);
            dispose(hitTransform, hitPosition);
        }

        B3Vec3 endPosition = new B3Vec3(start.GetX() + translation.GetX(), start.GetY() + translation.GetY(),
                start.GetZ() + translation.GetZ());
        B3Transform endTransform = new B3Transform(endPosition, rotation);
        drawCapsule(endTransform, 0x808080);
        dispose(endTransform, endPosition, rotation, start, output);
    }

    private void drawCapsule(B3Transform capsuleTransform, int color) {
        B3Vec3 local1 = capsule.GetCenter1();
        B3Vec3 local2 = capsule.GetCenter2();
        B3Vec3 world1 = capsuleTransform.TransformPoint(local1);
        B3Vec3 world2 = capsuleTransform.TransformPoint(local2);
        world().AddDebugCapsule(world1, world2, capsule.GetRadius(), color, 1.0f);
        dispose(world2, world1, local2, local1);
    }

    private static B3Vec3 scaled(float scale, float x, float y, float z) {
        return new B3Vec3(scale * x, scale * y, scale * z);
    }

    private static void setScaled(B3Vec3Array points, int index, float scale, float x, float y, float z) {
        B3Vec3 point = scaled(scale, x, y, z);
        points.SetValue(index, point);
        dispose(point);
    }
}
