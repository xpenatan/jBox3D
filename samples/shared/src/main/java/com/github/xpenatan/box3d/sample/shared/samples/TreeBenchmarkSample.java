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

final class TreeBenchmarkSample extends AbstractBox3DSample {
    private int rayStep;

    TreeBenchmarkSample() {
        addGroundBox(45.0f);
        for(int i = 0; i < 180; i++) {
            float x = -18.0f + (i % 18) * 2.1f;
            float y = 0.5f + (i / 18) * 0.6f;
            float z = -9.0f + (i % 9) * 2.0f;
            if((i & 1) == 0) {
                addDynamicBox(x, y, z, 0.35f, 0.35f, 0.35f);
            }
            else {
                addDynamicSphere(x, y, z, 0.35f);
            }
        }
    }

    @Override
    public void step(float deltaSeconds) {
        rayStep++;
        B3Vec3 origin = new B3Vec3(-22.0f, 8.0f + (rayStep % 12) * 0.5f, -10.0f);
        B3Vec3 translation = new B3Vec3(44.0f, -2.0f, 20.0f);
        com.github.xpenatan.box3d.B3QueryFilter filter = new com.github.xpenatan.box3d.B3QueryFilter();
        dispose(world().CastRayClosest(origin, translation, filter), filter, translation, origin);
        super.step(deltaSeconds);
    }
}
