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

final class NeedleMeshSample extends AbstractBox3DSample {
    NeedleMeshSample() {
        addGroundBox(26.0f);
        addStaticBox(0.0f, 5.0f, 0.0f, 0.1f, 5.0f, 4.0f, null);
        B3Vec3 velocity = new B3Vec3(140.0f, -12.0f, 0.0f);
        for(int i = 0; i < 9; i++) {
            B3Quat rotation = rotationZ((20.0f + i * 8.0f) * (float)Math.PI / 180.0f);
            B3Body body = addDynamicBox(-18.0f, 12.0f + i * 0.45f, -2.0f + i * 0.5f, 1.8f, 0.06f,
                    0.06f, rotation, 1.0f, 0.4f, 0.0f, 0.0f, velocity, null);
            body.SetBullet(true);
            dispose(rotation);
        }
        dispose(velocity);
    }
}
