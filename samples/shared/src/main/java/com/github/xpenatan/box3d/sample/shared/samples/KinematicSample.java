package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

final class KinematicSample extends AbstractBox3DSample {
    private final B3Body body;
    private float time;

    KinematicSample() {
        addGroundBox(20.0f);

        B3Hull hull = B3Hull.CreateBox(0.1f, 1.0f, 0.2f);
        body = addHull(hull, B3.KinematicBody(), 4.0f, 3.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        dispose(hull);
    }

    @Override
    public void step(float deltaSeconds) {
        time += deltaSeconds;
        float delay = 2.0f;
        if(time > delay) {
            float t = time - delay;
            float amplitude = 2.0f;
            B3Vec3 position = new B3Vec3(2.0f * amplitude * (float)Math.cos(t),
                    amplitude * ((float)Math.sin(2.0f * t) + 1.0f) + 1.0f, 0.0f);
            B3Quat rotation = rotationZ(2.0f * t);
            body.SetTransform(position, rotation);
            dispose(rotation, position);
        }
        super.step(deltaSeconds);
    }
}
