package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class BenchmarkLargePyramidSample extends AbstractBox3DSample {
    BenchmarkLargePyramidSample() {
        addGroundBox(80.0f);
        createPyramid(34, 0.5f, 0.0f, 0.0f, 1.0f);
    }

    private void createPyramid(int baseCount, float halfWidth, float offsetX, float offsetZ, float density) {
        for(int row = 0; row < baseCount; row++) {
            float y = (2.0f * row + 1.0f) * halfWidth;
            for(int column = row; column < baseCount; column++) {
                float x = (row + 1.0f) * halfWidth + 2.0f * (column - row) * halfWidth - baseCount * halfWidth;
                addDynamicBox(offsetX + x, y, offsetZ, halfWidth, halfWidth, halfWidth, null, density, 0.6f,
                        0.0f, 0.0f);
            }
        }
    }
}
