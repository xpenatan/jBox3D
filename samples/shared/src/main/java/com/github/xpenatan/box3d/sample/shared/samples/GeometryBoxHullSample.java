package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene from Geometry/Box Hull in sample_geometry.cpp. */
final class GeometryBoxHullSample extends AbstractBox3DSample {
    GeometryBoxHullSample() {
        B3Vec3 halfWidths = new B3Vec3(1.0f, 0.5f, 0.25f);
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        B3Vec3 origin = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Quat identity = new B3Quat();
        B3Transform transform = new B3Transform(origin, identity);

        B3Hull generated = createHull(new float[][] {
                {1.0f, 0.5f, 0.25f}, {1.0f, 0.5f, -0.25f},
                {1.0f, -0.5f, 0.25f}, {1.0f, -0.5f, -0.25f},
                {-1.0f, 0.5f, 0.25f}, {-1.0f, 0.5f, -0.25f},
                {-1.0f, -0.5f, 0.25f}, {-1.0f, -0.5f, -0.25f}
        });
        B3Hull box = B3Hull.CreateScaledBox(halfWidths, transform, scale);
        world().AddDebugHull(generated, transform, scale, 0xFFFF00);
        world().AddDebugHull(box, transform, scale, 0x00FFFF);
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);

        dispose(box, generated, transform, identity, origin, scale, halfWidths);
    }
}
