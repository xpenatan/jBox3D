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

final class RayCurtainSample extends AbstractBox3DSample {
    RayCurtainSample() {
        addGroundBox(32.0f);
        DiagnosticUtil.addMixedObstacleField(this);
        for(int i = 0; i < 24; i++) {
            float z = -6.0f + i * 0.5f;
            addStaticBox(0.0f, 7.0f, z, 18.0f, 0.015f, 0.015f, null);
        }
    }
}
