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

final class SensorHitsSample extends AbstractBox3DSample {
    private B3Body kinematicSensorBody;
    private B3Body projectile;
    private float time;

    SensorHitsSample() {
        addGroundBox(10.0f);
        B3Body wall = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        addBoxShape(wall, 0.1f, 5.0f, 5.0f, 10.0f, 5.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);

        B3Quat sensorRotation = rotationZ(0.5f * (float)Math.PI);
        B3Body staticSensor = createBody(B3.StaticBody(), -4.0f, 6.0f, 0.0f, sensorRotation);
        addSensorBox(staticSensor, 2.5f, 0.1f, 2.5f);

        kinematicSensorBody = createBody(B3.KinematicBody(), 0.0f, 6.0f, 0.0f, sensorRotation);
        B3Vec3 velocity = new B3Vec3(0.5f, 0.0f, 0.0f);
        kinematicSensorBody.SetLinearVelocity(velocity);
        addSensorBox(kinematicSensorBody, 2.5f, 0.1f, 2.5f);

        B3Body dynamicSensor = createBody(B3.DynamicBody(), 4.0f, 1.0f, 0.0f, null);
        B3ShapeDef sensorDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        sensorDef.SetIsSensor(true);
        sensorDef.SetEnableSensorEvents(true);
        B3Vec3 c1 = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Vec3 c2 = new B3Vec3(0.0f, 9.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(c1, c2, 0.1f);
        dispose(dynamicSensor.CreateCapsuleShape(sensorDef, capsule));
        dispose(capsule, c2, c1, sensorDef, velocity, sensorRotation);

        launch();
    }

    @Override
    public void step(float deltaSeconds) {
        time += deltaSeconds;
        B3Vec3 p = kinematicSensorBody.GetPosition();
        if(p.GetX() > 1.0f) {
            B3Vec3 velocity = new B3Vec3(-0.5f, 0.0f, 0.0f);
            kinematicSensorBody.SetLinearVelocity(velocity);
            dispose(velocity);
        }
        else if(p.GetX() < -1.0f) {
            B3Vec3 velocity = new B3Vec3(0.5f, 0.0f, 0.0f);
            kinematicSensorBody.SetLinearVelocity(velocity);
            dispose(velocity);
        }
        dispose(p);

        if(time > 2.0f) {
            launch();
            time = 0.0f;
        }
        super.step(deltaSeconds);
        B3SensorEvents events = world().GetSensorEvents();
        dispose(events);
    }

    private void addSensorBox(B3Body body, float halfWidth, float halfHeight, float halfDepth) {
        B3ShapeDef sensorDef = shapeDef(0.0f, 0.6f, 0.0f, 0.0f);
        sensorDef.SetIsSensor(true);
        sensorDef.SetEnableSensorEvents(true);
        B3Hull hull = B3Hull.CreateBox(halfWidth, halfHeight, halfDepth);
        dispose(body.CreateHullShape(sensorDef, hull));
        dispose(hull, sensorDef);
    }

    private void launch() {
        if(projectile != null && projectile.IsValid()) {
            projectile.Destroy();
            dispose(projectile);
        }
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), -26.7f, 6.0f, 0.0f, null);
        bodyDef.SetIsBullet(true);
        B3Vec3 velocity = new B3Vec3(240.0f, 0.0f, 0.0f);
        bodyDef.SetLinearVelocity(velocity);
        projectile = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.8f, 0.0f, 0.01f);
        shapeDef.SetEnableSensorEvents(true);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.25f);
        dispose(projectile.CreateSphereShape(shapeDef, sphere));
        dispose(sphere, center, shapeDef, velocity, bodyDef);
    }
}
