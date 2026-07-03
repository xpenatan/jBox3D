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

final class CharacterRigidBodySample extends AbstractBox3DSample {
    CharacterRigidBodySample() {
        addGroundBox(24.0f);
        SamplePortUtil.addObstacleCourse(this);
        for(int i = 0; i < 4; i++) {
            B3Body character = addDynamicCapsule(-8.0f + i * 4.0f, 5.0f, 0.0f, 0.75f, 0.35f);
            B3MotionLocks locks = motionLocks(false, false, true, true, false, true);
            character.SetMotionLocks(locks);
            dispose(locks);
        }
    }
}
