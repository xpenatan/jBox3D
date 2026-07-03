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

final class BenchmarkFallingTreesSample extends AbstractBox3DSample {
    BenchmarkFallingTreesSample() {
        addGroundBox(50.0f);
        for(int i = 0; i < 40; i++) {
            float x = -20.0f + (i % 10) * 4.0f;
            float z = -8.0f + (i / 10) * 5.0f;
            B3Body trunk = addDynamicCylinder(x, 4.0f, z, 3.0f, 0.25f, 8);
            B3Vec3 push = new B3Vec3((i % 2 == 0 ? 30.0f : -30.0f), 0.0f, (i % 3 - 1) * 20.0f);
            trunk.ApplyLinearImpulseToCenter(push, true);
            dispose(push);
            addDynamicSphere(x, 7.2f, z, 0.85f, 0.5f, 0.6f, 0.0f, 0.0f);
        }
    }
}
