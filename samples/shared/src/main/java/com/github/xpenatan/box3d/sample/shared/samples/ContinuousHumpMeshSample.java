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

final class ContinuousHumpMeshSample extends AbstractBox3DSample {
    ContinuousHumpMeshSample() {
        addGroundBox(35.0f);
        for(int i = 0; i < 24; i++) {
            float x = -18.0f + i * 1.5f;
            float y = 0.4f + (float)Math.sin(i * 0.45f) * 1.2f;
            B3Quat rotation = rotationZ((float)Math.cos(i * 0.45f) * 0.35f);
            addStaticBox(x, y, 0.0f, 0.9f, 0.12f, 3.0f, rotation);
            dispose(rotation);
        }
        B3Vec3 velocity = new B3Vec3(95.0f, 0.0f, 0.0f);
        for(int i = 0; i < 8; i++) {
            B3Body body = createBody(B3.DynamicBody(), -22.0f, 4.0f + i * 0.45f, -1.8f + i * 0.5f, null);
            body.SetBullet(true);
            body.SetLinearVelocity(velocity);
            addSphereShape(body, 0.0f, 0.0f, 0.0f, 0.22f, 1.0f, 0.4f, 0.0f, 0.0f);
        }
        dispose(velocity);
    }
}
