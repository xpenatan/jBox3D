package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact default scene from Geometry/Capsule Mass in sample_geometry.cpp. */
final class GeometryCapsuleMassSample extends AbstractBox3DSample {
    GeometryCapsuleMassSample() {
        int sides = 6;
        B3Vec3Array points = new B3Vec3Array(2 * sides * sides);
        float delta = (float)Math.PI / (sides - 1.0f);
        float angle1 = -0.5f * (float)Math.PI;
        int index = 0;
        for(int i = 0; i < sides; i++) {
            float sin1 = (float)Math.sin(angle1);
            float cos1 = (float)Math.cos(angle1);
            float angle2 = -0.5f * (float)Math.PI;
            for(int j = 0; j < sides; j++) {
                B3Vec3 point = new B3Vec3(1.0f + cos1, sin1 * (float)Math.cos(angle2),
                        sin1 * (float)Math.sin(angle2));
                points.SetValue(index++, point);
                dispose(point);
                angle2 += delta;
            }
            angle1 += delta;
        }
        angle1 = 0.5f * (float)Math.PI;
        for(int i = 0; i < sides; i++) {
            float sin1 = (float)Math.sin(angle1);
            float cos1 = (float)Math.cos(angle1);
            float angle2 = -0.5f * (float)Math.PI;
            for(int j = 0; j < sides; j++) {
                B3Vec3 point = new B3Vec3(-1.0f + cos1, sin1 * (float)Math.cos(angle2),
                        sin1 * (float)Math.sin(angle2));
                points.SetValue(index++, point);
                dispose(point);
                angle2 += delta;
            }
            angle1 += delta;
        }

        B3Hull capsuleHull = B3Hull.CreateFromPoints(points, points.GetSize());
        B3Hull box = B3Hull.CreateBox(2.0f, 1.0f, 1.0f);
        B3Vec3 p1 = new B3Vec3(-1.0f, 0.0f, 0.0f);
        B3Vec3 p2 = new B3Vec3(1.0f, 0.0f, 0.0f);
        B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        B3Quat identity = new B3Quat();
        B3Transform transform = new B3Transform(zero, identity);
        world().AddDebugCapsule(p1, p2, 1.0f, 0x00FFFF, 0.8f);
        world().AddDebugHull(box, transform, one, 0x8A2BE2);
        world().AddDebugHull(capsuleHull, transform, one, 0xFFFF00);
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);

        dispose(transform, identity, one, zero, p2, p1, box, capsuleHull, points);
    }
}
