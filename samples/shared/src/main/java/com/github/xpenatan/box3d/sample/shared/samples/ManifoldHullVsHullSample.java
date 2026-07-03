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

final class ManifoldHullVsHullSample extends AbstractBox3DSample {
    ManifoldHullVsHullSample() {
        DiagnosticUtil.addPairBase(this);
        addDynamicBox(-0.5f, 4.0f, 0.0f, 0.75f, 0.75f, 0.75f);
        addDynamicBox(0.6f, 4.2f, 0.0f, 0.75f, 0.75f, 0.75f);
    }
}
