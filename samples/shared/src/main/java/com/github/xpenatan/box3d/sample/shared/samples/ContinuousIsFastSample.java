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

final class ContinuousIsFastSample extends AbstractBox3DSample {
    ContinuousIsFastSample() {
        addGroundBox(22.0f);
        addStaticBox(0.0f, 3.0f, 0.0f, 0.1f, 3.0f, 4.0f, null);
        for(int i = 0; i < 12; i++) {
            B3Vec3 velocity = new B3Vec3(90.0f + i * 12.0f, 0.0f, 0.0f);
            B3Body body = addDynamicBox(-15.0f, 1.0f + i * 0.6f, -2.0f + (i % 5), 0.18f, 0.18f, 0.18f,
                    null, 1.0f, 0.4f, 0.0f, 0.0f, velocity, null);
            body.SetBullet((i & 1) == 0);
            dispose(velocity);
        }
    }
}
