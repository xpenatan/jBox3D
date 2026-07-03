package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Quat;
import java.util.Random;

final class SimpleCompoundSample extends AbstractBox3DSample {
    SimpleCompoundSample() {
        B3Quat rotation = rotationY(0.25f * (float)Math.PI);
        B3Body ground = createBody(B3.StaticBody(), 2.0f, -1.0f, 0.0f, rotation);
        addBoxShape(ground, 4.0f, 0.5f, 4.0f, 1.0f, -0.5f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        dispose(rotation);
        addDynamicSphere(0.0f, 2.0f, 0.0f, 0.25f);
    }
}
