package com.github.xpenatan.box3d.sample.shared.samples;

final class Pyramid2DSample extends AbstractBox3DSample {
    Pyramid2DSample() {
        addGroundBox(40.0f);

        float a = 1.0f;
        int size = 12;
        for(int row = 0; row < size; row++) {
            for(int column = 0; column < size - row; column++) {
                addDynamicBox((-10.0f + 2.0f * column + row) * a, (1.5f + 2.5f * row) * a, 0.0f, a, a, a);
            }
        }
    }
}
