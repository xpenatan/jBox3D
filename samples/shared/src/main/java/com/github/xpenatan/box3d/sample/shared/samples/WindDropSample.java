package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3FilterJointDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Joint;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3RevoluteJointDef;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3Vec3;

final class WindDropSample extends AbstractBox3DSample {
    private final B3Shape shape;

    WindDropSample() {
        addGroundBox(15.0f);

        B3Quat rotation = rotationX(0.25f);
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, 10.0f, 0.0f, rotation);
        bodyDef.SetGravityScale(0.5f);
        B3Body body = world().CreateBody(bodyDef);
        shape = createBoxShape(body, 0.4f, 0.01f, 0.4f, 2.0f, 0.6f, 0.0f, 0.0f);
        dispose(rotation, bodyDef);
    }

    @Override
    public void step(float deltaSeconds) {
        B3Vec3 wind = new B3Vec3(0.0f, 0.0f, 0.0f);
        shape.ApplyWind(wind, 1.0f, 4.0f, 10.0f, true);
        dispose(wind);
        super.step(deltaSeconds);
    }
}
