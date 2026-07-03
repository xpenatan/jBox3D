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

final class CharacterMoverOverlapSample extends AbstractBox3DSample {
    CharacterMoverOverlapSample() {
        addGroundBox(16.0f);
        for(int i = 0; i < 8; i++) {
            addStaticBox(-6.0f + i * 1.7f, 0.75f, 0.0f, 0.35f, 0.75f, 1.5f, null);
        }
        for(int i = 0; i < 10; i++) {
            addDynamicCapsule(-6.0f + i * 1.3f, 4.0f, 0.0f, 0.65f, 0.3f);
        }
    }
}
