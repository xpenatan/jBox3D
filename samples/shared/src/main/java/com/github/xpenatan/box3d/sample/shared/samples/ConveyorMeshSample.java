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

final class ConveyorMeshSample extends AbstractBox3DSample {
    ConveyorMeshSample() {
        addGroundBox(28.0f);
        B3Vec3 leftVelocity = new B3Vec3(7.0f, 0.0f, 0.0f);
        B3Vec3 rightVelocity = new B3Vec3(-5.0f, 0.0f, 0.0f);
        for(int i = 0; i < 8; i++) {
            float x = -14.0f + i * 4.0f;
            float y = 1.0f + (i % 2) * 0.35f;
            B3Quat rotation = rotationZ((i % 2 == 0 ? 8.0f : -8.0f) * (float)Math.PI / 180.0f);
            addConveyorBox(x, y, 0.0f, 2.0f, 0.12f, 4.0f, rotation, i < 4 ? leftVelocity : rightVelocity);
            dispose(rotation);
        }
        for(int i = 0; i < 28; i++) {
            addDynamicBox(-12.0f + (i % 7) * 4.0f, 4.0f + (i / 7) * 1.2f, 0.0f, 0.35f, 0.35f, 0.35f);
        }
        dispose(rightVelocity, leftVelocity);
    }
}
