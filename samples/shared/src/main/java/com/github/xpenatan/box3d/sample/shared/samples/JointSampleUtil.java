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

final class JointSampleUtil {
    private JointSampleUtil() {
    }

    static void setLocalPositionA(B3DistanceJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionA(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalPositionB(B3DistanceJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionB(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalPositionA(B3MotorJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionA(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalPositionB(B3MotorJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionB(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalFrameA(B3ParallelJointDef jointDef, float x, float y, float z, B3Quat rotation) {
        B3Vec3 position = new B3Vec3(x, y, z);
        B3Transform transform = new B3Transform(position, rotation);
        jointDef.SetLocalFrameA(transform);
        AbstractBox3DSample.dispose(transform, position);
    }

    static void setLocalFrameB(B3ParallelJointDef jointDef, float x, float y, float z, B3Quat rotation) {
        B3Vec3 position = new B3Vec3(x, y, z);
        B3Transform transform = new B3Transform(position, rotation);
        jointDef.SetLocalFrameB(transform);
        AbstractBox3DSample.dispose(transform, position);
    }

    static void setLocalPositionA(B3PrismaticJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionA(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalPositionB(B3PrismaticJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionB(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalFrameA(B3RevoluteJointDef jointDef, float x, float y, float z, B3Quat rotation) {
        B3Vec3 position = new B3Vec3(x, y, z);
        B3Transform transform = new B3Transform(position, rotation);
        jointDef.SetLocalFrameA(transform);
        AbstractBox3DSample.dispose(transform, position);
    }

    static void setLocalFrameB(B3RevoluteJointDef jointDef, float x, float y, float z, B3Quat rotation) {
        B3Vec3 position = new B3Vec3(x, y, z);
        B3Transform transform = new B3Transform(position, rotation);
        jointDef.SetLocalFrameB(transform);
        AbstractBox3DSample.dispose(transform, position);
    }

    static void setLocalPositionA(B3RevoluteJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionA(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalPositionB(B3RevoluteJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionB(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalPositionA(B3WeldJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionA(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalPositionB(B3WeldJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionB(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalFrameA(B3WheelJointDef jointDef, float x, float y, float z, B3Quat rotation) {
        B3Vec3 position = new B3Vec3(x, y, z);
        B3Transform transform = new B3Transform(position, rotation);
        jointDef.SetLocalFrameA(transform);
        AbstractBox3DSample.dispose(transform, position);
    }

    static void setLocalFrameB(B3WheelJointDef jointDef, float x, float y, float z, B3Quat rotation) {
        B3Vec3 position = new B3Vec3(x, y, z);
        B3Transform transform = new B3Transform(position, rotation);
        jointDef.SetLocalFrameB(transform);
        AbstractBox3DSample.dispose(transform, position);
    }

    static void setLocalPositionA(B3SphericalJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionA(position);
        AbstractBox3DSample.dispose(position);
    }

    static void setLocalPositionB(B3SphericalJointDef jointDef, float x, float y, float z) {
        B3Vec3 position = new B3Vec3(x, y, z);
        jointDef.SetLocalPositionB(position);
        AbstractBox3DSample.dispose(position);
    }
}
