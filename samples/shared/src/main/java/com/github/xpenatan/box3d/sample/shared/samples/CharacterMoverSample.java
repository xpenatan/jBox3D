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

final class CharacterMoverSample extends AbstractBox3DSample {
    private final List<B3Body> movers = new ArrayList<B3Body>();

    CharacterMoverSample() {
        B3WorldDefBuilder.disableGravity(world());
        addGroundBox(18.0f);
        SamplePortUtil.addObstacleCourse(this);
        for(int i = 0; i < 5; i++) {
            B3Body mover = addDynamicCapsule(-8.0f + i * 4.0f, 3.0f, 0.0f, 0.6f, 0.3f);
            mover.SetGravityScale(0.0f);
            movers.add(mover);
        }
    }

    @Override
    public void step(float deltaSeconds) {
        for(int i = 0; i < movers.size(); i++) {
            B3Vec3 velocity = new B3Vec3((i % 2 == 0 ? 6.0f : -6.0f), 0.0f, 0.0f);
            movers.get(i).SetLinearVelocity(velocity);
            dispose(velocity);
        }
        super.step(deltaSeconds);
    }
}
