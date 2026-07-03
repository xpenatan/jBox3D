package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class GyroscopicTorqueSample extends AbstractBox3DSample {
    GyroscopicTorqueSample() {
        addGroundBox(20.0f);

        B3Quat rotation = rotationX(-0.5f * (float)Math.PI);
        B3Vec3 angularVelocity = new B3Vec3(0.01f, 0.01f, 10.0f);
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, 2.0f, 0.0f, rotation);
        bodyDef.SetAngularVelocity(angularVelocity);
        bodyDef.SetGravityScale(0.0f);

        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        B3Body body = world().CreateBody(bodyDef);
        B3Hull cylinder = B3Hull.CreateCylinder(0.6f, 0.15f, 0.0f, 32);
        B3Hull bar = B3Hull.CreateBox(1.0f, 0.05f, 0.1f);
        body.CreateHullShape(shapeDef, cylinder);
        body.CreateHullShape(shapeDef, bar);

        dispose(bar, cylinder, shapeDef, angularVelocity, rotation, bodyDef);
    }
}
