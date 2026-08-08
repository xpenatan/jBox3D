package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class ContinuousIsFastSample extends AbstractBox3DSample {
    ContinuousIsFastSample() {
        addGroundBox(40.0f);
        B3Hull box = B3Hull.CreateBox(0.5f, 10.0f, 0.5f);
        B3ShapeDef shapeDef = new B3ShapeDef();
        createSpinner(-12.0f, 0.0f, 0.0f, 4.0f, box, shapeDef);
        createSpinner(0.0f, 0.0f, 4.0f, 0.0f, box, shapeDef);
        createSpinner(12.0f, 4.0f, 0.0f, 0.0f, box, shapeDef);
        dispose(shapeDef, box);
    }

    private void createSpinner(float x, float angularX, float angularY, float angularZ, B3Hull box,
            B3ShapeDef shapeDef) {
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), x, 20.0f, 0.0f, null);
        bodyDef.SetGravityScale(0.0f);
        B3Vec3 angularVelocity = new B3Vec3(angularX, angularY, angularZ);
        bodyDef.SetAngularVelocity(angularVelocity);
        B3Body body = world().CreateBody(bodyDef);
        dispose(body.CreateHullShape(shapeDef, box));
        dispose(angularVelocity, bodyDef);
    }
}
