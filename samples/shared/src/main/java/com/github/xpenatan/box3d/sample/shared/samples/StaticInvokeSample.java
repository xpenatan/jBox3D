package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;

final class StaticInvokeSample extends AbstractBox3DSample {
    private B3Body staticBody;
    private int steps;

    StaticInvokeSample() {
        addGroundBox(20.0f);
        addDynamicSphere(0.25f, 1.0f, 0.0f, 0.5f, 1.0f, 0.6f, 0.0f, 0.2f);
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        steps++;
        if(steps == 20 && staticBody == null) {
            createStaticSphere();
        }
    }

    private void createStaticSphere() {
        B3BodyDef bodyDef = bodyDef(B3.StaticBody(), 0.0f, 0.5f, 0.0f, null);
        B3ShapeDef shapeDef = shapeDef(0.0f, 0.6f, 0.0f, 0.0f);
        shapeDef.SetInvokeContactCreation(false);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.5f);
        staticBody = world().CreateBody(bodyDef);
        staticBody.CreateSphereShape(shapeDef, sphere);
        dispose(sphere, center, shapeDef, bodyDef);
    }
}
