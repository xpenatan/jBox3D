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

final class ContinuousStallSample extends AbstractBox3DSample {
    ContinuousStallSample() {
        addGroundBox(30.0f);
        for(int i = 0; i < 14; i++) {
            addStaticBox(-10.0f + i * 1.6f, 4.0f, 0.0f, 0.08f, 4.0f, 4.0f, null);
        }
        B3Vec3 velocity = new B3Vec3(120.0f, 0.0f, 0.0f);
        for(int i = 0; i < 24; i++) {
            B3Body body = addDynamicSphere(-20.0f, 1.0f + i * 0.35f, -2.0f + (i % 9) * 0.5f, 0.12f);
            body.SetBullet(true);
            body.SetLinearVelocity(velocity);
        }
        dispose(velocity);
    }
}
