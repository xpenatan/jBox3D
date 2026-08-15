package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Filter;
import com.github.xpenatan.box3d.B3HeightField;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3ShapeProxy;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerInput;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerTarget;

/** Exact initial scene from {@code RigidBodyCharacter} in {@code sample_character.cpp}. */
final class CharacterRigidBodySample extends AbstractBox3DSample {
    private static final float SOURCE_UNIT = 0.0254f;
    private static final float BODY_RADIUS = 16.0f * SOURCE_UNIT;
    private static final float TOTAL_HEIGHT = 72.0f * SOURCE_UNIT;
    private static final float FEET_HEIGHT = TOTAL_HEIGHT * 0.5f;
    private static final float CHARACTER_MASS = 500.0f;
    private static final float WALK_SPEED = 230.0f * SOURCE_UNIT;
    private static final float RUN_SPEED = 350.0f * SOURCE_UNIT;
    private static final float JUMP_SPEED = 300.0f * SOURCE_UNIT;
    private static final float CHARACTER_GRAVITY = 15.0f;
    private static final float JUMP_COOLDOWN_TIME = 0.2f;
    private static final float STEP_UP_HEIGHT = 18.0f * SOURCE_UNIT;
    private static final float STEP_DOWN_HEIGHT = 18.0f * SOURCE_UNIT;
    private static final float SKIN = 0.095f * SOURCE_UNIT;
    private static final float BRAKE_POWER = 0.2f;
    private static final float SURFACE_FRICTION = 0.6f;
    private static final float AIR_FRICTION = 0.1f;
    private static final float MAX_SLOPE_COS = 0.70710677f;
    private static final long CHARACTER_CATEGORY = 2L;

    private B3Mesh levelMesh;
    private B3Mesh stairs;
    private B3Mesh building;
    private B3Mesh voxel01;
    private B3Mesh voxel02;
    private B3HeightField heightField;
    private long characterBodyId;
    private long feetShapeId;
    private Box3DPlayerInput playerInput;
    private boolean thirdPerson;
    private boolean onGround;
    private boolean sprint;
    private boolean didStep;
    private float jumpCooldown;
    private float groundNormalX;
    private float groundNormalY = 1.0f;
    private float groundNormalZ;
    private float groundVelocityX;
    private float groundVelocityY;
    private float groundVelocityZ;
    private float stepPositionX;
    private float stepPositionY;
    private float stepPositionZ;
    private float lastWishVelocityX;
    private float lastWishVelocityY;
    private float lastWishVelocityZ;

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
        setCharacterFilter(feetDef);
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
        setCharacterFilter(capsuleDef);
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

    private static void setCharacterFilter(B3ShapeDef shapeDef) {
        B3Filter filter = shapeDef.GetFilter();
        filter.SetCategoryBits(CHARACTER_CATEGORY);
        shapeDef.SetFilter(filter);
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
        if(!character.IsValid()) {
            dispose(character);
            super.step(deltaSeconds);
            return;
        }

        if(jumpCooldown > 0.0f) {
            jumpCooldown -= deltaSeconds;
        }
        if(thirdPerson && playerInput != null && playerInput.jump()) {
            jump(character);
        }
        sprint = thirdPerson && playerInput != null && onGround && playerInput.sprint();

        float maxSpeed = sprint ? RUN_SPEED : WALK_SPEED;
        float wishX = 0.0f;
        float wishZ = 0.0f;
        if(thirdPerson && playerInput != null) {
            wishX = maxSpeed * (playerInput.moveForward() * playerInput.cameraForwardX()
                    + playerInput.moveRight() * playerInput.cameraRightX());
            wishZ = maxSpeed * (playerInput.moveForward() * playerInput.cameraForwardZ()
                    + playerInput.moveRight() * playerInput.cameraRightZ());
        }
        float wishSpeed = length(wishX, 0.0f, wishZ);
        if(wishSpeed > maxSpeed) {
            float scale = maxSpeed / wishSpeed;
            wishX *= scale;
            wishZ *= scale;
        }
        lastWishVelocityX = wishX;
        lastWishVelocityY = 0.0f;
        lastWishVelocityZ = wishZ;

        updateBody(character, wishX, wishZ);
        addVelocity(character, wishX, wishZ);
        didStep = tryStep(character, STEP_UP_HEIGHT);
        dispose(character);

        super.step(deltaSeconds);

        character = new B3Body(characterBodyId);
        if(character.IsValid()) {
            restoreStep(character);
            reground(character, STEP_DOWN_HEIGHT);
            categorizeGround(character);
            drawCharacterDebug(character);
        }
        dispose(character);
    }

    private void jump(B3Body character) {
        if(!onGround || jumpCooldown > 0.0f) {
            return;
        }
        B3Vec3 velocity = character.GetLinearVelocity();
        B3Vec3 jumpVelocity = new B3Vec3(velocity.GetX(), JUMP_SPEED, velocity.GetZ());
        character.SetLinearVelocity(jumpVelocity);
        dispose(jumpVelocity);
        onGround = false;
        jumpCooldown = JUMP_COOLDOWN_TIME;
    }

    private void updateBody(B3Body character, float wishX, float wishZ) {
        float wishLength = length(wishX, 0.0f, wishZ);
        B3Vec3 velocity = character.GetLinearVelocity();
        float velocityLength = length(velocity.GetX(), velocity.GetY(), velocity.GetZ());

        float feetFriction = 0.0f;
        if(onGround) {
            boolean wantsBrakes = wishLength < 5.0f * SOURCE_UNIT || wishLength < velocityLength * 0.9f;
            if(wantsBrakes) {
                feetFriction = 1.0f + 100.0f * BRAKE_POWER * SURFACE_FRICTION;
            }
        }
        B3Shape feet = new B3Shape(feetShapeId);
        if(feet.IsValid()) {
            feet.SetFriction(feetFriction);
        }
        dispose(feet);

        updateMassCenter(character, wishLength);
        boolean wantsGravity = !onGround
                || velocityLength > SOURCE_UNIT
                || length(groundVelocityX, groundVelocityY, groundVelocityZ) > SOURCE_UNIT;
        character.SetGravityScale(wantsGravity ? CHARACTER_GRAVITY / 10.0f : 0.0f);
        boolean wantsDamping = onGround && wishLength < SOURCE_UNIT
                && length(groundVelocityX, groundVelocityY, groundVelocityZ) < SOURCE_UNIT;
        character.SetLinearDamping(wantsDamping ? 10.0f * BRAKE_POWER : AIR_FRICTION);
    }

    private void updateMassCenter(B3Body character, float wishSpeed) {
        float halfHeight = TOTAL_HEIGHT * 0.5f;
        float centerY = onGround ? clamp(wishSpeed, 0.0f, halfHeight) - halfHeight : 0.0f;
        B3Vec3 center = new B3Vec3(0.0f, centerY, 0.0f);
        B3Vec3 inertiaX = character.GetLocalRotationalInertiaColumnX();
        B3Vec3 inertiaY = character.GetLocalRotationalInertiaColumnY();
        B3Vec3 inertiaZ = character.GetLocalRotationalInertiaColumnZ();
        character.SetMassData(character.GetMass(), center, inertiaX, inertiaY, inertiaZ);
        dispose(center);
    }

    private void addVelocity(B3Body character, float wishX, float wishZ) {
        float wishLength = length(wishX, 0.0f, wishZ);
        if(wishLength < 0.001f) {
            return;
        }

        B3Vec3 bodyVelocity = character.GetLinearVelocity();
        float savedY = bodyVelocity.GetY();
        float velocityX = bodyVelocity.GetX() - groundVelocityX;
        float velocityY = bodyVelocity.GetY() - groundVelocityY;
        float velocityZ = bodyVelocity.GetZ() - groundVelocityZ;
        float speed = length(velocityX, velocityY, velocityZ);
        float maximumSpeed = Math.max(wishLength, speed);

        float amount = onGround ? 0.25f + SURFACE_FRICTION * 10.0f : 0.05f;
        float addX = amount * wishX;
        float addZ = amount * wishZ;
        float maximumAddition = onGround ? wishLength * amount : wishLength;
        float addLength = length(addX, 0.0f, addZ);
        if(addLength > maximumAddition && addLength > 0.0f) {
            float scale = maximumAddition / addLength;
            addX *= scale;
            addZ *= scale;
        }
        velocityX += addX;
        velocityZ += addZ;

        float newSpeed = length(velocityX, velocityY, velocityZ);
        if(newSpeed > maximumSpeed && newSpeed > 0.0f) {
            float scale = maximumSpeed / newSpeed;
            velocityX *= scale;
            velocityY *= scale;
            velocityZ *= scale;
        }
        velocityX += groundVelocityX;
        velocityY += groundVelocityY;
        velocityZ += groundVelocityZ;
        if(onGround) {
            velocityY = savedY;
        }

        B3Vec3 velocity = new B3Vec3(velocityX, velocityY, velocityZ);
        character.SetLinearVelocity(velocity);
        dispose(velocity);
    }

    private void categorizeGround(B3Body character) {
        B3Vec3 position = character.GetPosition();
        float feetX = position.GetX();
        float feetY = position.GetY() - TOTAL_HEIGHT * 0.5f;
        float feetZ = position.GetZ();
        float fromY = feetY + 4.0f * SOURCE_UNIT;
        float toY = feetY - 2.0f * SOURCE_UNIT;

        float radiusScale = 1.0f;
        TraceResult trace = traceBody(feetX, fromY, feetZ, feetX, toY, feetZ, radiusScale, 0.5f);
        while(trace.startedSolid || (trace.hit && !isStandableSurface(trace.normalY))) {
            radiusScale -= 0.1f;
            if(radiusScale < 0.7f) {
                updateGround(false, 0.0f, 1.0f, 0.0f);
                return;
            }
            trace = traceBody(feetX, fromY, feetZ, feetX, toY, feetZ, radiusScale, 0.5f);
        }

        if(!trace.startedSolid && trace.hit && isStandableSurface(trace.normalY) && jumpCooldown <= 0.0f) {
            updateGround(true, trace.normalX, trace.normalY, trace.normalZ);
        }
        else {
            updateGround(false, 0.0f, 1.0f, 0.0f);
        }
    }

    private void updateGround(boolean grounded, float normalX, float normalY, float normalZ) {
        onGround = grounded;
        groundNormalX = normalX;
        groundNormalY = normalY;
        groundNormalZ = normalZ;
        if(!grounded) {
            groundVelocityX = 0.0f;
            groundVelocityY = 0.0f;
            groundVelocityZ = 0.0f;
        }
    }

    private void reground(B3Body character, float stepSize) {
        if(!onGround) {
            return;
        }
        B3Vec3 position = character.GetPosition();
        float x = position.GetX();
        float y = position.GetY();
        float z = position.GetZ();
        float radiusScale = 1.0f;
        TraceResult trace = traceBody(x, y + 0.05f, z, x, y - stepSize, z, radiusScale, 0.5f);
        while(trace.startedSolid) {
            radiusScale -= 0.1f;
            if(radiusScale < 0.7f) {
                return;
            }
            trace = traceBody(x, y + 0.05f, z, x, y - stepSize, z, radiusScale, 0.5f);
        }
        if(trace.hit) {
            float targetY = trace.endY + 0.01f;
            B3Vec3 target = new B3Vec3(trace.endX, targetY, trace.endZ);
            character.SetTransform(target, character.GetRotation());
            dispose(target);
            if(targetY - y > 0.01f) {
                B3Vec3 oldVelocity = character.GetLinearVelocity();
                B3Vec3 velocity = new B3Vec3(oldVelocity.GetX(), 0.0f, oldVelocity.GetZ());
                character.SetLinearVelocity(velocity);
                dispose(velocity);
            }
        }
    }

    private boolean tryStep(B3Body character, float maxStepHeight) {
        if(!onGround) {
            return false;
        }
        B3Vec3 position = character.GetPosition();
        B3Vec3 velocity = character.GetLinearVelocity();
        float x = position.GetX();
        float y = position.GetY();
        float z = position.GetZ();
        float horizontalSpeed = length(velocity.GetX(), 0.0f, velocity.GetZ());
        if(horizontalSpeed < 0.01f) {
            return false;
        }
        float directionX = velocity.GetX() / horizontalSpeed;
        float directionZ = velocity.GetZ() / horizontalSpeed;
        float forwardDistance = horizontalSpeed / 60.0f + BODY_RADIUS;
        float forwardFromX = x - SKIN * directionX;
        float forwardFromZ = z - SKIN * directionZ;
        float forwardToX = x + forwardDistance * directionX;
        float forwardToZ = z + forwardDistance * directionZ;

        float radiusScale = 1.0f;
        TraceResult forward = traceBody(forwardFromX, y, forwardFromZ, forwardToX, y, forwardToZ,
                radiusScale, 1.0f);
        while(forward.startedSolid) {
            radiusScale -= 0.1f;
            if(radiusScale < 0.6f) {
                return false;
            }
            forward = traceBody(forwardFromX, y, forwardFromZ, forwardToX, y, forwardToZ,
                    radiusScale, 1.0f);
        }
        if(!forward.hit) {
            return false;
        }

        TraceResult up = traceBody(forward.endX, forward.endY, forward.endZ,
                forward.endX, forward.endY + maxStepHeight, forward.endZ, radiusScale, 1.0f);
        if(up.startedSolid) {
            return false;
        }
        float topX = up.hit ? up.endX : forward.endX;
        float topY = up.hit ? up.endY : forward.endY + maxStepHeight;
        float topZ = up.hit ? up.endZ : forward.endZ;
        if(topY - forward.endY < 0.005f) {
            return false;
        }

        float acrossDistance = forwardDistance * (1.0f - forward.fraction) + BODY_RADIUS * 0.5f;
        TraceResult across = traceBody(topX, topY, topZ,
                topX + acrossDistance * directionX, topY, topZ + acrossDistance * directionZ,
                radiusScale, 1.0f);
        if(across.startedSolid) {
            return false;
        }
        float acrossX = across.hit ? across.endX : topX + acrossDistance * directionX;
        float acrossY = across.hit ? across.endY : topY;
        float acrossZ = across.hit ? across.endZ : topZ + acrossDistance * directionZ;

        TraceResult down = traceBody(acrossX, acrossY, acrossZ,
                acrossX, acrossY - maxStepHeight, acrossZ, radiusScale, 1.0f);
        if(!down.hit || !isStandableSurface(down.normalY) || down.endY - y < 0.01f) {
            return false;
        }

        stepPositionX = down.endX;
        stepPositionY = down.endY + 0.01f;
        stepPositionZ = down.endZ;
        B3Vec3 stepPosition = new B3Vec3(stepPositionX, stepPositionY, stepPositionZ);
        character.SetTransform(stepPosition, character.GetRotation());
        dispose(stepPosition);
        B3Vec3 oldVelocity = character.GetLinearVelocity();
        B3Vec3 steppedVelocity = new B3Vec3(0.9f * oldVelocity.GetX(), 0.0f,
                0.9f * oldVelocity.GetZ());
        character.SetLinearVelocity(steppedVelocity);
        dispose(steppedVelocity);
        return true;
    }

    private void restoreStep(B3Body character) {
        if(!didStep) {
            return;
        }
        B3Vec3 position = new B3Vec3(stepPositionX, stepPositionY, stepPositionZ);
        character.SetTransform(position, character.GetRotation());
        dispose(position);
        didStep = false;
    }

    private TraceResult traceBody(float fromX, float fromY, float fromZ,
            float toX, float toY, float toZ, float radiusScale, float heightScale) {
        TraceResult trace = new TraceResult();
        trace.endX = toX;
        trace.endY = toY;
        trace.endZ = toZ;
        trace.normalY = 1.0f;
        trace.fraction = 1.0f;

        float translationX = toX - fromX;
        float translationY = toY - fromY;
        float translationZ = toZ - fromZ;
        if(length(translationX, translationY, translationZ) < 0.000001f) {
            return trace;
        }

        float halfWidth = BODY_RADIUS * 0.5f * radiusScale;
        float halfHeight = TOTAL_HEIGHT * heightScale * 0.5f;
        float halfDepth = BODY_RADIUS * 0.5f * radiusScale;
        B3Vec3Array points = new B3Vec3Array(8);
        for(int i = 0; i < 8; ++i) {
            float px = (i & 1) != 0 ? halfWidth : -halfWidth;
            float py = halfHeight + ((i & 2) != 0 ? halfHeight : -halfHeight);
            float pz = (i & 4) != 0 ? halfDepth : -halfDepth;
            B3Vec3 point = new B3Vec3(px, py, pz);
            points.SetValue(i, point);
            dispose(point);
        }
        B3ShapeProxy proxy = new B3ShapeProxy(points, 8, 0.0f);
        B3Vec3 origin = new B3Vec3(fromX, fromY, fromZ);
        B3Vec3 translation = new B3Vec3(translationX, translationY, translationZ);
        B3QueryFilter filter = new B3QueryFilter();
        filter.SetCategoryBits(1L);
        filter.SetMaskBits(~CHARACTER_CATEGORY);
        B3RayResult result = world().CastShapeClosest(origin, proxy, translation, filter, true);
        if(result.GetHit()) {
            trace.hit = true;
            trace.fraction = result.GetFraction();
            trace.startedSolid = trace.fraction <= 0.000001f;
            B3Vec3 normal = result.GetNormal();
            B3Vec3 point = result.GetPoint();
            trace.normalX = normal.GetX();
            trace.normalY = normal.GetY();
            trace.normalZ = normal.GetZ();
            trace.hitX = point.GetX();
            trace.hitY = point.GetY();
            trace.hitZ = point.GetZ();
            trace.endX = fromX + trace.fraction * translationX;
            trace.endY = fromY + trace.fraction * translationY;
            trace.endZ = fromZ + trace.fraction * translationZ;
        }
        dispose(result, filter, translation, origin, proxy, points);
        return trace;
    }

    private void drawCharacterDebug(B3Body character) {
        B3Vec3 position = character.GetPosition();
        B3Vec3 velocity = character.GetLinearVelocity();
        B3Vec3 velocityEnd = new B3Vec3(position.GetX() + velocity.GetX(),
                position.GetY() + velocity.GetY(), position.GetZ() + velocity.GetZ());
        B3Vec3 wishEnd = new B3Vec3(position.GetX() + lastWishVelocityX,
                position.GetY() + lastWishVelocityY, position.GetZ() + lastWishVelocityZ);
        world().AddDebugSegment(position, velocityEnd, 0x800080);
        world().AddDebugSegment(position, wishEnd, 0xFFA500);
        if(onGround) {
            B3Vec3 feet = new B3Vec3(position.GetX(), position.GetY() - TOTAL_HEIGHT * 0.5f,
                    position.GetZ());
            B3Vec3 normalEnd = new B3Vec3(feet.GetX() + 0.3f * groundNormalX,
                    feet.GetY() + 0.3f * groundNormalY, feet.GetZ() + 0.3f * groundNormalZ);
            world().AddDebugSegment(feet, normalEnd, 0x008000);
            dispose(normalEnd, feet);
        }
        dispose(wishEnd, velocityEnd);
    }

    private static boolean isStandableSurface(float normalY) {
        return normalY >= MAX_SLOPE_COS;
    }

    private static float length(float x, float y, float z) {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    private static float clamp(float value, float minimum, float maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }

    @Override
    public boolean supportsPlayerControl() {
        return true;
    }

    @Override
    public boolean startsInThirdPerson() {
        return true;
    }

    @Override
    public void setPlayerInput(Box3DPlayerInput input, boolean thirdPerson) {
        playerInput = input;
        this.thirdPerson = thirdPerson;
    }

    @Override
    public boolean getCameraTarget(Box3DPlayerTarget target) {
        B3Body character = new B3Body(characterBodyId);
        if(!character.IsValid()) {
            dispose(character);
            return false;
        }
        B3Vec3 position = character.GetPosition();
        target.set(position.GetX(), position.GetY(), position.GetZ());
        dispose(character);
        return true;
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(heightField, voxel02, voxel01, building, stairs, levelMesh);
    }

    private static final class TraceResult {
        boolean hit;
        boolean startedSolid;
        float fraction;
        float endX;
        float endY;
        float endZ;
        float normalX;
        float normalY;
        float normalZ;
        float hitX;
        float hitY;
        float hitZ;
    }
}
