package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WheelJointDef;

final class DistanceDebugSample extends AbstractBox3DSample {
    private final B3Shape shape;
    private int steps;

    DistanceDebugSample() {
        addGroundBox(18.0f);
        B3Body body = addDynamicSphere(-2.5f, 5.0f, 0.0f, 0.6f);
        B3Body target = addDynamicBox(2.5f, 5.0f, 0.0f, 0.8f, 0.8f, 0.8f);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        B3Hull hull = B3Hull.CreateRock(0.7f);
        shape = target.CreateHullShape(shapeDef, hull);
        SamplePortUtil.createDistance(this, body, target, 5.0f, 1.5f, 0.9f);
        dispose(hull, shapeDef);
    }

    @Override
    public void step(float deltaSeconds) {
        steps++;
        B3Vec3 query = new B3Vec3((float)Math.sin(steps * 0.05f) * 4.0f, 5.0f, 0.0f);
        dispose(shape.GetClosestPoint(query), query);
        super.step(deltaSeconds);
    }
}
