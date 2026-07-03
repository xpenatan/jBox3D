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

final class WindSample extends AbstractBox3DSample {
    private static final int COUNT = 10;

    private final B3Shape[] shapes = new B3Shape[COUNT];
    private float noiseX;
    private float noiseY;
    private float noiseZ;

    WindSample() {
        addGroundBox(20.0f);

        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3SphericalJointDef jointDef = new B3SphericalJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        setLocalPositionA(jointDef, 0.0f, 2.0f, 0.0f);
        jointDef.SetDrawScale(0.1f);

        float radius = 0.1f;
        for(int i = 0; i < COUNT; i++) {
            B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), (2.0f * i + 1.0f) * radius, 2.0f, 0.0f, null);
            bodyDef.SetGravityScale(0.5f);
            bodyDef.SetEnableSleep(false);
            B3Body body = world().CreateBody(bodyDef);
            shapes[i] = createBoxShape(body, 1.25f * radius, 0.75f * radius, 0.125f * radius, 20.0f, 0.6f,
                    0.0f, 0.0f);

            jointDef.SetBodyIdB(body.GetId());
            setLocalPositionB(jointDef, -radius, 0.0f, 0.0f);
            dispose(world().CreateSphericalJoint(jointDef));

            jointDef.SetBodyIdA(body.GetId());
            setLocalPositionA(jointDef, radius, 0.0f, 0.0f);
            dispose(bodyDef);
        }

        dispose(jointDef);
    }

    @Override
    public void step(float deltaSeconds) {
        float windX = 6.0f * (1.0f + noiseX);
        float windY = 6.0f * noiseY;
        float windZ = 6.0f * noiseZ;
        B3Vec3 wind = new B3Vec3(windX, windY, windZ);
        for(B3Shape shape : shapes) {
            shape.ApplyWind(wind, 1.0f, 0.75f, 10.0f, true);
        }
        dispose(wind);

        noiseX = lerp(noiseX, randomNoise(), 0.05f);
        noiseY = lerp(noiseY, randomNoise(), 0.05f);
        noiseZ = lerp(noiseZ, randomNoise(), 0.05f);
        super.step(deltaSeconds);
    }

    private void setLocalPositionA(B3SphericalJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionA(position);
        dispose(position);
    }

    private void setLocalPositionB(B3SphericalJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionB(position);
        dispose(position);
    }

    private float lerp(float start, float end, float alpha) {
        return start + alpha * (end - start);
    }

    private float randomNoise() {
        return (float)(Math.random() * 0.6 - 0.3);
    }
}
