package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene from Geometry/Hull Transform in sample_geometry.cpp. */
final class GeometryHullTransformSample extends AbstractBox3DSample {
    GeometryHullTransformSample() {
        B3Hull original = B3Hull.CreateCylinder(1.0f, 0.5f, 0.0f, 9);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Quat identity = new B3Quat();
        B3Transform identityTransform = new B3Transform(zero, identity);
        B3Hull transformed = B3Hull.CloneAndTransform(original, identityTransform, one);

        B3Vec3 left = new B3Vec3(-2.0f, 0.0f, 0.0f);
        B3Vec3 right = new B3Vec3(2.0f, 0.0f, 0.0f);
        B3Transform transform1 = new B3Transform(left, identity);
        B3Transform transform2 = new B3Transform(right, identity);
        world().AddDebugHull(original, transform1, one, 0x008000);
        world().AddDebugHull(transformed, transform2, one, 0xFFFF00);
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);

        dispose(transform2, transform1, right, left, transformed, identityTransform, identity, zero, one, original);
    }
}
