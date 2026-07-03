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

final class ShapeDistanceSample extends AbstractBox3DSample {
    ShapeDistanceSample() {
        addGroundBox(18.0f);
        B3Body a = addDynamicSphere(-2.0f, 5.0f, 0.0f, 0.75f);
        B3Body b = addDynamicCapsule(2.0f, 5.0f, 0.0f, 0.8f, 0.25f);
        SamplePortUtil.createDistance(this, a, b, 4.0f, 2.0f, 0.8f);
        addStaticBox(0.0f, 2.0f, 0.0f, 0.2f, 1.5f, 2.0f, null);
    }
}
