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

final class HitEventSample extends AbstractBox3DSample {
    private int hitCount;
    private int lastSpawnHitCount;

    HitEventSample() {
        addGroundBox(80.0f);

        float radius = 0.75f;
        float y = radius;
        float length = 1.5f;
        float offset = 0.05f;
        int shapeCount = 18;
        int shapesPerBody = 3;
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.2f);
        shapeDef.SetEnableHitEvents(true);
        shapeDef.SetUpdateBodyMass(false);

        B3Vec3 origin = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Body body = createBody(B3.DynamicBody(), 0.0f, 0.0f, 0.0f, null);
        for(int i = 0; i < shapeCount; i++) {
            B3Vec3 center1 = new B3Vec3(offset, y, 0.0f);
            B3Vec3 center2 = new B3Vec3(0.0f, y + length, -offset);
            B3Capsule capsule = new B3Capsule(center1, center2, radius);
            dispose(body.CreateCapsuleShape(shapeDef, capsule));
            dispose(capsule, center2, center1);

            if((i + 1) % shapesPerBody == 0 || i == shapeCount - 1) {
                body.ApplyMassFromShapes();
                B3Vec3 angularVelocity = new B3Vec3(0.0f, 0.0f, -0.5f);
                B3Vec3 linearVelocity = new B3Vec3(-0.5f * y, 0.0f, 0.0f);
                body.SetAngularVelocity(angularVelocity);
                body.SetLinearVelocity(linearVelocity);
                dispose(linearVelocity, angularVelocity);
                if(i < shapeCount - 1) {
                    body = createBody(B3.DynamicBody(), 0.0f, 0.0f, 0.0f, null);
                }
            }

            y += length + 2.0f * radius;
            radius *= 0.95f;
            offset = -offset;
        }
        dispose(origin, shapeDef);
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        B3ContactEvents events = world().GetContactEvents();
        hitCount += events.GetHitCount();
        if(hitCount > 0 && hitCount % 20 == 0 && hitCount != lastSpawnHitCount) {
            addDynamicSphere(-6.0f + (hitCount % 8), 10.0f, 0.0f, 0.15f);
            lastSpawnHitCount = hitCount;
        }
        dispose(events);
    }
}
