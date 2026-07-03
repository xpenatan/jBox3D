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

final class ContinuousMeshDropSample extends AbstractBox3DSample {
    ContinuousMeshDropSample() {
        addGroundBox(30.0f);
        for(int i = 0; i < 12; i++) {
            B3Quat rotation = rotationZ((-30.0f + i * 5.0f) * (float)Math.PI / 180.0f);
            addStaticBox(-12.0f + i * 2.2f, 1.0f + (i % 2), 0.0f, 1.4f, 0.15f, 4.0f, rotation);
            dispose(rotation);
        }
        for(int i = 0; i < 16; i++) {
            B3Body body = addDynamicBox(-10.0f + i * 1.3f, 18.0f + i * 0.4f, 0.0f, 0.35f, 0.35f, 0.35f);
            body.SetBullet(true);
        }
    }
}
