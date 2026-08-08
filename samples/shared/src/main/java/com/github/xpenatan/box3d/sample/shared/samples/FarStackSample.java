package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;

/** Exact default BuildScene from World/Far Stack. */
final class FarStackSample extends AbstractBox3DSample {
    private static final float DOUBLE_PRECISION_OFFSET = 10_000_000.0f;

    FarStackSample() {
        float base = B3.IsDoublePrecision() ? DOUBLE_PRECISION_OFFSET : 0.0f;
        B3Body ground = createBody(B3.StaticBody(), base, -1.0f, 0.0f, null);
        addBoxShape(ground, 12.0f, 1.0f, 12.0f, 0.0f, 0.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        dispose(ground);
        for(int i = 0; i < 6; i++) {
            float skew = (i & 1) == 0 ? -0.02f : 0.02f;
            dispose(addDynamicBox(base + skew, 0.5f + i, 0.0f, 0.5f, 0.5f, 0.5f));
        }
    }

    static float offset() {
        // The pinned default build is single precision, so the C sample initially opens at the origin.
        return 0.0f;
    }
}
