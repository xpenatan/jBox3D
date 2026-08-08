package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene from Issues/Slide Twist Off Center Shape. */
final class IssueSlideTwistOffCenterSample extends AbstractBox3DSample {
    IssueSlideTwistOffCenterSample() {
        dispose(addGroundBox(50.0f));

        float angle = 20.0f * (float)Math.PI / 180.0f;
        B3Quat orientation = rotationX(angle);
        addStaticBox(0.0f, 4.0f, 0.0f, 10.0f, 0.5f, 10.0f, orientation);

        float sine = (float)Math.sin(angle);
        float cosine = (float)Math.cos(angle);
        float offsetX = 1.0f;
        float offsetY = 0.5f * cosine - sine;
        float offsetZ = 0.5f * sine + cosine;
        B3Body body = createBody(B3.DynamicBody(), -offsetX, 5.0f - offsetY, -offsetZ, orientation);
        dispose(createBoxShape(body, 1.0f, 0.5f, 1.0f, 1.0f, 0.5f, 1.0f, null,
                1.0f, 0.3f, 0.0f, 0.0f));
        B3Vec3 angularVelocity = new B3Vec3(0.0f, 25.0f * cosine, 25.0f * sine);
        body.SetAngularVelocity(angularVelocity);
        dispose(angularVelocity, body, orientation);
    }
}
