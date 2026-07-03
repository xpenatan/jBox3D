package com.github.xpenatan.box3d.sample.shared.samples;

final class HighMassRatio1Sample extends AbstractBox3DSample {
    HighMassRatio1Sample() {
        addGroundBox(50.0f);
        float extent = 1.0f;
        for(int stack = 0; stack < 3; stack++) {
            int count = 10;
            float offset = -20.0f * extent + 2.0f * (count + 1.0f) * extent * stack;
            float y = extent;
            while(count > 0) {
                for(int i = 0; i < count; i++) {
                    float coeff = i - 0.5f * count;
                    float yy = count == 1 ? y + 2.0f : y;
                    float density = count == 1 ? (stack + 1.0f) * 100.0f : 1.0f;
                    addDynamicBox(2.0f * coeff * extent + offset, yy, 0.0f, extent, extent, extent, null,
                            density, 0.6f, 0.0f, 0.0f);
                }
                count--;
                y += 2.0f * extent;
            }
        }
    }
}
