package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class BodyCastSample extends AbstractBox3DSample {
    BodyCastSample() {
        B3BodyDef bodyDef = bodyDef(B3.KinematicBody(), 5.0f, 5.0f, 0.0f, null);
        B3Vec3 angularVelocity = new B3Vec3(0.1f, -0.1f, 0.1f);
        bodyDef.SetAngularVelocity(angularVelocity);
        B3Body body = world().CreateBody(bodyDef);
        B3Hull cylinder = B3Hull.CreateCylinder(2.0f, 0.5f, 0.0f, 16);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(body.CreateHullShape(shapeDef, cylinder));
        dispose(shapeDef, cylinder, angularVelocity, bodyDef);
    }
}
