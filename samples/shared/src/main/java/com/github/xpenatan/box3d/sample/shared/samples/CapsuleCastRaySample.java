package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WheelJointDef;

final class CapsuleCastRaySample extends AbstractBox3DSample {
    CapsuleCastRaySample() {
        addGroundBox(24.0f);
        DiagnosticUtil.addCapsuleTargets(this);
        B3Vec3 velocity = new B3Vec3(95.0f, 0.0f, 0.0f);
        B3Body body = addDynamicCapsule(-15.0f, 5.0f, 0.0f, 0.8f, 0.2f);
        body.SetBullet(true);
        body.SetLinearVelocity(velocity);
        dispose(velocity);
    }
}
