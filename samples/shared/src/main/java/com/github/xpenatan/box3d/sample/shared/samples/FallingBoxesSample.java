package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class FallingBoxesSample extends AbstractBox3DSample {
    FallingBoxesSample() {
        addGroundBox(100.0f);
        float a = 0.5f;
        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < 8; j++) {
                for(int k = 0; k < 8; k++) {
                    addDynamicBox(-16.0f * a + 4.0f * a * j, 4.0f * a * i + 5.0f * a,
                            -16.0f * a + 4.0f * a * k, a, a, a);
                }
            }
        }
    }
}
