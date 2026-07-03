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

final class SensorVisitSample extends AbstractBox3DSample {
    private final long sensorShapeId;

    SensorVisitSample() {
        B3BodyDef visitorDef = bodyDef(B3.DynamicBody(), 0.0f, 12.5f, 0.0f, null);
        B3Body visitor = world().CreateBody(visitorDef);
        B3ShapeDef visitorShapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        visitorShapeDef.SetEnableSensorEvents(true);
        B3Hull visitorHull = B3Hull.CreateBox(0.5f, 0.5f, 0.5f);
        dispose(visitor.CreateHullShape(visitorShapeDef, visitorHull));

        B3Body sensorBody = createBody(B3.KinematicBody(), 0.0f, 2.0f, 0.0f, null);
        B3ShapeDef sensorDef = shapeDef(0.0f, 0.6f, 0.0f, 0.0f);
        sensorDef.SetIsSensor(true);
        sensorDef.SetEnableSensorEvents(true);
        B3Hull sensorHull = B3Hull.CreateBox(2.0f, 2.0f, 2.0f);
        B3Shape sensorShape = sensorBody.CreateHullShape(sensorDef, sensorHull);
        sensorShapeId = sensorShape.GetId();
        dispose(sensorShape, sensorHull, sensorDef, visitorHull, visitorShapeDef, visitorDef);
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        B3SensorEvents events = world().GetSensorEvents();
        for(int i = 0; i < events.GetBeginCount(); i++) {
            B3SensorBeginTouchEvent event = events.GetBeginEvent(i);
            if(event.GetSensorShapeId() == sensorShapeId) {
                B3Shape visitorShape = new B3Shape(event.GetVisitorShapeId());
                B3Body visitorBody = new B3Body(visitorShape.GetBodyId());
                visitorBody.Destroy();
                dispose(visitorBody, visitorShape, event);
                break;
            }
            dispose(event);
        }
        dispose(events);
    }
}
