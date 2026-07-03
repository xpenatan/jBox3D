package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WeldJointDef;

final class DisableSample extends AbstractBox3DSample {
    private static final int LINK_COUNT = 4;

    private final B3Body[] bodies = new B3Body[LINK_COUNT];
    private final B3Body ball;
    private float toggleTimer;
    private boolean linkEnabled = true;
    private boolean ballEnabled = true;

    DisableSample() {
        addGroundBox(20.0f);

        float linkRadius = 0.1f;
        float linkLength = 5.0f * linkRadius;
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        B3Vec3 center1 = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, -linkLength, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, linkRadius);

        B3Body parent = null;
        for(int link = 0; link < LINK_COUNT; link++) {
            int bodyType = parent == null ? B3.KinematicBody() : B3.DynamicBody();
            B3BodyDef bodyDef = bodyDef(bodyType, 0.0f, (LINK_COUNT - link) * linkLength + 1.0f, 0.0f, null);
            B3Body child = world().CreateBody(bodyDef);
            dispose(child.CreateCapsuleShape(shapeDef, capsule));
            bodies[link] = child;

            if(parent != null) {
                B3WeldJointDef jointDef = new B3WeldJointDef();
                jointDef.SetBodyIdA(parent.GetId());
                jointDef.SetBodyIdB(child.GetId());
                setLocalPositionA(jointDef, 0.0f, -linkLength, 0.0f);
                jointDef.SetAngularHertz(10.0f);
                jointDef.SetAngularDampingRatio(1.0f);
                dispose(world().CreateWeldJoint(jointDef));
                dispose(jointDef);
            }

            parent = child;
            dispose(bodyDef);
        }

        B3BodyDef ballDef = bodyDef(B3.DynamicBody(), 3.0f, 3.0f, 0.0f, null);
        ball = world().CreateBody(ballDef);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.5f);
        dispose(ball.CreateSphereShape(shapeDef, sphere));
        dispose(sphere, center, ballDef, capsule, center2, center1, shapeDef);
    }

    @Override
    public void step(float deltaSeconds) {
        toggleTimer += deltaSeconds;
        if(toggleTimer > 3.0f) {
            toggleTimer = 0.0f;
            linkEnabled = !linkEnabled;
            setEnabled(bodies[2], linkEnabled);
            if(linkEnabled) {
                ballEnabled = !ballEnabled;
                setEnabled(ball, ballEnabled);
            }
        }

        B3Vec3 impulse = new B3Vec3(0.0f, 0.1f, 0.0f);
        bodies[2].ApplyLinearImpulseToCenter(impulse, true);
        dispose(impulse);

        super.step(deltaSeconds);
    }

    private void setLocalPositionA(B3WeldJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionA(position);
        dispose(position);
    }

    private void setEnabled(B3Body body, boolean enable) {
        if(enable) {
            body.Enable();
        }
        else {
            body.Disable();
        }
    }
}
