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

final class TopDownFrictionSample extends AbstractBox3DSample {
    TopDownFrictionSample() {
        super(4);

        B3WorldDefBuilder.disableGravity(world());
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        addArenaWall(ground, 0.0f, 0.0f, 0.0f, 10.0f, 0.5f, 4.0f);
        addArenaWall(ground, -10.0f, 10.0f, 0.0f, 0.5f, 10.0f, 4.0f);
        addArenaWall(ground, 10.0f, 10.0f, 0.0f, 0.5f, 10.0f, 4.0f);
        addArenaWall(ground, 0.0f, 20.0f, 0.0f, 10.0f, 0.5f, 4.0f);

        B3MotorJointDef jointDef = new B3MotorJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetCollideConnected(true);
        jointDef.SetMaxVelocityForce(1000.0f);
        jointDef.SetMaxVelocityTorque(1000.0f);

        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.8f, 0.0f);
        B3Vec3 c1 = new B3Vec3(-0.25f, 0.0f, 0.0f);
        B3Vec3 c2 = new B3Vec3(0.25f, 0.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(c1, c2, 0.25f);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.35f);
        B3Hull cube = B3Hull.CreateBox(0.35f, 0.35f, 0.35f);

        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), -5.0f + j, 15.0f - i, 0.0f, null);
                bodyDef.SetGravityScale(0.0f);
                B3Body body = world().CreateBody(bodyDef);
                int remainder = (10 * i + j) % 4;
                if(remainder == 0) {
                    dispose(body.CreateCapsuleShape(shapeDef, capsule));
                }
                else if(remainder == 1) {
                    dispose(body.CreateSphereShape(shapeDef, sphere));
                }
                else {
                    dispose(body.CreateHullShape(shapeDef, cube));
                }
                jointDef.SetBodyIdB(body.GetId());
                dispose(world().CreateMotorJoint(jointDef));
                dispose(bodyDef);
            }
        }

        dispose(cube, sphere, center, capsule, c2, c1, shapeDef, jointDef);
    }

    private void addArenaWall(B3Body ground, float x, float y, float z, float hx, float hy, float hz) {
        addBoxShape(ground, hx, hy, hz, x, y, z, null, 0.0f, 0.6f, 0.0f, 0.0f);
    }
}
