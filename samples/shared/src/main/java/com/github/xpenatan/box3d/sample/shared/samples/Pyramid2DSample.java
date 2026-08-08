package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3ShapeDef;

final class Pyramid2DSample extends AbstractBox3DSample {
    Pyramid2DSample() {
        addGroundBox(40.0f);

        float a = 1.0f;
        int size = 12;
        B3Hull box = B3Hull.CreateBox(a, a, a);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3MotionLocks locks = motionLocks(false, false, true, true, true, false);
        for(int row = 0; row < size; row++) {
            for(int column = 0; column < size - row; column++) {
                B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), (-10.0f + 2.0f * column + row) * a,
                        (1.5f + 2.5f * row) * a, 0.0f, null);
                bodyDef.SetMotionLocks(locks);
                B3Body body = world().CreateBody(bodyDef);
                dispose(body.CreateHullShape(shapeDef, box), bodyDef);
            }
        }
        dispose(locks, shapeDef, box);
    }
}
