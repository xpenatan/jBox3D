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

final class FilterJointSample extends AbstractBox3DSample {
    FilterJointSample() {
        addGroundBox(20.0f);

        B3Body body1 = addDynamicBox(2.0f, 4.0f, 0.0f, 0.5f, 0.5f, 0.5f);
        B3Body body2 = addDynamicBox(-2.0f, 4.0f, 0.0f, 0.5f, 0.5f, 0.5f);

        B3FilterJointDef jointDef = new B3FilterJointDef();
        jointDef.SetBodyIdA(body1.GetId());
        jointDef.SetBodyIdB(body2.GetId());
        dispose(world().CreateFilterJoint(jointDef));
        dispose(jointDef);
    }
}
