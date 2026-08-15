package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3HeightField;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Joint;
import com.github.xpenatan.box3d.B3ParallelJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WheelJointDef;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerInput;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerTarget;

/** Exact default scene from Box3D's Joints/Driving sample. */
final class DrivingSample extends AbstractBox3DSample {
    private static final float PI = (float)Math.PI;

    private B3HeightField heightField;
    private long chassisId;
    private long frontLeftId;
    private long frontRightId;
    private long rearLeftId;
    private long rearRightId;
    private Box3DPlayerInput playerInput;
    private boolean thirdPerson;

    DrivingSample() {
        B3BodyDef groundDef = bodyDef(B3.StaticBody(), -20.0f, 0.0f, -20.0f, null);
        B3Body ground = world().CreateBody(groundDef);
        B3Vec3 heightScale = new B3Vec3(4.0f, 2.0f, 4.0f);
        heightField = B3HeightField.CreateWave(50, 50, heightScale, 0.02f, 0.04f, false);
        B3ShapeDef groundShapeDef = new B3ShapeDef();
        dispose(ground.CreateHeightFieldShape(groundShapeDef, heightField));
        dispose(groundShapeDef, heightScale, groundDef);

        B3BodyDef chassisDef = bodyDef(B3.DynamicBody(), 0.0f, 2.5f, 0.0f, null);
        B3Body chassis = world().CreateBody(chassisDef);
        chassisId = chassis.GetId();
        B3ShapeDef chassisShapeDef = new B3ShapeDef();
        chassisShapeDef.SetDensity(0.5f);
        B3Hull chassisHull = B3Hull.CreateBox(2.0f, 0.5f, 1.0f);
        dispose(chassis.CreateHullShape(chassisShapeDef, chassisHull));
        dispose(chassisHull, chassisShapeDef, chassisDef);

        B3ParallelJointDef parallelDef = new B3ParallelJointDef();
        parallelDef.SetBodyIdA(ground.GetId());
        parallelDef.SetBodyIdB(chassis.GetId());
        B3Quat upright = rotationX(-0.5f * PI);
        JointSampleUtil.setLocalFrameA(parallelDef, 0.0f, 0.0f, 0.0f, upright);
        JointSampleUtil.setLocalFrameB(parallelDef, 0.0f, 0.0f, 0.0f, upright);
        parallelDef.SetDrawScale(2.0f);
        parallelDef.SetCollideConnected(true);
        parallelDef.SetHertz(0.5f);
        parallelDef.SetDampingRatio(1.0f);
        dispose(world().CreateParallelJoint(parallelDef), parallelDef, upright);

        B3ShapeDef wheelShapeDef = new B3ShapeDef();
        wheelShapeDef.SetDensity(2.0f);
        B3SurfaceMaterial wheelMaterial = wheelShapeDef.GetBaseMaterial();
        wheelMaterial.SetFriction(3.0f);
        wheelShapeDef.SetBaseMaterial(wheelMaterial);

        B3Quat wheelRotation = rotationX(0.5f * PI);
        B3BodyDef wheelBodyDef = new B3BodyDef();
        wheelBodyDef.SetType(B3.DynamicBody());
        wheelBodyDef.SetAllowFastRotation(true);
        wheelBodyDef.SetRotation(wheelRotation);

        B3WheelJointDef wheelDef = new B3WheelJointDef();
        wheelDef.SetBodyIdA(chassis.GetId());
        B3Quat frameA = rotationZ(0.5f * PI);
        B3Quat frameB = rotationX(-0.5f * PI);
        JointSampleUtil.setLocalFrameB(wheelDef, 0.0f, 0.0f, 0.0f, frameB);
        wheelDef.SetEnableSuspensionLimit(true);
        wheelDef.SetLowerSuspensionLimit(-0.2f);
        wheelDef.SetUpperSuspensionLimit(0.2f);
        wheelDef.SetEnableSuspensionSpring(true);
        wheelDef.SetSuspensionHertz(4.0f);
        wheelDef.SetSuspensionDampingRatio(0.7f);
        wheelDef.SetEnableSpinMotor(true);
        wheelDef.SetMaxSpinTorque(5.0f);
        wheelDef.SetEnableSteering(true);
        wheelDef.SetSteeringHertz(10.0f);
        wheelDef.SetSteeringDampingRatio(0.7f);
        wheelDef.SetTargetSteeringAngle(0.0f);
        wheelDef.SetMaxSteeringTorque(5.0f);
        wheelDef.SetEnableSteeringLimit(true);
        wheelDef.SetLowerSteeringLimit(-0.25f * PI);
        wheelDef.SetUpperSteeringLimit(0.25f * PI);

        B3Vec3 sphereCenter = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere wheelSphere = new B3Sphere(sphereCenter, 0.4f);
        frontLeftId = createWheel(chassis, wheelBodyDef, wheelShapeDef, wheelSphere, wheelDef, frameA,
                1.5f, 2.0f, 0.8f, true, false);
        frontRightId = createWheel(chassis, wheelBodyDef, wheelShapeDef, wheelSphere, wheelDef, frameA,
                1.5f, 2.0f, -0.8f, true, false);
        rearLeftId = createWheel(chassis, wheelBodyDef, wheelShapeDef, wheelSphere, wheelDef, frameA,
                -1.5f, 2.0f, 0.8f, false, true);
        rearRightId = createWheel(chassis, wheelBodyDef, wheelShapeDef, wheelSphere, wheelDef, frameA,
                -1.5f, 2.0f, -0.8f, false, true);

        dispose(wheelSphere, sphereCenter, wheelDef, frameB, frameA, wheelBodyDef, wheelRotation,
                wheelMaterial, wheelShapeDef, chassis, ground);
    }

    private long createWheel(B3Body chassis, B3BodyDef bodyDef, B3ShapeDef shapeDef, B3Sphere sphere,
            B3WheelJointDef jointDef, B3Quat frameA, float x, float y, float z, boolean steering,
            boolean spinMotor) {
        B3Vec3 position = new B3Vec3(x, y, z);
        bodyDef.SetPosition(position);
        B3Body wheel = world().CreateBody(bodyDef);
        dispose(wheel.CreateSphereShape(shapeDef, sphere));

        jointDef.SetBodyIdA(chassis.GetId());
        jointDef.SetBodyIdB(wheel.GetId());
        JointSampleUtil.setLocalFrameA(jointDef, x, -0.5f, z, frameA);
        jointDef.SetEnableSteering(steering);
        jointDef.SetEnableSpinMotor(spinMotor);
        B3Joint joint = world().CreateWheelJoint(jointDef);
        long jointId = joint.GetId();
        dispose(joint, wheel, position);
        return jointId;
    }

    @Override
    public void step(float deltaSeconds) {
        float forward = thirdPerson && playerInput != null ? playerInput.moveForward() : 0.0f;
        float right = thirdPerson && playerInput != null ? playerInput.moveRight() : 0.0f;
        B3Joint frontLeft = new B3Joint(frontLeftId);
        B3Joint frontRight = new B3Joint(frontRightId);
        B3Joint rearLeft = new B3Joint(rearLeftId);
        B3Joint rearRight = new B3Joint(rearRightId);
        float steering = -0.25f * PI * right;
        frontLeft.SetWheelTargetSteeringAngle(steering);
        frontRight.SetWheelTargetSteeringAngle(steering);
        rearLeft.SetWheelSpinMotorSpeed(-30.0f * forward);
        rearRight.SetWheelSpinMotorSpeed(-30.0f * forward);
        if(forward != 0.0f || right != 0.0f) {
            B3Body chassis = new B3Body(chassisId);
            chassis.SetAwake(true);
            dispose(chassis);
        }
        dispose(rearRight, rearLeft, frontRight, frontLeft);
        super.step(deltaSeconds);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(heightField);
    }

    @Override
    public boolean supportsPlayerControl() {
        return true;
    }

    @Override
    public void setPlayerInput(Box3DPlayerInput input, boolean thirdPerson) {
        playerInput = input;
        this.thirdPerson = thirdPerson;
    }

    @Override
    public boolean getCameraTarget(Box3DPlayerTarget target) {
        B3Body chassis = new B3Body(chassisId);
        if(!chassis.IsValid()) {
            dispose(chassis);
            return false;
        }
        B3Vec3 position = chassis.GetPosition();
        target.set(position.GetX(), position.GetY(), position.GetZ());
        dispose(chassis);
        return true;
    }
}
