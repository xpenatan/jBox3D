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

final class LongRayCastSample extends AbstractBox3DSample {
    private int steps;

    LongRayCastSample() {
        addGroundBox(35.0f);
        DiagnosticUtil.addMixedObstacleField(this);
        for(int i = 0; i < 7; i++) {
            B3Quat rotation = rotationZ((-20.0f + i * 8.0f) * (float)Math.PI / 180.0f);
            addStaticBox(-12.0f + i * 4.0f, 7.0f, 0.0f, 0.05f, 6.0f, 0.05f, rotation);
            dispose(rotation);
        }
    }

    @Override
    public void step(float deltaSeconds) {
        steps++;
        B3Vec3 origin = new B3Vec3(-28.0f, 6.0f + (steps % 20) * 0.2f, -8.0f);
        B3Vec3 translation = new B3Vec3(56.0f, 0.0f, 16.0f);
        B3QueryFilter filter = new B3QueryFilter();
        dispose(world().CastRayClosest(origin, translation, filter), filter, translation, origin);
        super.step(deltaSeconds);
    }
}
