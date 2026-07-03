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

final class ManifoldHullVsSphereSample extends AbstractBox3DSample {
    ManifoldHullVsSphereSample() {
        DiagnosticUtil.addPairBase(this);
        addDynamicBox(-0.4f, 4.0f, 0.0f, 0.8f, 0.8f, 0.8f);
        addDynamicSphere(0.8f, 4.3f, 0.0f, 0.6f);
    }
}
