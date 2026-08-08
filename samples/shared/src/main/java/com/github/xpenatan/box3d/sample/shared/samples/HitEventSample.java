package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3SurfaceMaterialArray;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WeldJointDef;

final class HitEventSample extends AbstractBox3DSample {
    private B3Mesh gridMesh;

    HitEventSample() {
        createGround();

        B3WeldJointDef jointDef = new B3WeldJointDef();
        jointDef.SetAngularHertz(10.0f);
        jointDef.SetAngularDampingRatio(2.0f);

        float radius = 0.75f;
        float y = radius;
        float length = 1.5f;
        float offset = 0.05f;
        int shapeCount = 22;
        int shapesPerBody = 3;
        float velocityScale = 0.5f;

        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetEnableHitEvents(true);
        shapeDef.SetUpdateBodyMass(false);
        B3SurfaceMaterial material = new B3SurfaceMaterial();
        material.SetRollingResistance(0.2f);
        material.SetUserMaterialId(42L);
        shapeDef.SetBaseMaterial(material);

        B3Body body = createBody(B3.DynamicBody(), 0.0f, 0.0f, 0.0f, null);
        for(int i = 0; i < shapeCount; i++) {
            B3Vec3 center1 = new B3Vec3(offset, y, 0.0f);
            B3Vec3 center2 = new B3Vec3(0.0f, y + length, -offset);
            B3Capsule capsule = new B3Capsule(center1, center2, radius);
            dispose(body.CreateCapsuleShape(shapeDef, capsule), capsule, center2, center1);

            if((i + 1) % shapesPerBody == 0 || i == shapeCount - 1) {
                body.ApplyMassFromShapes();
                B3Vec3 center = body.GetWorldCenter();
                B3Vec3 omega = new B3Vec3(0.0f, 0.0f, -velocityScale);
                B3Vec3 velocity = new B3Vec3(velocityScale * center.GetY(),
                        -velocityScale * center.GetX(), 0.0f);
                body.SetAngularVelocity(omega);
                body.SetLinearVelocity(velocity);
                dispose(velocity, omega, center);

                if(i < shapeCount - 1) {
                    B3Body previousBody = body;
                    body = createBody(B3.DynamicBody(), 0.0f, 0.0f, 0.0f, null);
                    B3Vec3 anchor = new B3Vec3(0.0f, y + length + radius, 0.0f);
                    jointDef.SetBodyIdA(previousBody.GetId());
                    jointDef.SetBodyIdB(body.GetId());
                    jointDef.SetLocalPositionA(anchor);
                    jointDef.SetLocalPositionB(anchor);
                    dispose(world().CreateWeldJoint(jointDef), anchor, previousBody);
                    velocityScale *= 0.75f;
                }
            }

            y += length + 2.0f * radius;
            radius *= 0.95f;
            offset = -offset;
        }
        dispose(body, material, shapeDef, jointDef);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(gridMesh);
    }

    private void createGround() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        gridMesh = B3Mesh.CreateGrid(20, 20, 8.0f, 6, true);
        B3SurfaceMaterialArray materials = new B3SurfaceMaterialArray(6);
        for(int i = 0; i < 6; i++) {
            B3SurfaceMaterial material = new B3SurfaceMaterial();
            material.SetUserMaterialId(i + 1L);
            materials.SetValue(i, material);
            dispose(material);
        }
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShapeWithMaterials(shapeDef, gridMesh, scale, materials));
        dispose(scale, shapeDef, materials, ground);
    }
}
