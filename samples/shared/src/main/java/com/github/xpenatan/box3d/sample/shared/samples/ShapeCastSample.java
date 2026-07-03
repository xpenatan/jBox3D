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

final class ShapeCastSample extends AbstractBox3DSample {
    ShapeCastSample() {
        addGroundBox(24.0f);
        addStaticBox(0.0f, 5.0f, -2.5f, 0.3f, 4.0f, 0.3f, null);
        addStaticBox(0.0f, 5.0f, 2.5f, 0.3f, 4.0f, 0.3f, null);
        B3Vec3 velocity = new B3Vec3(80.0f, 0.0f, 0.0f);
        for(int i = 0; i < 5; i++) {
            B3Body body = addDynamicBox(-15.0f, 4.0f + i * 0.7f, -1.5f + i * 0.75f,
                    0.45f, 0.45f, 0.45f, null, 1.0f, 0.6f, 0.0f, 0.0f, velocity, null);
            body.SetBullet(true);
        }
        dispose(velocity);
    }
}
