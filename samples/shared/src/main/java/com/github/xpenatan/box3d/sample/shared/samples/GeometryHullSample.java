package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;

/** Exact regression hull from Geometry/Hull in sample_geometry.cpp. */
final class GeometryHullSample extends AbstractBox3DSample {
    GeometryHullSample() {
        float[][] source = {
                {-3.9866004f, 75.4595108f, 28.3783073f}, {-13.1079493f, 73.080368f, 28.296587f},
                {-18.6611958f, 72.0040894f, 16.9292431f}, {4.82537603f, 79.2908554f, 22.2369995f},
                {-12.7315464f, 79.2187576f, 2.94275379f}, {-21.806488f, 78.7758865f, 0.985544085f},
                {-27.7619209f, 73.3481522f, 11.9647141f}, {-22.3994541f, 72.2203826f, 21.4116211f},
                {-25.3797474f, 76.7417755f, 27.9124985f}, {-22.7552319f, 77.0559006f, 29.4733639f},
                {-6.81736374f, 78.3484726f, 36.8649979f}, {3.62397718f, 85.5270843f, 29.2077713f},
                {7.90363788f, 84.121231f, 18.2612896f}, {-12.3809223f, 84.5280533f, -0.43230924f},
                {5.83599472f, 95.2908325f, 4.4423275f}, {-22.5541401f, 89.9094467f, -4.87791252f},
                {-43.9060402f, 78.5287094f, 1.32877088f}, {-42.6015129f, 76.7829742f, 7.67437983f},
                {-25.735527f, 78.1218796f, 27.908411f}, {-23.5183544f, 77.6326675f, 29.1178799f},
                {2.0977366f, 100.430191f, 34.3929482f}, {1.09743047f, 103.952553f, 35.5656395f},
                {8.50175952f, 96.0529861f, 8.73674774f}, {2.52570295f, 103.303696f, 32.2314339f},
                {-20.099781f, 89.4923248f, -4.15468454f}, {2.8092947f, 123.516098f, -1.12693477f},
                {-43.9318161f, 79.1106186f, 1.39006138f}, {-23.358511f, 90.9599686f, -4.25683546f},
                {2.10804915f, 123.603645f, -1.38435471f}, {-44.1329117f, 78.7192383f, 1.54941654f},
                {-42.4365158f, 77.725357f, 8.14835929f}, {-43.204792f, 77.5811691f, 7.14319515f},
                {-44.17416f, 78.7810363f, 2.50146222f}, {-32.8975143f, 99.1221771f, 7.55588436f},
                {-0.624746263f, 110.070351f, 32.7381058f}, {0.00431228895f, 109.14341f, 33.6411133f},
                {-0.58865279f, 122.980537f, 16.6554794f}, {2.18539238f, 124.324593f, -0.620266676f},
                {-1.02177501f, 123.881721f, 16.8230057f}, {1.9842999f, 124.571777f, -0.321986318f},
                {1.86570692f, 124.365791f, -0.599836588f}, {-43.591507f, 78.1373291f, 6.1135149f},
                {-43.8235397f, 79.2239074f, 3.48619604f}, {-43.591507f, 78.50811f, 5.54555655f},
                {1.21086729f, 124.49453f, 1.07543683f}, {-1.86223853f, 124.195847f, 15.6257992f},
                {-1.46520972f, 124.355492f, 16.9864483f}, {1.654302f, 124.612976f, 0.621887207f}
        };
        B3Vec3Array points = new B3Vec3Array(source.length);
        for(int i = 0; i < source.length; i++) {
            B3Vec3 point = new B3Vec3(0.01f * source[i][0], 0.01f * source[i][1], 0.01f * source[i][2]);
            points.SetValue(i, point);
            dispose(point);
        }
        B3Hull hull = B3Hull.CreateFromPoints(points, 16);
        if(hull.IsValid()) {
            B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
            B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
            B3Quat identity = new B3Quat();
            B3Transform transform = new B3Transform(zero, identity);
            world().AddDebugHull(hull, transform, one, 0xFFFF00);
            dispose(transform, identity, one, zero);
        }
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);
        dispose(hull, points);
    }
}
