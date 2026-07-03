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

final class BulletVersusStackSample extends AbstractBox3DSample {
    private B3Body bullet;
    private float time;

    BulletVersusStackSample() {
        addGroundBox(50.0f);
        B3Body wallBody = createBody(B3.StaticBody(), 0.0f, -1.0f, 0.0f, null);
        addBoxShape(wallBody, 0.1f, 5.0f, 10.0f, -1.0f, 5.0f, 0.0f, null, 0.0f, 0.6f, 0.0f, 0.0f);
        for(int row = 0; row < 10; row++) {
            addDynamicBox(0.0f, 0.5f + 1.1f * row, 0.0f, 0.5f, 0.5f, 0.5f);
        }
        launch();
    }

    @Override
    public void step(float deltaSeconds) {
        time += deltaSeconds;
        if(time > 2.0f) {
            launch();
            time = 0.0f;
        }
        super.step(deltaSeconds);
    }

    private void launch() {
        if(bullet != null && bullet.IsValid()) {
            bullet.Destroy();
            dispose(bullet);
        }
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 20.5f, 5.5f, 0.0f, null);
        bodyDef.SetIsBullet(true);
        B3Vec3 velocity = new B3Vec3(-500.0f, 0.0f, 0.0f);
        bodyDef.SetLinearVelocity(velocity);
        B3ShapeDef shapeDef = shapeDef(10.0f, 0.6f, 0.0f, 0.0f);
        bullet = world().CreateBody(bodyDef);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.25f);
        dispose(bullet.CreateSphereShape(shapeDef, sphere));
        dispose(sphere, center, shapeDef, velocity, bodyDef);
    }
}
