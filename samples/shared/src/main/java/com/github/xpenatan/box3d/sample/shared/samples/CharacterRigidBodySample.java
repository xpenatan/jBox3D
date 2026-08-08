package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3HeightField;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact initial scene from {@code RigidBodyCharacter} in {@code sample_character.cpp}. */
final class CharacterRigidBodySample extends AbstractBox3DSample {
    private static final float SOURCE_UNIT = 0.0254f;
    private static final float BODY_RADIUS = 16.0f * SOURCE_UNIT;
    private static final float TOTAL_HEIGHT = 72.0f * SOURCE_UNIT;
    private static final float FEET_HEIGHT = TOTAL_HEIGHT * 0.5f;
    private static final float CHARACTER_MASS = 500.0f;

    private B3Mesh levelMesh;
    private B3Mesh stairs;
    private B3Mesh building;
    private B3Mesh voxel01;
    private B3Mesh voxel02;
    private B3HeightField heightField;
    private long characterBodyId;
    private long feetShapeId;

    CharacterRigidBodySample() {
        createCharacter();
        levelMesh = loadMesh("data/meshes/test_map01.obj");
        createMeshBody(levelMesh, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        stairs = loadMesh("data/meshes/stairs.obj");
        createMeshBody(stairs, -10.0f, 0.0f, 0.0f, 0.75f, 0.75f, -1.5f);
        building = loadMesh("data/meshes/building.obj");
        createMeshBody(building, -5.0f, 0.0f, -10.0f, 1.0f, 1.0f, 1.0f);
        voxel01 = loadMesh("data/meshes/voxel_mesh_01.obj");
        createMeshBody(voxel01, 10.0f, 0.0f, -10.0f, 1.0f, 1.0f, 1.0f);
        voxel02 = loadMesh("data/meshes/voxel_mesh_02.obj");
        createMeshBody(voxel02, 10.0f, 0.0f, 10.0f, 1.0f, 1.0f, 1.0f);

        B3Vec3 unitScale = new B3Vec3(1.0f, 1.0f, 1.0f);
        heightField = B3HeightField.CreateWave(50, 50, unitScale, 0.02f, 0.04f, true);
        B3Body heightBody = createBody(B3.StaticBody(), 20.0f, 0.0f, 0.0f, null);
        B3ShapeDef heightDef = new B3ShapeDef();
        dispose(heightBody.CreateHeightFieldShape(heightDef, heightField), heightBody, heightDef, unitScale);

        B3Quat rampRotation = rotationZ(-20.0f * (float)Math.PI / 180.0f);
        createColoredBox(B3.StaticBody(), 6.0f, 1.0f, 4.0f, 3.0f, 0.15f, 1.5f,
                rampRotation, 0x6B8E23);
        dispose(rampRotation);
        B3Quat steepRotation = rotationZ(-50.0f * (float)Math.PI / 180.0f);
        createColoredBox(B3.StaticBody(), 6.0f, 2.0f, -4.0f, 2.5f, 0.15f, 1.5f,
                steepRotation, 0xCD5C5C);
        dispose(steepRotation);

        for(int i = 0; i < 3; ++i) {
            createColoredBox(B3.StaticBody(), -4.0f + 3.5f * i, 1.2f, -5.0f,
                    1.2f, 0.15f, 1.2f, null, 0x708090);
        }
        for(int i = 0; i < 5; ++i) {
            float lipHeight = 0.05f + 0.08f * i;
            createColoredBox(B3.StaticBody(), -8.0f, lipHeight, -1.0f + 2.0f * i,
                    1.0f, lipHeight, 0.6f, null, 0x6495ED);
        }
        createColoredBox(B3.StaticBody(), 0.0f, 1.5f, 10.0f, 4.0f, 1.5f, 0.2f,
                null, 0x2F4F4F);
        for(int i = 0; i < 3; ++i) {
            createColoredBox(B3.DynamicBody(), 3.0f + 1.5f * i, 0.5f, 0.0f,
                    0.4f, 0.4f, 0.4f, null, 0xFFD700);
        }
        createColoredSphere(-3.0f, 1.0f, 0.0f, 0.5f, 0xFFA500);
    }

    private void createCharacter() {
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3Vec3 position = new B3Vec3(7.5f, 2.0f, 9.0f);
        bodyDef.SetPosition(position);
        B3MotionLocks locks = new B3MotionLocks();
        locks.SetAngularX(true);
        locks.SetAngularY(true);
        locks.SetAngularZ(true);
        bodyDef.SetMotionLocks(locks);
        bodyDef.SetEnableSleep(false);
        bodyDef.SetEnableContactRecycling(false);
        bodyDef.SetGravityScale(1.5f);
        B3Body character = world().CreateBody(bodyDef);
        characterBodyId = character.GetId();

        float halfExtentX = BODY_RADIUS * 0.5f;
        float halfExtentY = FEET_HEIGHT * 0.5f;
        float halfExtentZ = BODY_RADIUS * 0.5f;
        float feetVolume = 8.0f * halfExtentX * halfExtentY * halfExtentZ;
        B3ShapeDef feetDef = coloredShapeDef((CHARACTER_MASS * 0.4f) / feetVolume, 0.0f, 0x32CD32);
        B3Vec3 feetOffset = new B3Vec3(0.0f, -TOTAL_HEIGHT * 0.5f + halfExtentY, 0.0f);
        B3Quat identity = new B3Quat();
        B3Transform feetTransform = new B3Transform(feetOffset, identity);
        B3Hull feetHull = B3Hull.CreateTransformedBox(halfExtentX, halfExtentY, halfExtentZ, feetTransform);
        B3Shape feetShape = character.CreateHullShape(feetDef, feetHull);
        feetShapeId = feetShape.GetId();
        dispose(feetShape, feetHull, feetTransform, feetOffset, feetDef);

        float capsuleRadius = BODY_RADIUS * 0.707f;
        float capsuleBottom = -TOTAL_HEIGHT * 0.5f + FEET_HEIGHT * 0.5f + capsuleRadius;
        float capsuleTop = TOTAL_HEIGHT * 0.5f - capsuleRadius;
        float capsuleHeight = capsuleTop - capsuleBottom;
        float capsuleVolume = (float)Math.PI * capsuleRadius * capsuleRadius
                * (capsuleHeight + 4.0f * capsuleRadius / 3.0f);
        B3ShapeDef capsuleDef = coloredShapeDef((CHARACTER_MASS * 0.6f) / capsuleVolume, 0.0f, 0x6495ED);
        B3Vec3 center1 = new B3Vec3(0.0f, capsuleBottom, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, capsuleTop, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, capsuleRadius);
        dispose(character.CreateCapsuleShape(capsuleDef, capsule), capsule, center2, center1, capsuleDef);

        B3Vec3 inertiaX = character.GetLocalRotationalInertiaColumnX();
        B3Vec3 inertiaY = character.GetLocalRotationalInertiaColumnY();
        B3Vec3 inertiaZ = character.GetLocalRotationalInertiaColumnZ();
        B3Vec3 massCenter = new B3Vec3(0.0f, 0.0f, 0.0f);
        character.SetMassData(character.GetMass(), massCenter, inertiaX, inertiaY, inertiaZ);
        dispose(massCenter, inertiaZ, inertiaY, inertiaX, character, identity, locks, position, bodyDef);
    }

    private void createMeshBody(B3Mesh mesh, float x, float y, float z, float sx, float sy, float sz) {
        B3Body body = createBody(B3.StaticBody(), x, y, z, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(sx, sy, sz);
        dispose(body.CreateMeshShape(shapeDef, mesh, scale), body, scale, shapeDef);
    }

    private void createColoredBox(int bodyType, float x, float y, float z, float hx, float hy, float hz,
            B3Quat rotation, int color) {
        B3BodyDef bodyDef = bodyDef(bodyType, x, y, z, rotation);
        B3Body body = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = coloredShapeDef(1.0f, 0.6f, color);
        B3Hull hull = B3Hull.CreateBox(hx, hy, hz);
        dispose(body.CreateHullShape(shapeDef, hull), hull, shapeDef, body, bodyDef);
    }

    private void createColoredSphere(float x, float y, float z, float radius, int color) {
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), x, y, z, null);
        B3Body body = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = coloredShapeDef(1.0f, 0.6f, color);
        B3Vec3 center = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(center, radius);
        dispose(body.CreateSphereShape(shapeDef, sphere), sphere, center, shapeDef, body, bodyDef);
    }

    private static B3ShapeDef coloredShapeDef(float density, float friction, int color) {
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetDensity(density);
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetFriction(friction);
        material.SetRestitution(0.0f);
        material.SetCustomColor(color);
        shapeDef.SetBaseMaterial(material);
        dispose(material);
        return shapeDef;
    }

    private static B3Mesh loadMesh(String path) {
        B3Mesh mesh = B3Mesh.CreateFromObj(SampleAssets.readUtf8(path), 1.0f, false, false, true, true, 0.002f);
        if(!mesh.IsValid()) {
            dispose(mesh);
            throw new IllegalStateException("Box3D could not create " + path);
        }
        return mesh;
    }

    @Override
    public void step(float deltaSeconds) {
        B3Body character = new B3Body(characterBodyId);
        B3Shape feet = new B3Shape(feetShapeId);
        if(character.IsValid() && feet.IsValid()) {
            feet.SetFriction(0.0f);
            character.SetGravityScale(1.5f);
            character.SetLinearDamping(0.1f);
        }
        dispose(feet, character);
        super.step(deltaSeconds);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(heightField, voxel02, voxel01, building, stairs, levelMesh);
    }
}
