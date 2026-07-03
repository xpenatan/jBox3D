package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

final class BodyTypeSample extends AbstractBox3DSample {
    private final B3Body platform;
    private final B3Body secondAttachment;
    private final B3Body secondPayload;
    private final B3Body touchingBody;
    private final B3Body floatingBody;
    private int bodyType = B3.DynamicBody();
    private float typeTimer;
    private float enableTimer;
    private boolean enabled = true;

    BodyTypeSample() {
        addGroundBox(20.0f);

        addDynamicBox(-2.0f, 3.0f, 0.0f, 0.5f, 2.0f, 0.5f);
        secondAttachment = addDynamicBox(3.0f, 3.0f, 0.0f, 0.5f, 2.0f, 0.5f);

        platform = createBody(bodyType, -4.0f, 5.0f, 0.0f, null);
        B3Quat platformRotation = rotationZ(0.5f * (float)Math.PI);
        addBoxShape(platform, 0.5f, 4.0f, 0.5f, 4.0f, 0.0f, 0.0f, platformRotation, 2.0f, 0.6f, 0.0f,
                0.0f);
        dispose(platformRotation);

        addDynamicBox(-3.0f, 8.0f, 0.0f, 0.75f, 0.75f, 0.75f, null, 2.0f, 0.6f, 0.0f, 0.0f);
        secondPayload = addDynamicBox(2.0f, 8.0f, 0.0f, 0.75f, 0.75f, 0.75f, null, 2.0f, 0.6f, 0.0f, 0.0f);
        touchingBody = createBody(bodyType, 8.0f, 0.2f, 0.0f, null);
        addCapsuleShape(touchingBody, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.25f, 2.0f, 0.6f, 0.0f,
                0.0f);
        floatingBody = addDynamicSphere(-8.0f, 12.0f, 0.0f, 0.25f, 2.0f, 0.6f, 0.0f, 0.0f);
        floatingBody.SetGravityScale(0.0f);
    }

    @Override
    public void step(float deltaSeconds) {
        typeTimer += deltaSeconds;
        enableTimer += deltaSeconds;

        if(typeTimer > 5.0f) {
            typeTimer = 0.0f;
            if(bodyType == B3.DynamicBody()) {
                setBodyType(B3.KinematicBody());
            }
            else if(bodyType == B3.KinematicBody()) {
                setBodyType(B3.StaticBody());
            }
            else {
                setBodyType(B3.DynamicBody());
            }
        }

        if(enableTimer > 8.0f) {
            enableTimer = 0.0f;
            enabled = !enabled;
            setEnabled(secondPayload, enabled);
            setEnabled(floatingBody, enabled);
        }

        if(bodyType == B3.KinematicBody()) {
            B3Vec3 position = platform.GetPosition();
            B3Vec3 velocity = platform.GetLinearVelocity();
            if((position.GetX() < -14.0f && velocity.GetX() < 0.0f)
                    || (position.GetX() > 6.0f && velocity.GetX() > 0.0f)) {
                velocity.SetX(-velocity.GetX());
                platform.SetLinearVelocity(velocity);
            }
            dispose(velocity, position);
        }

        super.step(deltaSeconds);
    }

    private void setBodyType(int type) {
        bodyType = type;
        setType(platform, type);
        setType(secondAttachment, type);
        setType(secondPayload, type);
        setType(touchingBody, type);
        setType(floatingBody, type);
        if(type == B3.KinematicBody()) {
            B3Vec3 velocity = new B3Vec3(-3.0f, 0.0f, 0.0f);
            platform.SetLinearVelocity(velocity);
            dispose(velocity);
        }
    }

    private void setType(B3Body body, int type) {
        if(body.IsValid()) {
            body.SetType(type);
        }
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
