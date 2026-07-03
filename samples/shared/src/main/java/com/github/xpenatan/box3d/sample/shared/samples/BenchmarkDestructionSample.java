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

final class BenchmarkDestructionSample extends AbstractBox3DSample {
    private final List<B3Body> targets = new ArrayList<B3Body>();
    private int steps;

    BenchmarkDestructionSample() {
        addGroundBox(40.0f);
        for(int row = 0; row < 10; row++) {
            for(int column = row; column < 18; column++) {
                targets.add(addDynamicBox(-9.0f + column - row * 0.5f, 0.5f + row, 0.0f, 0.45f, 0.45f, 0.45f));
            }
        }
    }

    @Override
    public void step(float deltaSeconds) {
        steps++;
        if(steps == 20) {
            SamplePortUtil.applyRadialImpulse(targets, -5.0f, 3.0f, 0.0f, 650.0f);
        }
        super.step(deltaSeconds);
    }
}
