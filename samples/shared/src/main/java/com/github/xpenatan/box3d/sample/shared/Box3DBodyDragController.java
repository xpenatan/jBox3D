package com.github.xpenatan.box3d.sample.shared;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Joint;
import com.github.xpenatan.box3d.B3MotorJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3World;
import com.github.xpenatan.jParser.api.NativeObject;

public final class Box3DBodyDragController {
    private static final float RAY_LENGTH = 260.0f;
    private static final float MOUSE_LINEAR_HERTZ = 5.0f;
    private static final float MOUSE_LINEAR_DAMPING_RATIO = 0.9f;
    private static final float MOUSE_FORCE_SCALE = 100.0f;
    private static final float REFERENCE_GRAVITY = 10.0f;
    private static final float MIN_BODY_MASS = 0.25f;
    private static final float MIN_RAY_PLANE_DOT = 0.0001f;

    private final float[] localGrabOffset = new float[3];
    private final float[] dragPlaneNormal = new float[3];
    private final float[] targetPoint = new float[3];
    private boolean enabled = true;
    private boolean dragging;
    private long bodyId;
    private long mouseBodyId;
    private long mouseJointId;
    private float dragPlaneDistance;
    private float fallbackDistance;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(!enabled) {
            end();
        }
    }

    public boolean isDragging() {
        return dragging;
    }

    public boolean begin(B3World world, float originX, float originY, float originZ, float directionX,
            float directionY, float directionZ) {
        end();
        if(!enabled || world == null) {
            return false;
        }

        float invLength = inverseLength(directionX, directionY, directionZ);
        if(invLength == 0.0f) {
            return false;
        }
        directionX *= invLength;
        directionY *= invLength;
        directionZ *= invLength;

        B3Vec3 origin = new B3Vec3(originX, originY, originZ);
        B3Vec3 translation = new B3Vec3(directionX * RAY_LENGTH, directionY * RAY_LENGTH, directionZ * RAY_LENGTH);
        B3QueryFilter filter = new B3QueryFilter();
        B3RayResult result = null;
        B3Vec3 point = null;
        B3Vec3 bodyPosition = null;
        B3Quat bodyRotation = null;
        B3Vec3 rotationVector = null;
        B3BodyDef mouseBodyDef = null;
        B3Vec3 mouseBodyPosition = null;
        B3Body mouseBody = null;
        B3MotorJointDef mouseJointDef = null;
        B3Vec3 localJointPosition = null;
        B3Joint mouseJoint = null;
        B3Shape shape = null;
        B3Body body = null;
        try {
            long defaultBits = B3.DefaultMaskBits();
            filter.SetCategoryBits(defaultBits);
            filter.SetMaskBits(defaultBits);
            result = world.CastRayClosest(origin, translation, filter);
            if(result == null || !result.GetHit()) {
                return false;
            }

            shape = new B3Shape(result.GetShapeId());
            if(!shape.IsValid()) {
                return false;
            }
            body = new B3Body(shape.GetBodyId());
            if(!body.IsValid() || body.GetType() != B3.DynamicBody()) {
                return false;
            }

            point = result.GetPoint();
            bodyPosition = body.GetPosition();
            bodyRotation = body.GetRotation();
            rotationVector = bodyRotation.GetV();
            bodyId = body.GetId();
            targetPoint[0] = point.GetX();
            targetPoint[1] = point.GetY();
            targetPoint[2] = point.GetZ();
            dragPlaneNormal[0] = directionX;
            dragPlaneNormal[1] = directionY;
            dragPlaneNormal[2] = directionZ;
            dragPlaneDistance = dot(dragPlaneNormal, targetPoint);
            fallbackDistance = Math.max(0.0f, RAY_LENGTH * result.GetFraction());
            inverseRotateVector(rotationVector.GetX(), rotationVector.GetY(), rotationVector.GetZ(), bodyRotation.GetS(),
                    point.GetX() - bodyPosition.GetX(), point.GetY() - bodyPosition.GetY(),
                    point.GetZ() - bodyPosition.GetZ(), localGrabOffset);

            mouseBodyDef = new B3BodyDef();
            mouseBodyPosition = new B3Vec3(targetPoint[0], targetPoint[1], targetPoint[2]);
            mouseBodyDef.SetType(B3.KinematicBody());
            mouseBodyDef.SetPosition(mouseBodyPosition);
            mouseBodyDef.SetEnableSleep(false);
            mouseBodyDef.SetIsAwake(true);
            mouseBody = world.CreateBody(mouseBodyDef);
            if(mouseBody == null || !mouseBody.IsValid()) {
                return false;
            }
            mouseBodyId = mouseBody.GetId();

            mouseJointDef = new B3MotorJointDef();
            localJointPosition = new B3Vec3(localGrabOffset[0], localGrabOffset[1], localGrabOffset[2]);
            mouseJointDef.SetBodyIdA(mouseBodyId);
            mouseJointDef.SetBodyIdB(bodyId);
            mouseJointDef.SetLocalPositionB(localJointPosition);
            mouseJointDef.SetLinearHertz(MOUSE_LINEAR_HERTZ);
            mouseJointDef.SetLinearDampingRatio(MOUSE_LINEAR_DAMPING_RATIO);
            float mass = Math.max(MIN_BODY_MASS, body.GetMass());
            float gravityScale = Math.max(1.0f, Math.abs(body.GetGravityScale()));
            float springForce = MOUSE_FORCE_SCALE * mass * REFERENCE_GRAVITY * gravityScale;
            mouseJointDef.SetMaxSpringForce(springForce);
            mouseJointDef.SetMaxVelocityTorque(0.0f);
            mouseJointDef.SetMaxSpringTorque(0.0f);
            mouseJoint = world.CreateMotorJoint(mouseJointDef);
            if(mouseJoint == null || !mouseJoint.IsValid()) {
                end();
                return false;
            }
            mouseJointId = mouseJoint.GetId();
            body.SetAwake(true);
            dragging = true;
            return true;
        }
        finally {
            dispose(mouseJoint, localJointPosition, mouseJointDef, mouseBody, mouseBodyPosition, mouseBodyDef,
                    rotationVector, bodyRotation, point, bodyPosition, body, shape, result, filter, translation, origin);
        }
    }

    public void updateTarget(float originX, float originY, float originZ, float directionX, float directionY,
            float directionZ) {
        if(!enabled || !dragging) {
            return;
        }
        float invLength = inverseLength(directionX, directionY, directionZ);
        if(invLength == 0.0f) {
            return;
        }
        directionX *= invLength;
        directionY *= invLength;
        directionZ *= invLength;

        float distance = rayPlaneDistance(originX, originY, originZ, directionX, directionY, directionZ);
        if(distance <= 0.0f) {
            distance = fallbackDistance;
        }
        if(distance <= 0.0f) {
            return;
        }
        targetPoint[0] = originX + directionX * distance;
        targetPoint[1] = originY + directionY * distance;
        targetPoint[2] = originZ + directionZ * distance;
    }

    public void step(float timeStep) {
        if(!enabled || !dragging || timeStep <= 0.0f) {
            return;
        }

        B3Joint mouseJoint = new B3Joint(mouseJointId);
        B3Body mouseBody = null;
        B3Vec3 target = null;
        B3Quat rotation = null;
        try {
            if(!mouseJoint.IsValid()) {
                end();
                return;
            }
            mouseBody = new B3Body(mouseBodyId);
            if(!mouseBody.IsValid()) {
                end();
                return;
            }
            target = new B3Vec3(targetPoint[0], targetPoint[1], targetPoint[2]);
            rotation = new B3Quat(0.0f, 0.0f, 0.0f, 1.0f);
            mouseBody.SetTargetTransform(target, rotation, timeStep, true);
        }
        finally {
            dispose(rotation, target, mouseBody, mouseJoint);
        }
    }

    public void end() {
        if(mouseJointId != 0L) {
            B3Joint mouseJoint = new B3Joint(mouseJointId);
            try {
                if(mouseJoint.IsValid()) {
                    mouseJoint.Destroy(true);
                }
            }
            finally {
                dispose(mouseJoint);
            }
        }
        if(mouseBodyId != 0L) {
            B3Body mouseBody = new B3Body(mouseBodyId);
            try {
                if(mouseBody.IsValid()) {
                    mouseBody.Destroy();
                }
            }
            finally {
                dispose(mouseBody);
            }
        }
        dragging = false;
        bodyId = 0L;
        mouseBodyId = 0L;
        mouseJointId = 0L;
        dragPlaneDistance = 0.0f;
        fallbackDistance = 0.0f;
        clear(localGrabOffset);
        clear(dragPlaneNormal);
        clear(targetPoint);
    }

    private static float inverseLength(float x, float y, float z) {
        float length = length(x, y, z);
        return length > 0.000001f ? 1.0f / length : 0.0f;
    }

    private static float length(float x, float y, float z) {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    private static void rotateVector(float qx, float qy, float qz, float qw, float x, float y, float z, float[] out) {
        float tx = 2.0f * (qy * z - qz * y);
        float ty = 2.0f * (qz * x - qx * z);
        float tz = 2.0f * (qx * y - qy * x);
        out[0] = x + qw * tx + qy * tz - qz * ty;
        out[1] = y + qw * ty + qz * tx - qx * tz;
        out[2] = z + qw * tz + qx * ty - qy * tx;
    }

    private static void inverseRotateVector(float qx, float qy, float qz, float qw, float x, float y, float z,
            float[] out) {
        rotateVector(-qx, -qy, -qz, qw, x, y, z, out);
    }

    private float rayPlaneDistance(float originX, float originY, float originZ, float directionX, float directionY,
            float directionZ) {
        float denominator = dragPlaneNormal[0] * directionX + dragPlaneNormal[1] * directionY
                + dragPlaneNormal[2] * directionZ;
        if(Math.abs(denominator) < MIN_RAY_PLANE_DOT) {
            return -1.0f;
        }

        float distance = (dragPlaneDistance
                - (dragPlaneNormal[0] * originX + dragPlaneNormal[1] * originY + dragPlaneNormal[2] * originZ))
                / denominator;
        return distance > 0.0f && distance <= RAY_LENGTH ? distance : -1.0f;
    }

    private static float dot(float[] a, float[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    private static void clear(float[] values) {
        values[0] = 0.0f;
        values[1] = 0.0f;
        values[2] = 0.0f;
    }

    private static void dispose(NativeObject... objects) {
        if(objects == null) {
            return;
        }
        for(int i = 0; i < objects.length; i++) {
            NativeObject object = objects[i];
            if(object != null && object.native_hasOwnership() && !object.isDisposed()) {
                object.dispose();
            }
        }
    }
}
