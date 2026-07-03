package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class BenchmarkManyPyramidsSample extends AbstractBox3DSample {
    BenchmarkManyPyramidsSample() {
        addGroundBox(90.0f);
        for(int z = 0; z < 5; z++) {
            for(int x = 0; x < 5; x++) {
                createPyramid(8, 0.45f, -18.0f + x * 9.0f, -18.0f + z * 9.0f);
            }
        }
    }

    private void createPyramid(int baseCount, float halfWidth, float offsetX, float offsetZ) {
        for(int row = 0; row < baseCount; row++) {
            float y = (2.0f * row + 1.0f) * halfWidth;
            for(int column = row; column < baseCount; column++) {
                float x = (row + 1.0f) * halfWidth + 2.0f * (column - row) * halfWidth - baseCount * halfWidth;
                addDynamicBox(offsetX + x, y, offsetZ, halfWidth, halfWidth, halfWidth);
            }
        }
    }
}
