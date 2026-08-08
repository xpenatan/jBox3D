package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact default sphere-reduction scene from Geometry/Hull Reduction. */
final class GeometryHullReductionSample extends AbstractBox3DSample {
    GeometryHullReductionSample() {
        SampleRandom random = new SampleRandom(42);
        B3Vec3Array points = new B3Vec3Array(128);
        for(int i = 0; i < 128; i++) {
            B3Vec3 point = random.nextUnitVector();
            points.SetValue(i, point);
            dispose(point);
        }
        B3Hull hull = B3Hull.CreateFromPoints(points, 16);
        B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        B3Quat identity = new B3Quat();
        B3Transform transform = new B3Transform(zero, identity);
        world().AddDebugHull(hull, transform, one, 0xFFFF00);
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);
        dispose(transform, identity, one, zero, hull, points);
    }
}
