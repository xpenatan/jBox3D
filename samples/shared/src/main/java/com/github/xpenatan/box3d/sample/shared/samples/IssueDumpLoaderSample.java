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

final class IssueDumpLoaderSample extends AbstractBox3DSample {
    IssueDumpLoaderSample() {
        addGroundBox(20.0f);
        for(int i = 0; i < 10; i++) {
            B3Body body = addDynamicBox(-6.0f + i * 1.4f, 5.0f + i * 0.25f, 0.0f, 0.4f, 0.4f, 0.4f);
            body.SetAwake((i & 1) == 0);
        }
    }
}
