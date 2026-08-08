package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact hull input from Issues/Hull Crash. */
final class IssueHullCrashSample extends AbstractBox3DSample {
    private final B3Hull hull;

    IssueHullCrashSample() {
        float scale = 0.01f;
        hull = createHull(new float[][] {
                { scale * 100.000000f, scale * -142.292389f, scale * 130.826111f },
                { scale * 99.5354385f, scale * -71.3011093f, scale * 130.826111f },
                { scale * 99.5930862f, scale * -80.1112213f, scale * -100.000000f },
                { scale * 100.000000f, scale * -142.292389f, scale * -100.000000f },
                { scale * 99.5930862f, scale * -80.1112213f, scale * 130.826111f }
        });

        B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        B3Quat identity = new B3Quat();
        B3Transform transform = new B3Transform(zero, identity);
        world().AddDebugHull(hull, transform, one, 0xFFFF00);
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);
        dispose(transform, identity, one, zero);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(hull);
    }
}
