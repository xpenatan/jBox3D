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

final class DoorSample extends AbstractBox3DSample {
    private final B3Body door;
    private int steps;

    DoorSample() {
        B3Body ground = addStaticBox(0.0f, -0.25f, 0.0f, 20.0f, 0.25f, 20.0f, null);
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, 1.5f, 0.0f, null);
        bodyDef.SetGravityScale(2.0f);
        door = world().CreateBody(bodyDef);
        B3Shape doorShape = createBoxShape(door, 0.75f, 1.5f, 0.1f, 1000.0f, 0.6f, 0.0f, 0.0f);

        B3Quat axis = rotationX(-0.5f * (float)Math.PI);
        createDoorJoint(ground, door, -0.75f, 1.0f, -0.75f, -1.5f, axis);
        createDoorJoint(ground, door, -0.75f, 4.0f, -0.75f, 1.5f, axis);

        dispose(axis, doorShape, bodyDef);
    }

    @Override
    public void step(float deltaSeconds) {
        steps++;
        if(steps == 45) {
            B3Vec3 impulse = new B3Vec3(0.0f, 0.0f, -50000.0f);
            B3Vec3 point = new B3Vec3(0.75f, 1.5f, 0.0f);
            door.ApplyLinearImpulse(impulse, point, true);
            dispose(point, impulse);
        }
        super.step(deltaSeconds);
    }

    private void createDoorJoint(B3Body ground, B3Body door, float ax, float ay, float bx, float by, B3Quat axis) {
        B3RevoluteJointDef jointDef = new B3RevoluteJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(door.GetId());
        JointSampleUtil.setLocalFrameA(jointDef, ax, ay, 0.0f, axis);
        JointSampleUtil.setLocalFrameB(jointDef, bx, by, 0.0f, axis);
        jointDef.SetConstraintHertz(120.0f);
        jointDef.SetConstraintDampingRatio(0.0f);
        jointDef.SetEnableLimit(true);
        jointDef.SetLowerAngle(degreesToRadians(-90.0f));
        jointDef.SetUpperAngle(degreesToRadians(90.0f));
        jointDef.SetEnableSpring(true);
        jointDef.SetHertz(1.0f);
        jointDef.SetDampingRatio(0.5f);
        jointDef.SetMaxMotorTorque(100.0f);
        jointDef.SetDrawScale(2.0f);
        dispose(world().CreateRevoluteJoint(jointDef));
        dispose(jointDef);
    }

    private float degreesToRadians(float degrees) {
        return (float)(degrees * Math.PI / 180.0);
    }
}
