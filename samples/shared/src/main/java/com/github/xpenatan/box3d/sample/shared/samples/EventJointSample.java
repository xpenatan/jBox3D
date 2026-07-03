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

final class EventJointSample extends AbstractBox3DSample {
    EventJointSample() {
        addGroundBox(20.0f);
        B3Body left = addDynamicBox(-1.0f, 5.0f, 0.0f, 0.5f, 0.5f, 0.5f);
        B3Body right = addDynamicBox(1.0f, 5.0f, 0.0f, 0.5f, 0.5f, 0.5f);
        SamplePortUtil.createDistance(this, left, right, 2.0f, 4.0f, 0.7f);
        B3Vec3 impulse = new B3Vec3(18.0f, 0.0f, 0.0f);
        left.ApplyLinearImpulseToCenter(impulse, true);
        impulse.Set(-18.0f, 0.0f, 0.0f);
        right.ApplyLinearImpulseToCenter(impulse, true);
        dispose(impulse);
    }
}
