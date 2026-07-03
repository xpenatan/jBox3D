package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3DistanceJointDef;
import com.github.xpenatan.box3d.B3Filter;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3Vec3;

import java.util.ArrayList;
import java.util.List;

final class BodyCastSample extends AbstractBox3DSample {
    private B3Body castBody;
    private float timer;

    BodyCastSample() {
        addGroundBox(28.0f);
        for(int i = 0; i < 8; i++) {
            addStaticBox(-10.0f + i * 3.0f, 1.0f + (i % 3) * 1.2f, 0.0f, 0.4f, 1.0f, 2.0f, null);
            addDynamicBox(-8.5f + i * 2.5f, 6.0f + i * 0.35f, 0.0f, 0.4f, 0.4f, 0.4f);
        }
        launch();
    }

    @Override
    public void step(float deltaSeconds) {
        timer += deltaSeconds;
        if(timer > 2.0f) {
            timer = 0.0f;
            launch();
        }
        super.step(deltaSeconds);
    }

    private void launch() {
        if(castBody != null && castBody.IsValid()) {
            castBody.Destroy();
            dispose(castBody);
        }
        B3Vec3 velocity = new B3Vec3(75.0f, 8.0f, 0.0f);
        B3Vec3 spin = new B3Vec3(0.0f, 0.0f, 14.0f);
        castBody = addDynamicBox(-18.0f, 2.5f, 0.0f, 0.65f, 0.25f, 0.65f, null, 5.0f, 0.6f, 0.0f, 0.0f,
                velocity, spin);
        castBody.SetBullet(true);
        dispose(spin, velocity);
    }
}
