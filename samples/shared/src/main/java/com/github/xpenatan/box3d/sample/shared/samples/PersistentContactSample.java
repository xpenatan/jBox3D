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

final class PersistentContactSample extends AbstractBox3DSample {
    PersistentContactSample() {
        addGroundBox(40.0f);
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), -18.0f, 1.0f, 0.5f, null);
        B3Vec3 velocity = new B3Vec3(4.0f, 0.0f, 0.0f);
        bodyDef.SetLinearVelocity(velocity);
        B3Body body = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = shapeDef(20.0f, 0.6f, 0.0f, 0.01f);
        shapeDef.SetEnableContactEvents(true);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.5f);
        dispose(body.CreateSphereShape(shapeDef, sphere));
        dispose(sphere, center, shapeDef, velocity, bodyDef);
    }
}
