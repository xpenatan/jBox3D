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

final class CastWorldSample extends AbstractBox3DSample {
    CastWorldSample() {
        addGroundBox(26.0f);
        DiagnosticUtil.addMixedObstacleField(this);
        B3Vec3 velocity = new B3Vec3(70.0f, 0.0f, 0.0f);
        B3Body cast = addDynamicBox(-17.0f, 6.5f, 0.0f, 0.6f, 0.3f, 0.6f, null, 2.0f, 0.5f,
                0.0f, 0.0f, velocity, null);
        cast.SetBullet(true);
        dispose(velocity);
    }
}
