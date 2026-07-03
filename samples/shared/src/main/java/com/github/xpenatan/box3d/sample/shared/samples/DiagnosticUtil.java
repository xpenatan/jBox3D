package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WheelJointDef;

final class DiagnosticUtil {
    private DiagnosticUtil() {
    }

    static void addCapsuleTargets(AbstractBox3DSample sample) {
        for(int i = 0; i < 8; i++) {
            sample.addDynamicCapsule(-8.0f + i * 2.0f, 3.5f + (i % 3) * 0.8f, 0.0f, 0.55f, 0.22f);
        }
    }

    static void addMixedObstacleField(AbstractBox3DSample sample) {
        for(int i = 0; i < 16; i++) {
            float x = -10.0f + (i % 8) * 2.8f;
            float z = -3.0f + (i / 8) * 6.0f;
            if(i % 3 == 0) {
                sample.addDynamicSphere(x, 3.0f + (i % 4), z, 0.45f);
            }
            else if(i % 3 == 1) {
                sample.addDynamicCapsule(x, 3.0f + (i % 4), z, 0.55f, 0.2f);
            }
            else {
                sample.addDynamicBox(x, 3.0f + (i % 4), z, 0.45f, 0.45f, 0.45f);
            }
        }
    }

    static void addFallingMix(AbstractBox3DSample sample, int count, float baseY) {
        for(int i = 0; i < count; i++) {
            float x = -8.0f + (i % 8) * 2.2f;
            float y = baseY + (i / 8) * 1.2f;
            float z = -3.0f + (i % 4) * 2.0f;
            if(i % 3 == 0) {
                sample.addDynamicSphere(x, y, z, 0.35f);
            }
            else if(i % 3 == 1) {
                sample.addDynamicCapsule(x, y, z, 0.35f, 0.18f);
            }
            else {
                sample.addDynamicBox(x, y, z, 0.35f, 0.35f, 0.35f);
            }
        }
    }

    static void addPairBase(AbstractBox3DSample sample) {
        sample.addGroundBox(10.0f);
        B3Vec3 left = new B3Vec3(8.0f, 0.0f, 0.0f);
        B3Vec3 right = new B3Vec3(-8.0f, 0.0f, 0.0f);
        B3Body a = sample.addDynamicBox(-3.0f, 8.0f, 0.0f, 0.2f, 0.2f, 0.2f);
        B3Body b = sample.addDynamicBox(3.0f, 8.0f, 0.0f, 0.2f, 0.2f, 0.2f);
        a.SetLinearVelocity(left);
        b.SetLinearVelocity(right);
        AbstractBox3DSample.dispose(right, left);
    }

    static void addTriangleHull(AbstractBox3DSample sample, float x, float y, float z) {
        B3Hull hull = B3Hull.CreateCone(0.7f, 0.7f, 0.7f, 4);
        sample.addHull(hull, B3.DynamicBody(), x, y, z, null, 1.0f, 0.6f, 0.0f, 0.0f);
        AbstractBox3DSample.dispose(hull);
    }

    static void addMeshBowl(AbstractBox3DSample sample, int count, float spacing) {
        sample.addGroundBox(24.0f);
        for(int i = -count; i <= count; i++) {
            float x = i * spacing;
            float y = 0.6f + Math.abs(i) * 0.12f;
            B3Quat rotation = sample.rotationZ(-i * 0.035f);
            sample.addStaticBox(x, y, -4.0f, spacing * 0.55f, 0.15f, 0.9f, rotation);
            sample.addStaticBox(x, y, 4.0f, spacing * 0.55f, 0.15f, 0.9f, rotation);
            AbstractBox3DSample.dispose(rotation);
        }
    }
}
