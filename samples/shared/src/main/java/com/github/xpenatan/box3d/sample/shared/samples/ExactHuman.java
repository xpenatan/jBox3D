package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Filter;
import com.github.xpenatan.box3d.B3FilterJointDef;
import com.github.xpenatan.box3d.B3Joint;
import com.github.xpenatan.box3d.B3ParallelJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3RevoluteJointDef;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3World;

/** Direct Java port of {@code shared/human.c} at the pinned Box3D commit. */
final class ExactHuman {
    static final int PELVIS = 0;
    static final int SPINE_01 = 1;
    static final int SPINE_02 = 2;
    static final int SPINE_03 = 3;
    static final int NECK = 4;
    static final int HEAD = 5;
    static final int THIGH_L = 6;
    static final int CALF_L = 7;
    static final int THIGH_R = 8;
    static final int CALF_R = 9;
    static final int UPPER_ARM_L = 10;
    static final int LOWER_ARM_L = 11;
    static final int UPPER_ARM_R = 12;
    static final int LOWER_ARM_R = 13;
    static final int BONE_COUNT = 14;

    private static final int NONE = 0;
    private static final int SPHERICAL = 1;
    private static final int REVOLUTE = 2;
    private static final int PANT = 0;
    private static final int SHIRT = 1;
    private static final int SKIN = 2;
    private static final float DEG = (float)(Math.PI / 180.0);

    private static final BoneDef[] BONES = {
            bone(-1, NONE,
                    frame(0.0f, 0.932087f, -0.051708f, 0.739169f, 0.0f, 0.0f, 0.673520f),
                    capsule(0.07f, 0.0f, -0.08f, -0.07f, 0.0f, -0.08f, 0.13f), false, PANT,
                    null, null, 0.0f, 0.0f, 0.0f, 1.0f),
            bone(PELVIS, SPHERICAL,
                    frame(0.0f, 1.113505f, -0.03481f, 0.739973f, 0.0f, 0.0f, 0.672637f),
                    capsule(0.06f, -0.0f, -0.052264f, -0.06f, 0.0f, -0.052264f, 0.12f), true, SHIRT,
                    frame(0.0f, 0.0f, -0.182204f, -0.999999f, 0.0f, -0.0f, 0.001194f),
                    frame(0.0f, 0.0f, -0.007736f, -1.0f, 0.0f, -0.0f, 0.0f),
                    25.0f, -15.0f, 15.0f, 1.0f),
            bone(SPINE_01, SPHERICAL,
                    frame(0.0f, 1.194336f, -0.027087f, 0.703611f, 0.0f, 0.0f, 0.710586f),
                    capsule(0.08f, -0.015133f, -0.091801f, -0.08f, -0.015133f, -0.091801f, 0.10f), false, SHIRT,
                    frame(0.0f, -0.0f, -0.088935f, -0.998619f, -0.0f, 0.0f, -0.052540f),
                    frame(-0.0f, 0.0f, -0.008199f, -1.0f, 0.0f, -0.0f, 0.0f),
                    25.0f, -15.0f, 15.0f, 1.0f),
            bone(SPINE_02, SPHERICAL,
                    frame(-0.0f, 1.31043f, -0.028232f, 0.669856f, 0.000001f, -0.000001f, 0.742491f),
                    capsule(0.11f, -0.039753f, -0.13f, -0.11f, -0.039753f, -0.13f, 0.145f), false, SHIRT,
                    frame(-0.0f, 0.0f, -0.124298f, -0.998921f, 0.000001f, -0.000001f, -0.046434f),
                    frame(0.0f, 0.0f, 0.0f, -1.0f, 0.0f, -0.000001f, 0.0f),
                    15.0f, -10.0f, 10.0f, 1.0f),
            bone(SPINE_03, SPHERICAL,
                    frame(0.0f, 1.575582f, -0.055837f, 0.879922f, 0.0f, 0.0f, 0.475118f),
                    capsule(-0.000001f, -0.0f, -0.02f, 0.0f, -0.005f, -0.08f, 0.07f), false, SKIN,
                    frame(0.000001f, -0.000259f, -0.266585f, -0.942192f, -0.000001f, 0.0f, 0.335074f),
                    frame(0.0f, 0.0f, 0.0f, -1.0f, 0.0f, -0.000001f, 0.0f),
                    45.0f, -15.0f, 15.0f, 0.8f),
            bone(NECK, SPHERICAL,
                    frame(0.0f, 1.653348f, -0.003241f, 0.750288f, 0.0f, 0.0f, 0.661111f),
                    capsule(-0.000001f, 0.016892f, -0.05869f, 0.0f, -0.003629f, -0.115072f, 0.0975f), false, SKIN,
                    frame(0.0f, 0.001321f, -0.093873f, -0.974301f, -0.0f, -0.0f, -0.225251f),
                    frame(0.0f, 0.001268f, -0.005104f, -1.0f, 0.0f, -0.0f, 0.0f),
                    15.0f, -15.0f, 15.0f, 0.4f),
            bone(PELVIS, SPHERICAL,
                    frame(0.090416f, 0.986104f, -0.035090f, -0.703287f, -0.070715f, 0.053866f, 0.705327f),
                    capsule(0.023719f, 0.006008f, -0.039068f, -0.064492f, -0.004664f, -0.424718f, 0.09f), true, PANT,
                    frame(0.05f, 0.011537f, -0.055325f, -0.714896f, -0.022305f, -0.698361f, -0.026790f),
                    frame(0.0f, 0.0f, 0.0f, -0.002064f, 0.758987f, 0.017046f, 0.650880f),
                    10.0f, -60.0f, 40.0f, 1.0f),
            bone(THIGH_L, REVOLUTE,
                    frame(0.101198f, 0.527027f, -0.037374f, -0.653328f, -0.066860f, 0.058582f, 0.751838f),
                    capsule(0.001778f, 0.0f, 0.009841f, -0.078577f, 0.014707f, -0.41816f, 0.075f), false, PANT,
                    frame(-0.069989f, 0.000253f, -0.453844f, -0.000677f, 0.760087f, 0.105674f, 0.641171f),
                    frame(0.0f, 0.0f, 0.0f, -0.044589f, 0.765540f, 0.053368f, 0.639619f),
                    0.0f, -5.0f, 45.0f, 1.0f),
            bone(PELVIS, SPHERICAL,
                    frame(-0.090416f, 0.986104f, -0.03509f, -0.703287f, 0.070715f, -0.053865f, 0.705326f),
                    capsule(-0.023719f, 0.006008f, -0.039068f, 0.064492f, -0.004664f, -0.424718f, 0.09f), true, PANT,
                    frame(-0.05f, 0.011537f, -0.055326f, -0.039089f, -0.714094f, 0.043177f, 0.697623f),
                    frame(0.0f, 0.0f, 0.0f, 0.758805f, -0.019886f, -0.651012f, -0.001759f),
                    10.0f, -30.0f, 60.0f, 1.0f),
            bone(THIGH_R, REVOLUTE,
                    frame(-0.101198f, 0.527027f, -0.037373f, -0.653327f, 0.06686f, -0.058582f, 0.751839f),
                    capsule(-0.001820f, 0.0f, 0.010071f, 0.077883f, 0.014825f, -0.418047f, 0.075f), false, PANT,
                    frame(0.069988f, 0.000253f, -0.453844f, 0.760086f, -0.000675f, -0.641171f, -0.105676f),
                    frame(0.0f, 0.0f, 0.0f, 0.765540f, -0.044589f, -0.639619f, -0.053368f),
                    0.0f, -45.0f, 5.0f, 1.0f),
            bone(SPINE_03, SPHERICAL,
                    frame(0.20378f, 1.484275f, -0.115897f, 0.143082f, 0.695980f, -0.690130f, 0.13733f),
                    capsule(0.0f, 0.0f, 0.0f, -0.091118f, 0.037775f, 0.229719f, 0.075f), false, SHIRT,
                    frame(0.203780f, -0.069369f, -0.181921f, -0.278486f, 0.445600f, -0.097014f, 0.845266f),
                    frame(0.0f, 0.0f, 0.0f, -0.201396f, -0.001586f, 0.901850f, 0.382234f),
                    60.0f, -5.0f, 5.0f, 1.0f),
            bone(UPPER_ARM_L, REVOLUTE,
                    frame(0.305614f, 1.242908f, -0.117599f, 0.165048f, 0.563437f, -0.802002f, 0.109959f),
                    capsule(0.0f, 0.0f, 0.0f, -0.142406f, 0.039392f, 0.261092f, 0.05f), false, SKIN,
                    frame(-0.095482f, 0.039584f, 0.240723f, 0.512487f, -0.180629f, 0.839474f, 0.003742f),
                    frame(0.0f, 0.0f, 0.0f, 0.503803f, -0.029831f, 0.858168f, 0.094017f),
                    0.0f, -5.0f, 60.0f, 1.0f),
            bone(SPINE_03, SPHERICAL,
                    frame(-0.20378f, 1.484276f, -0.115899f, 0.143083f, -0.695978f, 0.690132f, 0.137329f),
                    capsule(0.0f, 0.0f, 0.0f, 0.091118f, 0.037775f, 0.229718f, 0.075f), false, SHIRT,
                    frame(-0.203779f, -0.069371f, -0.181922f, -0.253621f, -0.414842f, 0.106962f, 0.867261f),
                    frame(0.0f, 0.0f, 0.0f, -0.201397f, 0.001587f, -0.901850f, 0.382233f),
                    60.0f, -5.0f, 5.0f, 1.0f),
            bone(UPPER_ARM_R, REVOLUTE,
                    frame(-0.305614f, 1.242907f, -0.117599f, 0.165048f, -0.563437f, 0.802002f, 0.109959f),
                    capsule(0.0f, 0.0f, 0.0f, 0.142406f, 0.039392f, 0.261092f, 0.05f), false, SKIN,
                    frame(0.095484f, 0.039585f, 0.240723f, -0.180627f, 0.512487f, -0.003744f, -0.839474f),
                    frame(0.0f, 0.0f, 0.0f, -0.029831f, 0.503803f, -0.094017f, -0.858169f),
                    0.0f, -60.0f, 5.0f, 1.0f)
    };

    private final B3World world;
    private final long[] bodyIds = new long[BONE_COUNT];
    private final long[] jointIds = new long[BONE_COUNT];
    private final int[] jointTypes = new int[BONE_COUNT];
    private final float[] jointFrictions = new float[BONE_COUNT];
    private long filterJointId;
    private float frictionTorque;

    private ExactHuman(B3World world) {
        this.world = world;
    }

    static ExactHuman create(B3World world, float x, float y, float z, float frictionTorque, float hertz,
            float dampingRatio, int groupIndex, boolean colorize) {
        ExactHuman human = new ExactHuman(world);
        human.frictionTorque = frictionTorque;

        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetRollingResistance(0.2f);
        B3Filter filter = shapeDef.GetFilter();
        int[] skinColors = { 0xFFDEAD, 0xFFFFE0, 0xCD853F, 0xD2B48C };
        int skinColor = skinColors[groupIndex % skinColors.length];

        for(int index = 0; index < BONE_COUNT; index++) {
            BoneDef bone = BONES[index];
            B3Vec3 position = new B3Vec3(x + bone.reference[0], y + bone.reference[1], z + bone.reference[2]);
            B3Quat rotation = quaternion(bone.reference);
            bodyDef.SetPosition(position);
            bodyDef.SetRotation(rotation);
            B3Body body = world.CreateBody(bodyDef);
            human.bodyIds[index] = body.GetId();
            human.jointTypes[index] = bone.jointType;
            human.jointFrictions[index] = bone.jointFriction;

            filter.SetGroupIndex(bone.filterByGroup ? -groupIndex : 0);
            shapeDef.SetFilter(filter);
            material.SetCustomColor(colorize ? colorFor(bone.colorRole, skinColor) : 0);
            shapeDef.SetBaseMaterial(material);
            B3Vec3 center1 = new B3Vec3(bone.capsule[0], bone.capsule[1], bone.capsule[2]);
            B3Vec3 center2 = new B3Vec3(bone.capsule[3], bone.capsule[4], bone.capsule[5]);
            B3Capsule capsule = new B3Capsule(center1, center2, bone.capsule[6]);
            AbstractBox3DSample.dispose(body.CreateCapsuleShape(shapeDef, capsule), capsule, center2, center1,
                    body, rotation, position);
        }

        for(int index = 1; index < BONE_COUNT; index++) {
            BoneDef bone = BONES[index];
            B3Transform localFrameA = transform(bone.localFrameA, true);
            B3Transform localFrameB = transform(bone.localFrameB, true);
            B3Joint joint;
            if(bone.jointType == REVOLUTE) {
                B3RevoluteJointDef jointDef = new B3RevoluteJointDef();
                jointDef.SetBodyIdA(human.bodyIds[bone.parent]);
                jointDef.SetBodyIdB(human.bodyIds[index]);
                jointDef.SetLocalFrameA(localFrameA);
                jointDef.SetLocalFrameB(localFrameB);
                jointDef.SetEnableLimit(true);
                jointDef.SetLowerAngle(bone.twistLowerDegrees * DEG);
                jointDef.SetUpperAngle(bone.twistUpperDegrees * DEG);
                jointDef.SetEnableSpring(hertz > 0.0f);
                jointDef.SetHertz(hertz);
                jointDef.SetDampingRatio(dampingRatio);
                jointDef.SetEnableMotor(true);
                jointDef.SetMaxMotorTorque(bone.jointFriction * frictionTorque);
                joint = world.CreateRevoluteJoint(jointDef);
                AbstractBox3DSample.dispose(jointDef);
            }
            else {
                B3SphericalJointDef jointDef = new B3SphericalJointDef();
                jointDef.SetBodyIdA(human.bodyIds[bone.parent]);
                jointDef.SetBodyIdB(human.bodyIds[index]);
                jointDef.SetLocalFrameA(localFrameA);
                jointDef.SetLocalFrameB(localFrameB);
                jointDef.SetEnableConeLimit(true);
                jointDef.SetConeAngle(bone.swingLimitDegrees * DEG);
                jointDef.SetEnableTwistLimit(true);
                jointDef.SetLowerTwistAngle(bone.twistLowerDegrees * DEG);
                jointDef.SetUpperTwistAngle(bone.twistUpperDegrees * DEG);
                jointDef.SetEnableSpring(hertz > 0.0f);
                jointDef.SetHertz(hertz);
                jointDef.SetDampingRatio(dampingRatio);
                jointDef.SetEnableMotor(true);
                jointDef.SetMaxMotorTorque(bone.jointFriction * frictionTorque);
                joint = world.CreateSphericalJoint(jointDef);
                AbstractBox3DSample.dispose(jointDef);
            }
            human.jointIds[index] = joint.GetId();
            AbstractBox3DSample.dispose(joint, localFrameB, localFrameA);
        }

        B3FilterJointDef filterDef = new B3FilterJointDef();
        filterDef.SetBodyIdA(human.bodyIds[THIGH_L]);
        filterDef.SetBodyIdB(human.bodyIds[THIGH_R]);
        B3Joint filterJoint = world.CreateFilterJoint(filterDef);
        human.filterJointId = filterJoint.GetId();
        AbstractBox3DSample.dispose(filterJoint, filterDef, filter, material, shapeDef, bodyDef);
        return human;
    }

    long bodyId(int boneIndex) {
        return bodyIds[boneIndex];
    }

    long jointId(int boneIndex) {
        return jointIds[boneIndex];
    }

    void setVelocity(float x, float y, float z) {
        B3Vec3 velocity = new B3Vec3(x, y, z);
        for(long bodyId : bodyIds) {
            B3Body body = new B3Body(bodyId);
            body.SetLinearVelocity(velocity);
            AbstractBox3DSample.dispose(body);
        }
        AbstractBox3DSample.dispose(velocity);
    }

    void setBullet(boolean bullet) {
        for(long bodyId : bodyIds) {
            B3Body body = new B3Body(bodyId);
            body.SetBullet(bullet);
            AbstractBox3DSample.dispose(body);
        }
    }

    void applyAngularImpulse(int boneIndex, float x, float y, float z) {
        B3Body body = new B3Body(bodyIds[boneIndex]);
        B3Vec3 impulse = new B3Vec3(x, y, z);
        body.ApplyAngularImpulse(impulse, true);
        AbstractBox3DSample.dispose(impulse, body);
    }

    void setJointFrictionTorque(float torque) {
        frictionTorque = torque;
        for(int index = 1; index < BONE_COUNT; index++) {
            B3Joint joint = new B3Joint(jointIds[index]);
            if(jointTypes[index] == REVOLUTE) {
                joint.SetRevoluteMaxMotorTorque(jointFrictions[index] * torque);
            }
            else {
                joint.SetSphericalMaxMotorTorque(jointFrictions[index] * torque);
            }
            AbstractBox3DSample.dispose(joint);
        }
    }

    void setJointSpringHertz(float hertz) {
        for(int index = 1; index < BONE_COUNT; index++) {
            B3Joint joint = new B3Joint(jointIds[index]);
            if(jointTypes[index] == REVOLUTE) {
                joint.SetRevoluteSpringHertz(hertz);
            }
            else {
                joint.SetSphericalSpringHertz(hertz);
            }
            AbstractBox3DSample.dispose(joint);
        }
    }

    void setJointDampingRatio(float dampingRatio) {
        for(int index = 1; index < BONE_COUNT; index++) {
            B3Joint joint = new B3Joint(jointIds[index]);
            if(jointTypes[index] == REVOLUTE) {
                joint.SetRevoluteSpringDampingRatio(dampingRatio);
            }
            else {
                joint.SetSphericalSpringDampingRatio(dampingRatio);
            }
            AbstractBox3DSample.dispose(joint);
        }
    }

    /** Exact port of {@code DestroyHuman} in {@code shared/human.c}. */
    void destroy() {
        if(filterJointId != 0L) {
            B3Joint filterJoint = new B3Joint(filterJointId);
            if(filterJoint.IsValid()) {
                filterJoint.Destroy(false);
            }
            AbstractBox3DSample.dispose(filterJoint);
            filterJointId = 0L;
        }
        for(int index = 1; index < BONE_COUNT; ++index) {
            if(jointIds[index] != 0L) {
                B3Joint joint = new B3Joint(jointIds[index]);
                if(joint.IsValid()) {
                    joint.Destroy(false);
                }
                AbstractBox3DSample.dispose(joint);
                jointIds[index] = 0L;
            }
        }
        for(int index = 0; index < BONE_COUNT; ++index) {
            if(bodyIds[index] != 0L) {
                B3Body body = new B3Body(bodyIds[index]);
                if(body.IsValid()) {
                    body.Destroy();
                }
                AbstractBox3DSample.dispose(body);
                bodyIds[index] = 0L;
            }
        }
    }

    void createParallelAnchors() {
        B3BodyDef anchorDef = new B3BodyDef();
        anchorDef.SetType(B3.KinematicBody());
        B3Vec3 axisZ = new B3Vec3(0.0f, 0.0f, 1.0f);
        B3Vec3 axisY = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Quat worldFrame = B3Quat.ComputeBetweenUnitVectors(axisZ, axisY);
        B3ParallelJointDef jointDef = new B3ParallelJointDef();
        jointDef.SetHertz(8.0f);
        jointDef.SetDampingRatio(1.0f);
        jointDef.SetMaxTorque(800.0f);

        for(long bodyId : bodyIds) {
            B3Body body = new B3Body(bodyId);
            B3Transform bodyTransform = body.GetTransform();
            B3Vec3 position = bodyTransform.GetP();
            B3Quat rotation = bodyTransform.GetQ();
            anchorDef.SetPosition(position);
            anchorDef.SetRotation(rotation);
            B3Body anchor = world.CreateBody(anchorDef);

            B3Quat frameRotation = B3Quat.InvMul(rotation, worldFrame);
            B3Vec3 localPosition = new B3Vec3(0.0f, 0.0f, 0.0f);
            B3Transform localFrame = new B3Transform(localPosition, frameRotation);
            jointDef.SetBodyIdA(anchor.GetId());
            jointDef.SetBodyIdB(bodyId);
            jointDef.SetLocalFrameA(localFrame);
            jointDef.SetLocalFrameB(localFrame);
            AbstractBox3DSample.dispose(world.CreateParallelJoint(jointDef), localFrame, localPosition,
                    frameRotation, anchor, rotation, position, bodyTransform, body);
        }
        AbstractBox3DSample.dispose(jointDef, worldFrame, axisY, axisZ, anchorDef);
    }

    private static int colorFor(int role, int skinColor) {
        if(role == PANT) {
            return 0x1E90FF;
        }
        if(role == SHIRT) {
            return 0x48D1CC;
        }
        return skinColor;
    }

    private static B3Transform transform(float[] values, boolean normalize) {
        B3Vec3 position = new B3Vec3(values[0], values[1], values[2]);
        B3Quat rotation = quaternion(values);
        if(normalize) {
            rotation.Normalize();
        }
        B3Transform transform = new B3Transform(position, rotation);
        AbstractBox3DSample.dispose(rotation, position);
        return transform;
    }

    private static B3Quat quaternion(float[] values) {
        return new B3Quat(values[3], values[4], values[5], values[6]);
    }

    private static BoneDef bone(int parent, int jointType, float[] reference, float[] capsule,
            boolean filterByGroup, int colorRole, float[] localFrameA, float[] localFrameB,
            float swingLimitDegrees, float twistLowerDegrees, float twistUpperDegrees, float jointFriction) {
        return new BoneDef(parent, jointType, reference, capsule, filterByGroup, colorRole,
                localFrameA, localFrameB, swingLimitDegrees, twistLowerDegrees, twistUpperDegrees, jointFriction);
    }

    private static float[] frame(float px, float py, float pz, float qx, float qy, float qz, float qw) {
        return new float[] { px, py, pz, qx, qy, qz, qw };
    }

    private static float[] capsule(float x1, float y1, float z1, float x2, float y2, float z2, float radius) {
        return new float[] { x1, y1, z1, x2, y2, z2, radius };
    }

    private static final class BoneDef {
        final int parent;
        final int jointType;
        final float[] reference;
        final float[] capsule;
        final boolean filterByGroup;
        final int colorRole;
        final float[] localFrameA;
        final float[] localFrameB;
        final float swingLimitDegrees;
        final float twistLowerDegrees;
        final float twistUpperDegrees;
        final float jointFriction;

        BoneDef(int parent, int jointType, float[] reference, float[] capsule, boolean filterByGroup,
                int colorRole, float[] localFrameA, float[] localFrameB, float swingLimitDegrees,
                float twistLowerDegrees, float twistUpperDegrees, float jointFriction) {
            this.parent = parent;
            this.jointType = jointType;
            this.reference = reference;
            this.capsule = capsule;
            this.filterByGroup = filterByGroup;
            this.colorRole = colorRole;
            this.localFrameA = localFrameA;
            this.localFrameB = localFrameB;
            this.swingLimitDegrees = swingLimitDegrees;
            this.twistLowerDegrees = twistLowerDegrees;
            this.twistUpperDegrees = twistUpperDegrees;
            this.jointFriction = jointFriction;
        }
    }
}
