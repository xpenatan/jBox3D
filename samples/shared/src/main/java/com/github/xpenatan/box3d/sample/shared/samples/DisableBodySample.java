package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WeldJointDef;

final class DisableBodySample extends AbstractBox3DSample {
    private final B3Body[] bodies = new B3Body[4];
    private final B3Body ball;
    private int steps;

    DisableBodySample() {
        addGroundBox(20.0f);
        for(int i = 0; i < bodies.length; i++) {
            bodies[i] = addDynamicCapsule(0.0f, 3.0f + i * 0.65f, 0.0f, 0.25f, 0.1f);
        }
        ball = addDynamicSphere(3.0f, 3.0f, 0.0f, 0.5f);
    }

    @Override
    public void step(float deltaSeconds) {
        steps++;
        if(steps == 60) {
            bodies[2].Disable();
        }
        else if(steps == 120) {
            bodies[2].Enable();
            ball.Disable();
        }
        else if(steps == 180) {
            ball.Enable();
        }

        if(bodies[2].IsEnabled()) {
            B3Vec3 impulse = new B3Vec3(0.0f, 0.1f, 0.0f);
            bodies[2].ApplyLinearImpulseToCenter(impulse, true);
            dispose(impulse);
        }

        super.step(deltaSeconds);
    }
}
