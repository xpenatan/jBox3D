package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;

final class ThinWallSample extends AbstractBox3DSample {
    ThinWallSample() {
        addGroundBox(40.0f);

        B3Quat wallRotation = rotationX(90.0f * (float)Math.PI / 180.0f);
        addStaticBox(0.0f, 10.0f, 0.0f, 10.0f, 0.1f, 10.0f, wallRotation);
        dispose(wallRotation);

        B3Vec3 linearVelocity = new B3Vec3(0.0f, 0.0f, -180.0f);
        B3Vec3 angularVelocity = new B3Vec3(20.0f, 0.0f, 0.0f);
        B3Body sphereBody = createFastBody(-5.0f, 10.0f, 20.0f, linearVelocity, angularVelocity);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.1f);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.1f);
        dispose(sphereBody.CreateSphereShape(shapeDef, sphere));

        angularVelocity.Set(20.0f, -5.0f, 0.0f);
        B3Body capsuleBody = createFastBody(0.0f, 10.0f, 20.0f, linearVelocity, angularVelocity);
        B3Vec3 center1 = new B3Vec3(-0.3f, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.3f, 0.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, 0.1f);
        dispose(capsuleBody.CreateCapsuleShape(shapeDef, capsule));

        angularVelocity.Set(20.0f, 5.0f, 0.0f);
        B3Body boxBody = createFastBody(5.0f, 10.0f, 20.0f, linearVelocity, angularVelocity);
        B3Hull box = B3Hull.CreateBox(0.4f, 0.1f, 0.1f);
        dispose(boxBody.CreateHullShape(shapeDef, box));

        dispose(box, capsule, center2, center1, sphere, center, shapeDef, angularVelocity, linearVelocity);
    }

    private B3Body createFastBody(float x, float y, float z, B3Vec3 linearVelocity, B3Vec3 angularVelocity) {
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), x, y, z, null);
        bodyDef.SetLinearVelocity(linearVelocity);
        bodyDef.SetAngularVelocity(angularVelocity);
        B3Body body = world().CreateBody(bodyDef);
        dispose(bodyDef);
        return body;
    }
}
