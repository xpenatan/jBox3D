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

final class TimeOfImpactSample extends AbstractBox3DSample {
    TimeOfImpactSample() {
        addGroundBox(24.0f);
        addStaticBox(0.0f, 4.0f, 0.0f, 0.08f, 4.0f, 4.0f, null);
        B3Vec3 velocity = new B3Vec3(140.0f, 0.0f, 0.0f);
        B3Quat rotation = rotationZ(0.3f);
        B3Body body = addDynamicBox(-16.0f, 4.5f, 0.0f, 1.7f, 0.08f, 0.08f, rotation,
                1.0f, 0.4f, 0.0f, 0.0f, velocity, null);
        body.SetBullet(true);
        dispose(velocity, rotation);
    }
}
