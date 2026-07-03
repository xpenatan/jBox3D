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

final class DistanceJointSample extends AbstractBox3DSample {
    private static final int COUNT = 8;

    DistanceJointSample() {
        addGroundBox(20.0f);

        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = shapeDef(20.0f, 0.6f, 0.0f, 0.0f);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, 0.25f);

        B3DistanceJointDef jointDef = new B3DistanceJointDef();
        jointDef.SetLength(1.0f);
        jointDef.SetHertz(5.0f);
        jointDef.SetDampingRatio(0.5f);
        jointDef.SetLowerSpringForce(-2000.0f);
        jointDef.SetUpperSpringForce(100.0f);

        B3Body previous = ground;
        for(int i = 0; i < COUNT; i++) {
            B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), i + 1.0f, 20.0f, 0.0f, null);
            bodyDef.SetAngularDamping(1.0f);
            B3Body body = world().CreateBody(bodyDef);
            dispose(body.CreateSphereShape(shapeDef, sphere));

            jointDef.SetBodyIdA(previous.GetId());
            jointDef.SetBodyIdB(body.GetId());
            JointSampleUtil.setLocalPositionA(jointDef, previous == ground ? i : 0.0f, previous == ground ? 20.0f : 0.0f, 0.0f);
            JointSampleUtil.setLocalPositionB(jointDef, 0.0f, 0.0f, 0.0f);
            dispose(world().CreateDistanceJoint(jointDef));

            previous = body;
            dispose(bodyDef);
        }

        dispose(jointDef, sphere, center, shapeDef);
    }
}
