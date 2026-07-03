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

final class MeshHollowBoxSample extends AbstractBox3DSample {
    MeshHollowBoxSample() {
        addGroundBox(20.0f);
        addStaticBox(-6.0f, 3.0f, 0.0f, 0.25f, 3.0f, 4.0f, null);
        addStaticBox(6.0f, 3.0f, 0.0f, 0.25f, 3.0f, 4.0f, null);
        addStaticBox(0.0f, 6.0f, 0.0f, 6.0f, 0.25f, 4.0f, null);
        DiagnosticUtil.addFallingMix(this, 32, 10.0f);
    }
}
