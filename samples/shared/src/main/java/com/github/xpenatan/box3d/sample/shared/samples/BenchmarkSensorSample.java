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

final class BenchmarkSensorSample extends AbstractBox3DSample {
    BenchmarkSensorSample() {
        addGroundBox(24.0f);
        B3Body sensorBody = createBody(B3.StaticBody(), 0.0f, 5.0f, 0.0f, null);
        B3ShapeDef sensorDef = shapeDef(0.0f, 0.0f, 0.0f, 0.0f);
        sensorDef.SetIsSensor(true);
        sensorDef.SetEnableSensorEvents(true);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sensor = new B3Sphere(center, 5.0f);
        dispose(sensorBody.CreateSphereShape(sensorDef, sensor));
        for(int i = 0; i < 80; i++) {
            addDynamicSphere(-10.0f + (i % 10) * 2.2f, 15.0f + (i / 10) * 1.0f, -4.0f + (i % 4) * 2.0f, 0.35f);
        }
        dispose(sensor, center, sensorDef);
    }
}
