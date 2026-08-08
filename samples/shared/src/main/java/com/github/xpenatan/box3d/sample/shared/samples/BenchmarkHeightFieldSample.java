package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3HeightField;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default geometry from {@code BenchmarkHeightField}. */
final class BenchmarkHeightFieldSample extends AbstractBox3DSample {
    private B3HeightField heightField;

    BenchmarkHeightFieldSample() {
        B3Body body = createBody(B3.StaticBody(), -25.0f, 0.0f, -25.0f, null);
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        heightField = B3HeightField.CreateWave(50, 50, scale, 0.02f, 0.04f, true);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(body.CreateHeightFieldShape(shapeDef, heightField), body, shapeDef, scale);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(heightField);
    }
}
