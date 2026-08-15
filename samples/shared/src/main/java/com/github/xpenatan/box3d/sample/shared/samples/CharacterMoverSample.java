package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3Filter;
import com.github.xpenatan.box3d.B3HeightField;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3MoverCollision;
import com.github.xpenatan.box3d.B3MoverPlaneResult;
import com.github.xpenatan.box3d.B3PlaneSolverResult;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3RevoluteJointDef;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerInput;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerTarget;

/** Direct port of the default, no-input BasicMover path from the pinned Box3D sample. */
final class CharacterMoverSample extends AbstractBox3DSample {
    private B3Mesh levelMesh;
    private B3Mesh stairs;
    private B3Mesh torus;
    private B3HeightField heightField;
    private final ExactCharacterMover mover;
    private long ignoreShapeId;
    private Box3DPlayerInput playerInput;
    private boolean thirdPerson;

    CharacterMoverSample() {
        mover = new ExactCharacterMover(this, 7.5f, 0.75f, 9.0f);

        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 unitScale = new B3Vec3(1.0f, 1.0f, 1.0f);

        levelMesh = loadMesh("data/meshes/test_map01.obj");
        B3Body levelBody = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        dispose(levelBody.CreateMeshShape(shapeDef, levelMesh, unitScale));
        addBoxShape(levelBody, 1.0f, 1.0f, 1.0f, 4.0f, 1.0f, 14.0f, null,
                1.0f, 0.6f, 0.0f, 0.0f);
        addBoxShape(levelBody, 1.0f, 1.0f, 1.0f, 4.0f, 1.0f, 13.95f, null,
                1.0f, 0.6f, 0.0f, 0.0f);
        B3Quat obstacleRotation = rotationY(0.1f * (float)Math.PI);
        addBoxShape(levelBody, 1.0f, 1.0f, 1.0f, 5.8f, 1.0f, 13.7f, obstacleRotation,
                1.0f, 0.6f, 0.0f, 0.0f);
        dispose(obstacleRotation, levelBody);

        stairs = loadMesh("data/meshes/stairs.obj");
        B3Body stairsBody = createBody(B3.StaticBody(), -10.0f, 0.0f, 0.0f, null);
        B3Vec3 stairsScale = new B3Vec3(0.75f, 0.75f, -1.5f);
        dispose(stairsBody.CreateMeshShape(shapeDef, stairs, stairsScale), stairsBody, stairsScale);

        torus = B3Mesh.CreateTorus(10, 12, 2.0f, 1.0f);
        B3Quat torusRotation = rotationY(0.5f * (float)Math.PI);
        B3Body torusBody = createBody(B3.StaticBody(), -10.0f, 1.0f, -8.0f, torusRotation);
        B3Vec3 torusScale = new B3Vec3(-0.75f, 1.5f, 0.5f);
        dispose(torusBody.CreateMeshShape(shapeDef, torus, torusScale), torusBody, torusScale, torusRotation);

        B3Body heightBody = createBody(B3.StaticBody(), 20.0f, 0.0f, 0.0f, null);
        heightField = B3HeightField.CreateWave(50, 50, unitScale, 0.02f, 0.04f, true);
        dispose(heightBody.CreateHeightFieldShape(shapeDef, heightField), heightBody);

        createStaticCapsule(0.0f, 1.4f, 6.0f, 0L, 0xC71585);
        createStaticCapsule(0.0f, 1.4f, 5.0f, 2L, 0x32CD32);

        B3BodyDef sphereBodyDef = new B3BodyDef();
        sphereBodyDef.SetType(B3.DynamicBody());
        B3Vec3 spherePosition = new B3Vec3(7.0f, 5.0f, 0.0f);
        sphereBodyDef.SetPosition(spherePosition);
        B3Body sphereBody = world().CreateBody(sphereBodyDef);
        B3Vec3 sphereCenter = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Sphere sphere = new B3Sphere(sphereCenter, 0.5f);
        dispose(sphereBody.CreateSphereShape(shapeDef, sphere), sphereBody, sphere, sphereCenter,
                spherePosition, sphereBodyDef);

        createIgnoreBox();
        createDoor();
        dispose(unitScale, shapeDef);
    }

    private void createIgnoreBox() {
        B3Body body = createBody(B3.StaticBody(), 7.0f, 2.0f, -3.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetCustomColor(0xFFFAF0);
        shapeDef.SetBaseMaterial(material);
        B3Hull box = B3Hull.CreateBox(0.5f, 0.25f, 0.5f);
        B3Shape shape = body.CreateHullShape(shapeDef, box);
        ignoreShapeId = shape.GetId();
        dispose(shape, box, material, shapeDef, body);
    }

    private void createStaticCapsule(float x, float y, float z, long categoryBits, int color) {
        B3BodyDef bodyDef = new B3BodyDef();
        B3Vec3 position = new B3Vec3(x, y, z);
        bodyDef.SetPosition(position);
        B3Body body = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        if(categoryBits != 0L) {
            B3Filter filter = shapeDef.GetFilter();
            filter.SetCategoryBits(categoryBits);
            shapeDef.SetFilter(filter);
            dispose(filter);
        }
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetCustomColor(color);
        shapeDef.SetBaseMaterial(material);
        B3Vec3 center1 = new B3Vec3(0.0f, -0.5f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, 0.5f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, 0.3f);
        dispose(body.CreateCapsuleShape(shapeDef, capsule), body, capsule, center2, center1,
                material, shapeDef, position, bodyDef);
    }

    private void createDoor() {
        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        bodyDef.SetGravityScale(2.0f);
        B3Vec3 position = new B3Vec3(-2.0f, 1.6f, 0.0f);
        bodyDef.SetPosition(position);
        B3Body door = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetDensity(1000.0f);
        B3Hull box = B3Hull.CreateBox(0.75f, 1.5f, 0.1f);
        dispose(door.CreateHullShape(shapeDef, box));

        B3Vec3 axisZ = new B3Vec3(0.0f, 0.0f, 1.0f);
        B3Vec3 axisY = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Quat axisRotation = B3Quat.ComputeBetweenUnitVectors(axisZ, axisY);
        B3Vec3 localA = new B3Vec3(-2.75f, 1.6f, 0.0f);
        B3Vec3 localB = new B3Vec3(-0.75f, 0.0f, 0.0f);
        B3Transform frameA = new B3Transform(localA, axisRotation);
        B3Transform frameB = new B3Transform(localB, axisRotation);
        B3RevoluteJointDef jointDef = new B3RevoluteJointDef();
        jointDef.SetBodyIdA(ground.GetId());
        jointDef.SetBodyIdB(door.GetId());
        jointDef.SetLocalFrameA(frameA);
        jointDef.SetLocalFrameB(frameB);
        jointDef.SetEnableLimit(true);
        jointDef.SetLowerAngle(-0.5f * (float)Math.PI);
        jointDef.SetUpperAngle(0.5f * (float)Math.PI);
        jointDef.SetEnableSpring(true);
        jointDef.SetHertz(1.0f);
        jointDef.SetDampingRatio(0.5f);
        jointDef.SetEnableMotor(false);
        jointDef.SetMaxMotorTorque(100.0f);
        jointDef.SetDrawScale(2.0f);
        dispose(world().CreateRevoluteJoint(jointDef), jointDef, frameB, frameA, localB, localA,
                axisRotation, axisY, axisZ, box, shapeDef, door, position, bodyDef, ground);
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
        world().ClearDebugOverlay();
        mover.step(deltaSeconds, ignoreShapeId, true, playerInput, thirdPerson);
        addDebugAxes(0.0f, 0.0f, 0.02f, 2.0f);
        super.step(deltaSeconds);
    }

    @Override
    public void dispose() {
        super.dispose();
        mover.dispose();
        dispose(heightField, torus, stairs, levelMesh);
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
}
