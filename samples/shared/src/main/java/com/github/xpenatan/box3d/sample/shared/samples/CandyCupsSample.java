package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class CandyCupsSample extends AbstractBox3DSample {
    CandyCupsSample() {
        addGroundBox(60.0f);
        B3Hull cup = B3Hull.CreateCone(1.0f, 0.6f, 0.95f, 8);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                for(int k = 0; k < 7; k++) {
                    B3Body body = createBody(B3.DynamicBody(), -9.0f + 3.0f * j, 1.0f * i,
                            -9.0f + 3.0f * k, null);
                    dispose(body.CreateHullShape(shapeDef, cup));
                }
            }
        }
        dispose(shapeDef, cup);
    }
}
