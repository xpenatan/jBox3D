package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class BenchmarkRainSample extends AbstractBox3DSample {
    private int stepCount;

    BenchmarkRainSample() {
        addGroundBox(60.0f);
        for(int y = 0; y < 12; y++) {
            addRainRow(y);
        }
    }

    @Override
    public void step(float deltaSeconds) {
        stepCount++;
        if(stepCount % 20 == 0) {
            addRainRow(12 + (stepCount / 20) % 4);
        }
        super.step(deltaSeconds);
    }

    private void addRainRow(int layer) {
        for(int x = 0; x < 7; x++) {
            for(int z = 0; z < 7; z++) {
                float px = -12.0f + x * 4.0f;
                float py = 15.0f + layer * 1.5f;
                float pz = -12.0f + z * 4.0f;
                if(((x + z + layer) & 1) == 0) {
                    addDynamicSphere(px, py, pz, 0.35f);
                }
                else {
                    addDynamicBox(px, py, pz, 0.35f, 0.35f, 0.35f);
                }
            }
        }
    }
}
