package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3DistanceJointDef;
import com.github.xpenatan.box3d.B3FilterJointDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3MotorJointDef;
import com.github.xpenatan.box3d.B3ParallelJointDef;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3RevoluteJointDef;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WeldJointDef;
import com.github.xpenatan.box3d.B3WheelJointDef;

final class BallAndChainSample extends AbstractBox3DSample {
    private static final int LINK_COUNT = 32;

    BallAndChainSample() {
        B3Body groundBody = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);

        float linkRadius = 0.125f;
        float linkExtent = 0.5f;
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        B3Vec3 center1 = new B3Vec3(-linkExtent, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(linkExtent, 0.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, linkRadius);

        B3Body parent = groundBody;
        B3SphericalJointDef jointDef = new B3SphericalJointDef();
        setLocalPositionA(jointDef, 0.0f, 0.0f, 0.0f);
        setLocalPositionB(jointDef, -linkExtent, 0.0f, 0.0f);
        jointDef.SetEnableMotor(true);
        jointDef.SetMaxMotorTorque(10.0f);

        for(int i = 0; i < LINK_COUNT; i++) {
            B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), (1.0f + 2.0f * i) * linkExtent, 0.0f, 0.0f, null);
            B3Body child = world().CreateBody(bodyDef);
            dispose(child.CreateCapsuleShape(shapeDef, capsule));

            jointDef.SetBodyIdA(parent.GetId());
            jointDef.SetBodyIdB(child.GetId());
            dispose(world().CreateSphericalJoint(jointDef));

            setLocalPositionA(jointDef, linkExtent, 0.0f, 0.0f);
            parent = child;
            dispose(bodyDef);
        }

        float sphereRadius = 2.0f;
        B3BodyDef sphereDef = bodyDef(B3.DynamicBody(),
                (1.0f + 2.0f * LINK_COUNT) * linkExtent + sphereRadius - linkExtent, 0.0f, 0.0f, null);
        B3Body sphereBody = world().CreateBody(sphereDef);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, sphereRadius);
        dispose(sphereBody.CreateSphereShape(shapeDef, sphere));
        jointDef.SetBodyIdA(parent.GetId());
        jointDef.SetBodyIdB(sphereBody.GetId());
        setLocalPositionB(jointDef, -sphereRadius, 0.0f, 0.0f);
        dispose(world().CreateSphericalJoint(jointDef));

        dispose(sphere, center, sphereDef, jointDef, capsule, center2, center1, shapeDef);
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
}
