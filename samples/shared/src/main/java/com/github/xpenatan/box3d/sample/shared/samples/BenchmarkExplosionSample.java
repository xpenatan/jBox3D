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

final class BenchmarkExplosionSample extends AbstractBox3DSample {
    private final List<B3Body> bodies = new ArrayList<B3Body>();
    private int steps;

    BenchmarkExplosionSample() {
        addGroundBox(35.0f);
        for(int y = 0; y < 7; y++) {
            for(int x = 0; x < 7; x++) {
                for(int z = 0; z < 4; z++) {
                    bodies.add(addDynamicSphere(-5.0f + x * 1.5f, 1.0f + y * 1.2f, -3.0f + z * 1.5f, 0.35f));
                }
            }
        }
    }

    @Override
    public void step(float deltaSeconds) {
        steps++;
        if(steps == 10) {
            SamplePortUtil.applyRadialImpulse(bodies, 0.0f, 2.0f, 0.0f, 800.0f);
        }
        super.step(deltaSeconds);
    }
}
