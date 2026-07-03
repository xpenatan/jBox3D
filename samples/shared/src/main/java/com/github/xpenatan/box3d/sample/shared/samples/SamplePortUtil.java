package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3DistanceJointDef;
import com.github.xpenatan.box3d.B3Filter;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3Vec3;

import java.util.ArrayList;
import java.util.List;

final class SamplePortUtil {
    private SamplePortUtil() {
    }

    static void createDistance(AbstractBox3DSample sample, B3Body bodyA, B3Body bodyB, float length, float hertz,
            float damping) {
        B3DistanceJointDef jointDef = new B3DistanceJointDef();
        jointDef.SetBodyIdA(bodyA.GetId());
        jointDef.SetBodyIdB(bodyB.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, 0.0f, 0.0f, 0.0f);
        JointSampleUtil.setLocalPositionB(jointDef, 0.0f, 0.0f, 0.0f);
        jointDef.SetLength(length);
        jointDef.SetHertz(hertz);
        jointDef.SetDampingRatio(damping);
        jointDef.SetEnableSpring(true);
        AbstractBox3DSample.dispose(sample.world().CreateDistanceJoint(jointDef), jointDef);
    }

    static void createHangingChain(AbstractBox3DSample sample, float x, float y, float z, int linkCount) {
        B3Body anchor = sample.createBody(B3.StaticBody(), x, y, z, null);
        B3Body previous = anchor;
        B3SphericalJointDef jointDef = new B3SphericalJointDef();
        jointDef.SetEnableSpring(true);
        jointDef.SetHertz(4.0f);
        jointDef.SetDampingRatio(0.7f);
        B3ShapeDef shapeDef = sample.shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        B3Vec3 c1 = new B3Vec3(0.0f, -0.35f, 0.0f);
        B3Vec3 c2 = new B3Vec3(0.0f, 0.35f, 0.0f);
        B3Capsule capsule = new B3Capsule(c1, c2, 0.12f);
        for(int i = 0; i < linkCount; i++) {
            B3Body body = sample.createBody(B3.DynamicBody(), x, y - 0.7f * (i + 1), z, null);
            AbstractBox3DSample.dispose(body.CreateCapsuleShape(shapeDef, capsule));
            jointDef.SetBodyIdA(previous.GetId());
            jointDef.SetBodyIdB(body.GetId());
            JointSampleUtil.setLocalPositionA(jointDef, 0.0f, previous == anchor ? 0.0f : -0.35f, 0.0f);
            JointSampleUtil.setLocalPositionB(jointDef, 0.0f, 0.35f, 0.0f);
            AbstractBox3DSample.dispose(sample.world().CreateSphericalJoint(jointDef));
            previous = body;
        }
        AbstractBox3DSample.dispose(capsule, c2, c1, shapeDef, jointDef);
    }

    static void createSimpleRagdoll(AbstractBox3DSample sample, float x, float y, float z) {
        B3Body torso = sample.addDynamicBox(x, y, z, 0.45f, 0.9f, 0.25f);
        B3Body head = sample.addDynamicSphere(x, y + 1.35f, z, 0.35f);
        B3Body leftArm = sample.addDynamicCapsule(x - 0.85f, y + 0.35f, z, 0.45f, 0.16f);
        B3Body rightArm = sample.addDynamicCapsule(x + 0.85f, y + 0.35f, z, 0.45f, 0.16f);
        B3Body leftLeg = sample.addDynamicCapsule(x - 0.28f, y - 1.15f, z, 0.55f, 0.18f);
        B3Body rightLeg = sample.addDynamicCapsule(x + 0.28f, y - 1.15f, z, 0.55f, 0.18f);
        connect(sample, torso, head, 0.0f, 0.85f, 0.0f, 0.0f, -0.35f, 0.0f);
        connect(sample, torso, leftArm, -0.45f, 0.45f, 0.0f, 0.0f, 0.45f, 0.0f);
        connect(sample, torso, rightArm, 0.45f, 0.45f, 0.0f, 0.0f, 0.45f, 0.0f);
        connect(sample, torso, leftLeg, -0.25f, -0.85f, 0.0f, 0.0f, 0.55f, 0.0f);
        connect(sample, torso, rightLeg, 0.25f, -0.85f, 0.0f, 0.0f, 0.55f, 0.0f);
    }

    static void addObstacleCourse(AbstractBox3DSample sample) {
        for(int i = 0; i < 7; i++) {
            sample.addStaticBox(-9.0f + i * 3.0f, 0.5f + (i % 3) * 0.3f, 0.0f, 0.75f, 0.5f, 2.0f, null);
        }
        B3Quat slope = sample.rotationZ(0.25f);
        sample.addStaticBox(6.0f, 1.8f, 0.0f, 3.0f, 0.15f, 2.0f, slope);
        AbstractBox3DSample.dispose(slope);
    }

    static void applyRadialImpulse(List<B3Body> bodies, float centerX, float centerY, float centerZ, float impulseScale) {
        for(B3Body body : bodies) {
            B3Vec3 position = body.GetPosition();
            float dx = position.GetX() - centerX;
            float dy = position.GetY() - centerY;
            float dz = position.GetZ() - centerZ;
            float length = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
            float invLength = length > 0.001f ? 1.0f / length : 1.0f;
            B3Vec3 impulse = new B3Vec3(dx * invLength * impulseScale, (dy * invLength + 0.5f) * impulseScale,
                    dz * invLength * impulseScale);
            body.ApplyLinearImpulseToCenter(impulse, true);
            AbstractBox3DSample.dispose(impulse, position);
        }
    }

    private static void connect(AbstractBox3DSample sample, B3Body bodyA, B3Body bodyB, float ax, float ay, float az,
            float bx, float by, float bz) {
        B3SphericalJointDef jointDef = new B3SphericalJointDef();
        jointDef.SetBodyIdA(bodyA.GetId());
        jointDef.SetBodyIdB(bodyB.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, ax, ay, az);
        JointSampleUtil.setLocalPositionB(jointDef, bx, by, bz);
        jointDef.SetEnableSpring(true);
        jointDef.SetHertz(3.0f);
        jointDef.SetDampingRatio(0.7f);
        AbstractBox3DSample.dispose(sample.world().CreateSphericalJoint(jointDef), jointDef);
    }
}
