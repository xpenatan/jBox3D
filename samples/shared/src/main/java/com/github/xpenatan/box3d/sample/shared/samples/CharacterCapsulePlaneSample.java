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

final class CharacterCapsulePlaneSample extends AbstractBox3DSample {
    CharacterCapsulePlaneSample() {
        B3Quat slope = rotationZ(-0.25f);
        addStaticBox(0.0f, 0.5f, 0.0f, 18.0f, 0.2f, 5.0f, slope);
        dispose(slope);
        for(int i = 0; i < 8; i++) {
            addDynamicCapsule(-8.0f + i * 2.2f, 5.0f + i * 0.3f, 0.0f, 0.7f, 0.32f);
        }
    }
}
