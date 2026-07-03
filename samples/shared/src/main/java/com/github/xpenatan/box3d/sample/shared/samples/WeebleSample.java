package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class WeebleSample extends AbstractBox3DSample {
    private final B3Body weeble;
    private float time;

    WeebleSample() {
        addGroundBox(30.0f);

        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, 3.0f, 0.0f, null);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.1f);
        B3Vec3 center1 = new B3Vec3(0.0f, -1.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, 1.0f);
        weeble = world().CreateBody(bodyDef);
        weeble.CreateCapsuleShape(shapeDef, capsule);
        dispose(capsule, center2, center1, shapeDef, bodyDef);
    }

    @Override
    public void step(float deltaSeconds) {
        time += deltaSeconds;
        if(time > 1.0f) {
            B3Vec3 impulse = new B3Vec3(20.0f, 0.0f, 0.0f);
            B3Vec3 point = new B3Vec3(0.0f, 4.5f, 0.0f);
            weeble.ApplyLinearImpulse(impulse, point, true);
            dispose(point, impulse);
            time = 0.0f;
        }
        super.step(deltaSeconds);
    }
}
