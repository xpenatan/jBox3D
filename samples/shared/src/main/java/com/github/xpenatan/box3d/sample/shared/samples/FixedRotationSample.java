package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

final class FixedRotationSample extends AbstractBox3DSample {
    FixedRotationSample() {
        addGroundBox(20.0f);

        B3Body staticBody = createBody(B3.StaticBody(), 0.0f, 0.5f, 0.0f, null);
        addCapsuleShape(staticBody, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.3f, 0.0f, 0.6f, 0.0f, 0.0f);

        com.github.xpenatan.box3d.B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.3f, 0.5f, 0.0f, null);
        bodyDef.SetGravityScale(0.0f);
        bodyDef.SetEnableSleep(false);
        B3MotionLocks locks = motionLocks(false, false, false, true, true, true);
        bodyDef.SetMotionLocks(locks);
        B3Body lockedBody = world().CreateBody(bodyDef);
        addCapsuleShape(lockedBody, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.2f, 1.0f, 0.6f, 0.0f, 0.0f);
        dispose(locks, bodyDef);
    }
}
