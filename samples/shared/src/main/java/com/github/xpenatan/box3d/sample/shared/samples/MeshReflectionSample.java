package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3SurfaceMaterialArray;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default scene from Mesh/Reflection. */
final class MeshReflectionSample extends AbstractBox3DSample {
    private final ExactHuman[] humans = new ExactHuman[20];
    private final B3Mesh gridMesh;
    private final B3Mesh buildingMesh;

    MeshReflectionSample() {
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);

        gridMesh = B3Mesh.CreateGrid(20, 20, 2.0f, 2, true);
        B3Body gridBody = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        dispose(gridBody.CreateMeshShape(shapeDef, gridMesh, one), gridBody);

        buildingMesh = B3Mesh.CreateFromObj(SampleAssets.readUtf8("data/meshes/building.obj"),
                1.0f, false, false, true, true, 0.002f);
        B3SurfaceMaterialArray materials = new B3SurfaceMaterialArray(3);
        B3SurfaceMaterial material0 = new B3SurfaceMaterial();
        material0.SetFriction(0.6f);
        B3SurfaceMaterial material1 = new B3SurfaceMaterial();
        material1.SetFriction(0.0f);
        material1.SetRestitution(0.95f);
        material1.SetUserMaterialId(1L);
        B3SurfaceMaterial material2 = new B3SurfaceMaterial();
        material2.SetFriction(0.2f);
        material2.SetRestitution(0.2f);
        material2.SetUserMaterialId(2L);
        materials.SetValue(0, material0);
        materials.SetValue(1, material1);
        materials.SetValue(2, material2);
        B3Body leftBuilding = createBody(B3.StaticBody(), -10.0f, 0.0f, 0.0f, null);
        B3Body rightBuilding = createBody(B3.StaticBody(), 10.0f, 0.0f, 0.0f, null);
        B3Vec3 reflected = new B3Vec3(-1.0f, 1.0f, 1.0f);
        dispose(leftBuilding.CreateMeshShapeWithMaterials(shapeDef, buildingMesh, one, materials), leftBuilding);
        dispose(rightBuilding.CreateMeshShapeWithMaterials(shapeDef, buildingMesh, reflected, materials), rightBuilding);

        B3SurfaceMaterial dynamicMaterial = shapeDef.GetBaseMaterial();
        dynamicMaterial.SetRollingResistance(0.2f);
        dynamicMaterial.SetUserMaterialId(42L);
        shapeDef.SetBaseMaterial(dynamicMaterial);
        B3Body sphereBody = createBody(B3.DynamicBody(), 6.0f, 15.0f, 0.0f, null);
        B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(zero, 0.5f);
        dispose(sphereBody.CreateSphereShape(shapeDef, sphere), sphereBody, sphere);

        dynamicMaterial.SetUserMaterialId(11L);
        shapeDef.SetBaseMaterial(dynamicMaterial);
        B3Body capsuleBody = createBody(B3.DynamicBody(), 9.0f, 15.0f, 0.0f, null);
        B3Vec3 capsuleP1 = new B3Vec3(-0.5f, 0.5f, 0.0f);
        B3Vec3 capsuleP2 = new B3Vec3(0.5f, 0.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(capsuleP1, capsuleP2, 0.25f);
        dispose(capsuleBody.CreateCapsuleShape(shapeDef, capsule), capsuleBody, capsule, capsuleP2, capsuleP1);

        dynamicMaterial.SetUserMaterialId(555L);
        shapeDef.SetBaseMaterial(dynamicMaterial);
        B3Body hullBody = createBody(B3.DynamicBody(), 12.0f, 15.0f, 0.0f, null);
        B3Hull box = B3Hull.CreateBox(0.25f, 0.5f, 0.75f);
        dispose(hullBody.CreateHullShape(shapeDef, box), hullBody, box);

        for(int i = 0; i < humans.length; i++) {
            humans[i] = ExactHuman.create(world(), -14.0f + 1.5f * i, 8.0f, 0.0f,
                    5.0f, 1.0f, 0.7f, i, false);
        }
        dispose(zero, dynamicMaterial, reflected, material2, material1, material0, materials, one, shapeDef);
    }

    @Override
    public void dispose() {
        for(ExactHuman human : humans) {
            if(human != null) {
                human.destroy();
            }
        }
        super.dispose();
        dispose(buildingMesh, gridMesh);
    }
}
