package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3ContactEvents;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3SensorBeginTouchEvent;
import com.github.xpenatan.box3d.B3SensorEvents;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;

final class MoveEventSample extends AbstractBox3DSample {
    MoveEventSample() {
        addGroundBox(40.0f);
        B3Body body = createBody(B3.DynamicBody(), 0.0f, 1.0f, 0.0f, null);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        shapeDef.SetEnableHitEvents(true);
        B3Vec3 offset = new B3Vec3(0.0f, 10.0f, 0.0f);
        B3Hull hull = B3Hull.CreateOffsetBox(0.5f, 10.0f, 0.5f, offset);
        dispose(body.CreateHullShape(shapeDef, hull));
        B3Vec3 angularVelocity = new B3Vec3(0.0f, 0.0f, 1.0f);
        B3Vec3 linearVelocity = new B3Vec3(-10.0f, 0.0f, 0.0f);
        body.SetAngularVelocity(angularVelocity);
        body.SetLinearVelocity(linearVelocity);
        dispose(linearVelocity, angularVelocity, hull, offset, shapeDef);
    }
}
