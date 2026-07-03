package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class BenchmarkHullSample extends AbstractBox3DSample {
    BenchmarkHullSample() {
        addGroundBox(50.0f);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.05f);
        for(int y = 0; y < 8; y++) {
            for(int x = 0; x < 8; x++) {
                for(int z = 0; z < 8; z++) {
                    B3Hull rock = B3Hull.CreateRock(0.45f);
                    B3Quat rotation = rotationY((x * 31.0f + z * 17.0f) * 0.017453292f);
                    B3Body body = createBody(B3.DynamicBody(), -7.0f + x * 2.0f, 1.0f + y * 1.2f,
                            -7.0f + z * 2.0f, rotation);
                    dispose(body.CreateHullShape(shapeDef, rock));
                    dispose(rotation, rock);
                }
            }
        }
        dispose(shapeDef);
    }
}
