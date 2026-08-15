package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class GyroscopicPrecessionSample extends AbstractBox3DSample {
    GyroscopicPrecessionSample() {
        addGroundBox(40.0f);

        int segmentCount = 7;
        float radius = 2.0f;
        float height = 2.0f;
        float[][] points = new float[segmentCount + 1][3];
        float deltaAngle = 2.0f * (float)Math.PI / segmentCount;
        for(int i = 0; i < segmentCount; i++) {
            points[i][0] = radius * (float)Math.cos(i * deltaAngle);
            points[i][1] = height;
            points[i][2] = radius * (float)Math.sin(i * deltaAngle);
        }
        B3Hull hull = createHull(points);
        B3ShapeDef shapeDef = new B3ShapeDef();
        float tilt = 15.0f * (float)Math.PI / 180.0f;
        B3Quat rotation = rotationZ(tilt);
        B3Vec3 localAngularVelocity = new B3Vec3(0.0f, 75.0f, 0.0f);
        B3Vec3 angularVelocity = rotatedVector(rotation, localAngularVelocity);

        int count = 8;
        float separation = 6.0f;
        for(int x = 0; x < count; x++) {
            for(int z = 0; z < count; z++) {
                B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), (x - count / 2) * separation, height,
                        (z - count / 2) * separation, rotation);
                bodyDef.SetAllowFastRotation(true);
                B3Body body = world().CreateBody(bodyDef);
                dispose(body.CreateHullShape(shapeDef, hull));
                body.SetAngularVelocity(angularVelocity);
                dispose(bodyDef);
            }
        }
        dispose(angularVelocity, localAngularVelocity, rotation, shapeDef, hull);
    }
}
