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

final class MotionLocksSample extends AbstractBox3DSample {
    private final B3Body[] bodies = new B3Body[4];
    private float timer;
    private int lockMode;

    MotionLocksSample() {
        addGroundBox(20.0f);
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);

        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        B3Hull box = B3Hull.CreateBox(1.0f, 1.0f, 0.5f);

        createDistanceRow(ground, shapeDef, box, 0, -7.5f, 10.0f);
        createPrismaticRow(ground, shapeDef, box, 1, -2.5f, 10.0f);
        createRevoluteRow(ground, shapeDef, box, 2, 2.5f, 10.0f);
        createWeldRow(ground, shapeDef, box, 3, 7.5f, 10.0f);

        dispose(box, shapeDef);
    }

    @Override
    public void step(float deltaSeconds) {
        timer += deltaSeconds;
        if(timer > 2.0f) {
            timer = 0.0f;
            lockMode = (lockMode + 1) % 4;
            B3MotionLocks locks = motionLocks(lockMode == 1, false, true, false, lockMode == 2, lockMode == 3);
            for(int i = 0; i < bodies.length; i++) {
                bodies[i].SetMotionLocks(locks);
                bodies[i].SetAwake(true);
            }
            dispose(locks);
        }

        B3Vec3 impulse = new B3Vec3(25.0f, 0.0f, 0.0f);
        bodies[0].ApplyLinearImpulseToCenter(impulse, true);
        dispose(impulse);
        super.step(deltaSeconds);
    }

    private void createDistanceRow(B3Body ground, B3ShapeDef shapeDef, B3Hull box, int index, float x, float y) {
        B3Body body = createLockBody(shapeDef, box, index, x, y);
        B3DistanceJointDef jointDef = new B3DistanceJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(body.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, x, y + 3.0f, 0.0f);
        JointSampleUtil.setLocalPositionB(jointDef, 0.0f, 1.0f, 0.0f);
        jointDef.SetLength(2.0f);
        jointDef.SetCollideConnected(true);
        dispose(world().CreateDistanceJoint(jointDef));
        dispose(jointDef);
    }

    private void createPrismaticRow(B3Body ground, B3ShapeDef shapeDef, B3Hull box, int index, float x, float y) {
        B3Body body = createLockBody(shapeDef, box, index, x, y);
        B3PrismaticJointDef jointDef = new B3PrismaticJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(body.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, x - 1.0f, y, 0.0f);
        JointSampleUtil.setLocalPositionB(jointDef, -1.0f, 0.0f, 0.0f);
        jointDef.SetCollideConnected(true);
        dispose(world().CreatePrismaticJoint(jointDef));
        dispose(jointDef);
    }

    private void createRevoluteRow(B3Body ground, B3ShapeDef shapeDef, B3Hull box, int index, float x, float y) {
        B3Body body = createLockBody(shapeDef, box, index, x, y);
        B3RevoluteJointDef jointDef = new B3RevoluteJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(body.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, x - 1.0f, y, 0.0f);
        JointSampleUtil.setLocalPositionB(jointDef, -1.0f, 0.0f, 0.0f);
        jointDef.SetCollideConnected(true);
        dispose(world().CreateRevoluteJoint(jointDef));
        dispose(jointDef);
    }

    private void createWeldRow(B3Body ground, B3ShapeDef shapeDef, B3Hull box, int index, float x, float y) {
        B3Body body = createLockBody(shapeDef, box, index, x, y);
        B3WeldJointDef jointDef = new B3WeldJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(body.GetId());
        JointSampleUtil.setLocalPositionA(jointDef, x - 1.0f, y, 0.0f);
        JointSampleUtil.setLocalPositionB(jointDef, -1.0f, 0.0f, 0.0f);
        jointDef.SetAngularHertz(2.0f);
        jointDef.SetAngularDampingRatio(0.5f);
        jointDef.SetCollideConnected(true);
        dispose(world().CreateWeldJoint(jointDef));
        dispose(jointDef);
    }

    private B3Body createLockBody(B3ShapeDef shapeDef, B3Hull box, int index, float x, float y) {
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), x, y, 0.0f, null);
        bodyDef.SetEnableSleep(false);
        B3Body body = world().CreateBody(bodyDef);
        dispose(body.CreateHullShape(shapeDef, box));
        bodies[index] = body;
        dispose(bodyDef);
        return body;
    }
}
