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

final class OverlapWorldSample extends AbstractBox3DSample {
    OverlapWorldSample() {
        addGroundBox(20.0f);
        B3Body sensorBody = createBody(B3.StaticBody(), 0.0f, 5.0f, 0.0f, null);
        B3ShapeDef sensorDef = shapeDef(0.0f, 0.0f, 0.0f, 0.0f);
        sensorDef.SetIsSensor(true);
        sensorDef.SetEnableSensorEvents(true);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sensor = new B3Sphere(center, 4.0f);
        dispose(sensorBody.CreateSphereShape(sensorDef, sensor));
        DiagnosticUtil.addFallingMix(this, 48, 10.0f);
        dispose(sensor, center, sensorDef);
    }
}
