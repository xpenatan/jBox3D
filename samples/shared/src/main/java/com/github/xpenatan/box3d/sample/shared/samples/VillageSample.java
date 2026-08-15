package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Compound;
import com.github.xpenatan.box3d.B3CompoundDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3ShapeProxy;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3SurfaceMaterialArray;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerInput;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerTarget;

final class VillageSample extends AbstractBox3DSample {
    private static final int GRID_COUNT = 200;
    private static final float CELL_HALF_WIDTH = 4.0f;
    private static final int RANDOM_LIMIT = 32767;

    private int randomSeed = 12345;
    private B3Compound compound;
    private final ExactCharacterMover mover;
    private final B3Vec3 rayOrigin;
    private final B3Vec3Array proxyPoints;
    private final B3ShapeProxy shapeCastProxy;
    private final B3ShapeProxy overlapProxy;
    private Box3DPlayerInput playerInput;
    private boolean thirdPerson;

    VillageSample() {
        mover = new ExactCharacterMover(this, 0.0f, 10.0f, 0.0f);
        int capsuleCapacity = GRID_COUNT * GRID_COUNT / 8 + 1;
        int hullCount = GRID_COUNT * GRID_COUNT;
        int sphereCapacity = GRID_COUNT * GRID_COUNT / 8 + 1;
        int meshGridCount = GRID_COUNT / 4;
        int meshCount = meshGridCount * meshGridCount;

        B3CompoundDef compoundDef = new B3CompoundDef(capsuleCapacity, hullCount, meshCount, sphereCapacity);
        B3SurfaceMaterial material = new B3SurfaceMaterial();
        B3Hull terrainCell = B3Hull.CreateBox(CELL_HALF_WIDTH, 0.5f * CELL_HALF_WIDTH, CELL_HALF_WIDTH);
        B3Quat identityRotation = new B3Quat();
        B3Vec3 origin = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Transform transform = new B3Transform(origin, identityRotation);

        int capsuleIndex = 0;
        int sphereIndex = 0;
        for(int i = 0; i < GRID_COUNT; i++) {
            float x = (2.0f * i - GRID_COUNT) * CELL_HALF_WIDTH;
            for(int j = 0; j < GRID_COUNT; j++) {
                float z = (2.0f * j - GRID_COUNT) * CELL_HALF_WIDTH;
                float y = randomFloatRange(-0.25f, 0.125f) * CELL_HALF_WIDTH;
                B3Vec3 position = new B3Vec3(x, y, z);
                transform.SetP(position);

                if((i & 1) != 0 && (j & 1) != 0) {
                    B3Vec3 point1 = randomVec3(x - CELL_HALF_WIDTH, y + CELL_HALF_WIDTH, z - CELL_HALF_WIDTH,
                            x + CELL_HALF_WIDTH, y + 2.0f * CELL_HALF_WIDTH, z + CELL_HALF_WIDTH);
                    B3Vec3 point2 = randomVec3(x - CELL_HALF_WIDTH, y + CELL_HALF_WIDTH, z - CELL_HALF_WIDTH,
                            x + CELL_HALF_WIDTH, y + 2.0f * CELL_HALF_WIDTH, z + CELL_HALF_WIDTH);
                    float radius = randomFloatRange(0.1f, 0.5f);
                    if(capsuleIndex < sphereIndex) {
                        B3Capsule capsule = new B3Capsule(point1, point2, radius);
                        compoundDef.AddCapsule(capsule, material);
                        capsuleIndex++;
                        dispose(capsule);
                    }
                    else {
                        B3Sphere sphere = new B3Sphere(point1, radius);
                        compoundDef.AddSphere(sphere, material);
                        sphereIndex++;
                        dispose(sphere);
                    }
                    dispose(point2, point1);
                }

                compoundDef.AddHull(terrainCell, transform, material);
                dispose(position);
            }
        }

        B3Mesh buildingMesh = createBuildingMesh();
        B3SurfaceMaterialArray meshMaterials = createMeshMaterials(buildingMesh.GetMaterialCount());
        float buildingSpacing = 4.0f * CELL_HALF_WIDTH;
        int meshIndex = 0;
        for(int i = 0; i < meshGridCount; i++) {
            float x = (2.0f * i - meshGridCount) * buildingSpacing + 0.5f * buildingSpacing;
            for(int j = 0; j < meshGridCount; j++) {
                float z = (2.0f * j - meshGridCount) * buildingSpacing + 0.5f * buildingSpacing;
                B3Vec3 position = new B3Vec3(x, 0.5f * CELL_HALF_WIDTH, z);
                B3Quat rotation = rotationY(randomFloatRange(-(float)Math.PI, (float)Math.PI));
                transform.SetP(position);
                transform.SetQ(rotation);

                float scaleX = randomFloatRange(0.5f, 2.0f);
                float scaleY = randomFloatRange(0.5f, 2.0f);
                float scaleZ = randomFloatRange(0.5f, 2.0f);
                if((meshIndex & 1) != 0) {
                    scaleX = -scaleX;
                }
                if((meshIndex & 3) != 0) {
                    scaleZ = -scaleZ;
                }
                B3Vec3 scale = new B3Vec3(scaleX, scaleY, scaleZ);
                compoundDef.AddMesh(buildingMesh, transform, scale, meshMaterials);
                meshIndex++;
                dispose(scale, rotation, position);
            }
        }

        compound = B3Compound.CreateFromDef(compoundDef);
        B3Quat groundRotation = rotationY(-1.15f * (float)Math.PI);
        B3BodyDef bodyDef = bodyDef(B3.StaticBody(), -1.0f, -0.5f, 2.0f, groundRotation);
        B3Body ground = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        dispose(ground.CreateBakedCompoundShape(shapeDef, compound));

        dispose(shapeDef, bodyDef, groundRotation, meshMaterials, buildingMesh, transform, origin, identityRotation,
                terrainCell, material, compoundDef);

        float worldWidth = 2.0f * GRID_COUNT * CELL_HALF_WIDTH;
        rayOrigin = new B3Vec3(-0.45f * worldWidth, 20.0f, -0.45f * worldWidth);
        proxyPoints = new B3Vec3Array(1);
        B3Vec3 proxyPoint = new B3Vec3(0.0f, 0.0f, 0.0f);
        proxyPoints.SetValue(0, proxyPoint);
        shapeCastProxy = new B3ShapeProxy(proxyPoints, 1, 0.25f);
        overlapProxy = new B3ShapeProxy(proxyPoints, 1, 0.3f);
        dispose(proxyPoint);
    }

    @Override
    public void step(float deltaSeconds) {
        world().ClearDebugOverlay();
        mover.step(deltaSeconds, 0L, true, playerInput, thirdPerson);
        addDebugAxes(0.0f, 0.01f, 0.0f, 4.0f);

        B3Vec3 translation = new B3Vec3(10.0f, -40.0f, -5.0f);
        B3QueryFilter filter = new B3QueryFilter();

        B3Vec3 rayEnd = offset(rayOrigin, translation);
        world().AddDebugSegment(rayOrigin, rayEnd, 0xF0F8FF);
        B3RayResult rayResult = world().CastRayClosest(rayOrigin, translation, filter);
        if(rayResult.GetHit()) {
            B3Vec3 point = rayResult.GetPoint();
            B3Vec3 normal = rayResult.GetNormal();
            B3Vec3 normalEnd = new B3Vec3(point.GetX() + 0.5f * normal.GetX(),
                    point.GetY() + 0.5f * normal.GetY(), point.GetZ() + 0.5f * normal.GetZ());
            world().AddDebugSegment(point, normalEnd, 0xFFFF00);
            world().AddDebugPoint(point, 8.0f, 0xF08080);
            dispose(normalEnd, normal, point);
        }

        B3Vec3 shapeOrigin = new B3Vec3(rayOrigin.GetX() - 1.0f, rayOrigin.GetY(), rayOrigin.GetZ() - 1.0f);
        B3Vec3 shapeEnd = offset(shapeOrigin, translation);
        world().AddDebugSegment(shapeOrigin, shapeEnd, 0xF0F8FF);
        B3RayResult shapeResult = world().CastShapeClosest(shapeOrigin, shapeCastProxy, translation, filter, true);
        if(shapeResult.GetHit()) {
            B3Vec3 position = new B3Vec3(
                    shapeOrigin.GetX() + shapeResult.GetFraction() * translation.GetX(),
                    shapeOrigin.GetY() + shapeResult.GetFraction() * translation.GetY(),
                    shapeOrigin.GetZ() + shapeResult.GetFraction() * translation.GetZ());
            B3Vec3 point = shapeResult.GetPoint();
            B3Vec3 normal = shapeResult.GetNormal();
            B3Vec3 normalEnd = new B3Vec3(point.GetX() + 0.5f * normal.GetX(),
                    point.GetY() + 0.5f * normal.GetY(), point.GetZ() + 0.5f * normal.GetZ());
            world().AddDebugSegment(point, normalEnd, 0xFFFF00);
            world().AddDebugPoint(point, 8.0f, 0xF08080);
            world().AddDebugSphere(position, 0.25f, 0xDA70D6, 1.0f);
            dispose(normalEnd, normal, point, position);
        }

        B3Vec3 overlapOrigin = new B3Vec3(rayOrigin.GetX() - 1.0f, 2.0f, rayOrigin.GetZ() - 1.0f);
        boolean overlap = world().OverlapShape(overlapOrigin, overlapProxy, filter);
        world().AddDebugSphere(overlapOrigin, 0.3f, overlap ? 0x8B008B : 0x8FBC8F, 1.0f);

        float worldLimit = 0.45f * 2.0f * GRID_COUNT * CELL_HALF_WIDTH;
        float x = rayOrigin.GetX();
        float z = rayOrigin.GetZ();
        if(x > worldLimit) {
            x = -worldLimit;
            z += 8.0f;
        }
        if(z > worldLimit) {
            z = -worldLimit;
        }
        rayOrigin.Set(x + 2.0f * deltaSeconds, rayOrigin.GetY(), z);

        dispose(overlapOrigin, shapeResult, shapeEnd, shapeOrigin, rayResult, rayEnd, filter, translation);
        super.step(deltaSeconds);
    }

    @Override
    public void dispose() {
        super.dispose();
        mover.dispose();
        dispose(overlapProxy, shapeCastProxy, proxyPoints, rayOrigin, compound);
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
        mover.getPosition(target);
        return true;
    }

    private B3Mesh createBuildingMesh() {
        String obj = SampleAssets.readUtf8("data/meshes/building.obj");
        B3Mesh mesh = B3Mesh.CreateFromObj(obj, 1.0f, false, false, true, true, 0.002f);
        if(!mesh.IsValid()) {
            dispose(mesh);
            throw new IllegalStateException("Box3D could not create data/meshes/building.obj");
        }
        return mesh;
    }

    private B3SurfaceMaterialArray createMeshMaterials(int materialCount) {
        B3SurfaceMaterialArray materials = new B3SurfaceMaterialArray(materialCount);
        for(int i = 0; i < materialCount; i++) {
            B3SurfaceMaterial material = new B3SurfaceMaterial();
            if(i == 0) {
                material.SetFriction(0.0f);
            }
            else if(i == 1) {
                material.SetRestitution(0.5f);
            }
            material.SetUserMaterialId(i + 42L);
            materials.SetValue(i, material);
            dispose(material);
        }
        return materials;
    }

    private B3Vec3 randomVec3(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new B3Vec3(randomFloatRange(minX, maxX), randomFloatRange(minY, maxY), randomFloatRange(minZ, maxZ));
    }

    private float randomFloatRange(float minimum, float maximum) {
        int x = randomSeed;
        x ^= x << 13;
        x ^= x >>> 17;
        x ^= x << 5;
        randomSeed = x;
        float ratio = (float)(x & RANDOM_LIMIT) / RANDOM_LIMIT;
        return (maximum - minimum) * ratio + minimum;
    }

    private static B3Vec3 offset(B3Vec3 origin, B3Vec3 translation) {
        return new B3Vec3(origin.GetX() + translation.GetX(), origin.GetY() + translation.GetY(),
                origin.GetZ() + translation.GetZ());
    }
}
