package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3HeightField;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact CreateWavePile scene from shared/determinism.c. */
final class DeterminismWavePileSample extends AbstractBox3DSample {
    private B3HeightField heightField;

    DeterminismWavePileSample() {
        SampleRandom random = new SampleRandom(52977);
        B3Vec3 fieldScale = new B3Vec3(1.0f, 0.6f, 1.0f);
        heightField = B3HeightField.CreateWave(21, 21, fieldScale, 0.08f, 0.06f, false);
        B3Body ground = createBody(B3.StaticBody(), -10.0f, 0.0f, -10.0f, null);
        B3ShapeDef groundShapeDef = new B3ShapeDef();
        dispose(ground.CreateHeightFieldShape(groundShapeDef, heightField), groundShapeDef, ground, fieldScale);

        B3Hull rock = B3Hull.CreateRock(0.55f);
        B3Hull box = B3Hull.CreateBox(0.45f, 0.3f, 0.55f);
        B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(zero, 0.5f);
        B3Vec3 capsuleA = new B3Vec3(0.0f, -0.3f, 0.0f);
        B3Vec3 capsuleB = new B3Vec3(0.0f, 0.3f, 0.0f);
        B3Capsule capsule = new B3Capsule(capsuleA, capsuleB, 0.35f);

        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetRollingResistance(0.3f);
        shapeDef.SetBaseMaterial(material);

        int index = 0;
        for(int layer = 0; layer < 4; layer++) {
            for(int i = 0; i < 5; i++) {
                for(int j = 0; j < 5; j++) {
                    float jitterX = random.nextFloat(-0.3f, 0.3f);
                    float jitterY = random.nextFloat(-0.3f, 0.3f);
                    float jitterZ = random.nextFloat(-0.3f, 0.3f);
                    B3Vec3 position = new B3Vec3(1.7f * (i - 2.0f) + jitterX,
                            2.5f + 1.6f * layer + 0.3f * jitterY, 1.7f * (j - 2.0f) + jitterZ);
                    B3Quat rotation = random.nextQuaternion();
                    bodyDef.SetPosition(position);
                    bodyDef.SetRotation(rotation);
                    B3Body body = world().CreateBody(bodyDef);
                    switch(index & 3) {
                        case 0:
                            dispose(body.CreateSphereShape(shapeDef, sphere));
                            break;
                        case 1:
                            dispose(body.CreateCapsuleShape(shapeDef, capsule));
                            break;
                        case 2:
                            dispose(body.CreateHullShape(shapeDef, box));
                            break;
                        default:
                            dispose(body.CreateHullShape(shapeDef, rock));
                            break;
                    }
                    dispose(body, rotation, position);
                    index++;
                }
            }
        }
        dispose(material, shapeDef, bodyDef, capsule, capsuleB, capsuleA, sphere, zero, box, rock);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(heightField);
    }
}
