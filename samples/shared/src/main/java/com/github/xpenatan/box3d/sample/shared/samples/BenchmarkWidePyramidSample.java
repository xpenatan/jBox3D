package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class BenchmarkWidePyramidSample extends AbstractBox3DSample {
    BenchmarkWidePyramidSample() {
        addGroundBox(80.0f);
        for(int stack = 0; stack < 3; stack++) {
            createLayeredPyramid(18, 0.5f, -24.0f + stack * 24.0f);
        }
    }

    private void createLayeredPyramid(int baseCount, float halfWidth, float offsetX) {
        for(int row = 0; row < baseCount; row++) {
            float y = (2.0f * row + 1.0f) * halfWidth;
            for(int column = row; column < baseCount; column++) {
                float x = (row + 1.0f) * halfWidth + 2.0f * (column - row) * halfWidth - baseCount * halfWidth;
                addDynamicBox(offsetX + x, y, 0.0f, halfWidth, halfWidth, halfWidth);
            }
        }
    }
}
