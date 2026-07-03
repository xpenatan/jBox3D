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

final class FarMeshDropSample extends AbstractBox3DSample {
    FarMeshDropSample() {
        float far = 10000.0f;
        addStaticBox(far, -0.25f, far, 24.0f, 0.25f, 24.0f, null);
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 5; j++) {
                float x = far - 10.0f + i * 2.0f;
                float z = far - 5.0f + j * 2.0f;
                if(((i + j) & 1) == 0) {
                    addDynamicSphere(x, 10.0f + j * 1.5f, z, 0.45f);
                }
                else {
                    addDynamicBox(x, 10.0f + j * 1.5f, z, 0.45f, 0.45f, 0.45f);
                }
            }
        }
    }
}
