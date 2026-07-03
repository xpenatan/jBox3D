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

final class MeshDropUnitTestSample extends AbstractBox3DSample {
    MeshDropUnitTestSample() {
        addGroundBox(18.0f);
        B3Quat rampRotation = rotationZ(-0.15f);
        addStaticBox(0.0f, 1.5f, 0.0f, 7.5f, 0.12f, 2.0f, rampRotation);
        dispose(rampRotation);
        for(int i = 0; i < 6; i++) {
            B3Body body = addDynamicBox(-4.0f + i * 1.6f, 12.0f + i * 0.25f, 0.0f, 0.3f, 0.3f, 0.3f);
            body.SetBullet(true);
        }
    }
}
