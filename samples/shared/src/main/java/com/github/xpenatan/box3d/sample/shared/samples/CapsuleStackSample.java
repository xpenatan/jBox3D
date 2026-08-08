package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class CapsuleStackSample extends AbstractBox3DSample {
    CapsuleStackSample() {
        addGroundBox(40.0f);

        B3MotionLocks locks = motionLocks(false, false, true, true, true, true);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 center1 = new B3Vec3(-1.0f, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(1.0f, 0.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, 0.5f);

        float y = 0.75f;
        for(int i = 0; i < 20; i++) {
            B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, y, 0.0f, null);
            bodyDef.SetMotionLocks(locks);
            B3Body body = world().CreateBody(bodyDef);
            dispose(body.CreateCapsuleShape(shapeDef, capsule), bodyDef);
            y += 1.0f;
        }

        dispose(capsule, center2, center1, shapeDef, locks);
    }
}
