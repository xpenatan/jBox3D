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

final class BounceHouseSample extends AbstractBox3DSample {
    BounceHouseSample() {
        addGroundBox(10.0f);

        B3Body walls = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        addBoxShape(walls, 0.1f, 5.0f, 10.0f, 10.0f, 5.0f, 0.0f, null, 0.0f, 0.0f, 1.0f, 0.0f);
        addBoxShape(walls, 0.1f, 5.0f, 10.0f, -10.0f, 5.0f, 0.0f, null, 0.0f, 0.0f, 1.0f, 0.0f);
        addBoxShape(walls, 10.0f, 5.0f, 0.1f, 0.0f, 5.0f, -10.0f, null, 0.0f, 0.0f, 1.0f, 0.0f);
        addBoxShape(walls, 10.0f, 5.0f, 0.1f, 0.0f, 5.0f, 10.0f, null, 0.0f, 0.0f, 1.0f, 0.0f);

        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), -8.0f, 4.0f, 0.0f, null);
        bodyDef.SetGravityScale(0.0f);
        B3Vec3 velocity = new B3Vec3(120.0f, 0.0f, 120.0f);
        bodyDef.SetLinearVelocity(velocity);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.0f, 1.0f, 0.0f);
        B3Body body = world().CreateBody(bodyDef);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.5f);
        dispose(body.CreateSphereShape(shapeDef, sphere));
        dispose(sphere, center, shapeDef, velocity, bodyDef);
    }
}
