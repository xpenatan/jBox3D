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

final class IssueSandboxMoverSample extends AbstractBox3DSample {
    IssueSandboxMoverSample() {
        B3WorldDefBuilder.disableGravity(world());
        addGroundBox(18.0f);
        SamplePortUtil.addObstacleCourse(this);
        for(int i = 0; i < 8; i++) {
            B3Body body = addDynamicCapsule(-7.0f + i * 2.0f, 3.0f, 0.0f, 0.55f, 0.28f);
            body.SetGravityScale(0.0f);
        }
    }
}
