package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3MeshDef;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3RevoluteJointDef;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3SurfaceMaterialArray;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact scene construction from Box3D's Joints/Gear Lift sample. */
final class GearLiftSample extends AbstractBox3DSample {
    private static final float PI = (float)Math.PI;
    private static final float GEAR_RADIUS = 1.0f;
    private static final float GEAR_HALF_DEPTH = 0.125f;
    private static final float GEAR_Z = 1.5f;
    private static final float AXLE_RADIUS = 0.2f;
    private static final float TOOTH_HALF_WIDTH = 0.11f;
    private static final float TOOTH_HALF_HEIGHT = 0.09f;
    private static final float TOOTH_RADIUS = 0.03f;
    private static final float LINK_HALF_LENGTH = 0.07f;
    private static final float LINK_RADIUS = 0.05f;
    private static final int LINK_COUNT = 40;
    private static final float DOOR_HALF_HEIGHT = 1.5f;
    private static final float DOOR_HALF_DEPTH = 1.95f;
    private static final int GEAR_SIDES = 24;
    private static final int AXLE_SIDES = 12;
    private static final float ROCK_RADIUS = 0.3f;

    private static final int DARK_SEA_GREEN = 0x8FBC8F;
    private static final int SADDLE_BROWN = 0x8B4513;
    private static final int SLATE_GRAY = 0x708090;
    private static final int GRAY = 0x808080;
    private static final int LIGHT_STEEL_BLUE = 0xB0C4DE;
    private static final int METALLIC_DARK_CYAN = 0x05008B8B;
    private static final int[] ROCK_COLORS = { 0x808080, 0xDCDCDC, 0xD3D3D3, 0x778899, 0xA9A9A9 };

    private static final float[][] STAIRWELL = {
            { -11.3000f, -0.2167f }, { 9.3375f, -0.2167f }, { 9.3375f, 7.1917f }, { 8.8083f, 7.1917f },
            { 8.8083f, 0.3125f }, { 0.3417f, 0.3125f }, { 0.3417f, 0.8417f }, { -0.1875f, 0.8417f },
            { -0.1875f, 1.3708f }, { -0.7167f, 1.3708f }, { -0.7167f, 1.9000f }, { -1.2458f, 1.9000f },
            { -1.2458f, 2.4292f }, { -1.7750f, 2.4292f }, { -1.7750f, 2.9583f }, { -2.3042f, 2.9583f },
            { -2.3042f, 3.4875f }, { -2.8333f, 3.4875f }, { -2.8333f, 4.0167f }, { -3.3625f, 4.0167f },
            { -3.3625f, 4.5458f }, { -3.8917f, 4.5458f }, { -3.8917f, 5.0750f }, { -4.4208f, 5.0750f },
            { -4.4208f, 5.6042f }, { -4.9500f, 5.6042f }, { -4.9500f, 6.1333f }, { -5.4792f, 6.1333f },
            { -5.4792f, 6.6625f }, { -6.0083f, 6.6625f }, { -6.0083f, 7.1917f }, { -11.3000f, 7.1917f }
    };

    // Exact mapbox::earcut output for STAIRWELL at the pinned Box3D commit.
    private static final int[] CAP_TRIANGLES = {
            30, 31, 0, 1, 2, 3, 5, 6, 7, 7, 8, 9, 9, 10, 11, 11, 12, 13, 13, 14, 15,
            15, 16, 17, 17, 18, 19, 19, 20, 21, 21, 22, 23, 23, 24, 25, 25, 26, 27, 27, 28, 29,
            29, 30, 0, 1, 3, 4, 5, 7, 9, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 0, 0, 1,
            4, 9, 13, 15, 15, 19, 21, 21, 25, 27, 0, 4, 5, 9, 15, 21, 21, 27, 0, 0, 5, 9, 9, 21, 0
    };

    private B3Mesh mesh;

    GearLiftSample() {
        dispose(addGroundBox(20.0f));

        B3Body ground = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        createMesh(ground);

        B3Hull diskNear = makeZCylinder(GEAR_RADIUS, -GEAR_Z - GEAR_HALF_DEPTH,
                -GEAR_Z + GEAR_HALF_DEPTH, GEAR_SIDES);
        B3Hull diskFar = makeZCylinder(GEAR_RADIUS, GEAR_Z - GEAR_HALF_DEPTH,
                GEAR_Z + GEAR_HALF_DEPTH, GEAR_SIDES);
        B3Hull axle = makeZCylinder(AXLE_RADIUS, -GEAR_Z, GEAR_Z, AXLE_SIDES);

        B3Body driver = buildGearBody(-4.25f, 9.75f, GEAR_RADIUS + TOOTH_HALF_HEIGHT,
                diskNear, diskFar, axle);
        B3Body follower = buildGearBody(-2.25f, 10.75f, GEAR_RADIUS + TOOTH_HALF_WIDTH,
                diskNear, diskFar, axle);
        dispose(axle, diskFar, diskNear);

        B3RevoluteJointDef driverDef = new B3RevoluteJointDef();
        driverDef.SetBodyIdA(ground.GetId());
        driverDef.SetBodyIdB(driver.GetId());
        setLocalPointA(driverDef, ground, -4.25f, 9.75f, 0.0f);
        JointSampleUtil.setLocalPositionB(driverDef, 0.0f, 0.0f, 0.0f);
        driverDef.SetEnableMotor(true);
        driverDef.SetMaxMotorTorque(30000.0f);
        driverDef.SetMotorSpeed(-0.3f);
        dispose(world().CreateRevoluteJoint(driverDef), driverDef);

        B3RevoluteJointDef followerDef = new B3RevoluteJointDef();
        followerDef.SetBodyIdA(ground.GetId());
        followerDef.SetBodyIdB(follower.GetId());
        B3Vec3 followerPoint = localPoint(ground, -2.25f, 10.75f, 0.0f);
        B3Quat followerFrameRotation = rotationZ(0.25f * PI);
        JointSampleUtil.setLocalFrameA(followerDef, followerPoint.GetX(), followerPoint.GetY(), followerPoint.GetZ(),
                followerFrameRotation);
        JointSampleUtil.setLocalPositionB(followerDef, 0.0f, 0.0f, 0.0f);
        followerDef.SetEnableMotor(true);
        followerDef.SetMaxMotorTorque(0.5f);
        followerDef.SetLowerAngle(-0.3f * PI);
        followerDef.SetUpperAngle(0.8f * PI);
        followerDef.SetEnableLimit(true);
        dispose(world().CreateRevoluteJoint(followerDef), followerFrameRotation, followerPoint, followerDef);

        float attachX = -2.25f + GEAR_RADIUS + 2.0f * TOOTH_HALF_WIDTH + TOOTH_RADIUS;
        float attachY = 10.75f;
        float doorY = attachY - (2.0f * LINK_COUNT * LINK_HALF_LENGTH + DOOR_HALF_HEIGHT);
        B3Body nearLink = createChain(follower, attachX, attachY, -GEAR_Z);
        B3Body farLink = createChain(follower, attachX, attachY, GEAR_Z);
        createDoor(ground, attachX, doorY, nearLink, farLink);
        createDebris();

        dispose(farLink, nearLink, follower, driver, ground);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(mesh);
    }

    private void createMesh(B3Body ground) {
        B3MeshDef meshDef = new B3MeshDef(64, 124);
        float zMin = -2.0f;
        float zMax = 2.0f;
        for(float[] point : STAIRWELL) {
            addVertex(meshDef, point[0], point[1], zMin);
            addVertex(meshDef, point[0], point[1], zMax);
        }

        for(int i = 0; i < STAIRWELL.length; i++) {
            int j = (i + 1) % STAIRWELL.length;
            int aLo = 2 * i;
            int aHi = aLo + 1;
            int bLo = 2 * j;
            int bHi = bLo + 1;
            meshDef.AddTriangle(aLo, bLo, bHi, 0);
            meshDef.AddTriangle(aLo, bHi, aHi, 0);
        }

        for(int i = 0; i < CAP_TRIANGLES.length; i += 3) {
            pushCap(meshDef, CAP_TRIANGLES[i], CAP_TRIANGLES[i + 1], CAP_TRIANGLES[i + 2], 1, true);
            pushCap(meshDef, CAP_TRIANGLES[i], CAP_TRIANGLES[i + 1], CAP_TRIANGLES[i + 2], 0, false);
        }
        meshDef.SetIdentifyEdges(true);
        mesh = B3Mesh.CreateFromDef(meshDef);

        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetCustomColor(DARK_SEA_GREEN);
        shapeDef.SetBaseMaterial(material);
        B3SurfaceMaterialArray materials = new B3SurfaceMaterialArray(1);
        materials.SetValue(0, material);
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(ground.CreateMeshShapeWithMaterials(shapeDef, mesh, scale, materials));

        float lowerX = STAIRWELL[0][0];
        float lowerY = STAIRWELL[0][1];
        float upperX = lowerX;
        float upperY = lowerY;
        for(int i = 1; i < STAIRWELL.length; i++) {
            lowerX = Math.min(lowerX, STAIRWELL[i][0]);
            lowerY = Math.min(lowerY, STAIRWELL[i][1]);
            upperX = Math.max(upperX, STAIRWELL[i][0]);
            upperY = Math.max(upperY, STAIRWELL[i][1]);
        }
        float wallHalfThick = 0.05f;
        B3Vec3 wallCenter = new B3Vec3(0.5f * (lowerX + upperX), 0.5f * (lowerY + upperY),
                -zMax - wallHalfThick);
        B3Quat identity = new B3Quat();
        B3Transform wallTransform = new B3Transform(wallCenter, identity);
        B3Hull wall = B3Hull.CreateTransformedBox(0.5f * (upperX - lowerX), 0.5f * (upperY - lowerY),
                wallHalfThick, wallTransform);
        dispose(ground.CreateHullShape(shapeDef, wall));
        dispose(wall, wallTransform, identity, wallCenter, scale, materials, material, shapeDef, meshDef);
    }

    private static void addVertex(B3MeshDef meshDef, float x, float y, float z) {
        B3Vec3 vertex = new B3Vec3(x, y, z);
        meshDef.AddVertex(vertex);
        dispose(vertex);
    }

    private static void pushCap(B3MeshDef meshDef, int r0, int r1, int r2, int vertexOffset,
            boolean wantPositiveZ) {
        float[] p0 = STAIRWELL[r0];
        float[] p1 = STAIRWELL[r1];
        float[] p2 = STAIRWELL[r2];
        float cross = (p1[0] - p0[0]) * (p2[1] - p0[1]) - (p1[1] - p0[1]) * (p2[0] - p0[0]);
        int v0 = 2 * r0 + vertexOffset;
        int v1 = 2 * r1 + vertexOffset;
        int v2 = 2 * r2 + vertexOffset;
        if((cross > 0.0f) == wantPositiveZ) {
            meshDef.AddTriangle(v0, v1, v2, 0);
        }
        else {
            meshDef.AddTriangle(v0, v2, v1, 0);
        }
    }

    private B3Hull makeZCylinder(float radius, float zMin, float zMax, int sides) {
        float[][] points = new float[2 * sides][3];
        for(int i = 0; i < sides; i++) {
            float angle = 2.0f * PI * i / sides;
            float x = radius * (float)Math.cos(angle);
            float y = radius * (float)Math.sin(angle);
            points[2 * i] = new float[] { x, y, zMin };
            points[2 * i + 1] = new float[] { x, y, zMax };
        }
        return createHull(points);
    }

    private B3Body buildGearBody(float x, float y, float toothCenterRadius, B3Hull diskNear, B3Hull diskFar,
            B3Hull axle) {
        B3Body body = createBody(B3.DynamicBody(), x, y, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetFriction(0.1f);
        material.SetCustomColor(SADDLE_BROWN);
        shapeDef.SetBaseMaterial(material);
        dispose(body.CreateHullShape(shapeDef, diskNear), body.CreateHullShape(shapeDef, diskFar));

        material.SetCustomColor(SLATE_GRAY);
        shapeDef.SetBaseMaterial(material);
        dispose(body.CreateHullShape(shapeDef, axle));
        dispose(material, shapeDef);

        addTeeth(body, toothCenterRadius, -GEAR_Z);
        addTeeth(body, toothCenterRadius, GEAR_Z);
        return body;
    }

    private void addTeeth(B3Body body, float centerRadius, float zCenter) {
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetFriction(0.1f);
        material.SetCustomColor(GRAY);
        shapeDef.SetBaseMaterial(material);

        int count = 16;
        float deltaAngle = 2.0f * PI / count;
        float baseHalf = TOOTH_HALF_HEIGHT;
        float tipHalf = TOOTH_HALF_HEIGHT - TOOTH_RADIUS;
        for(int i = 0; i < count; i++) {
            float angle = i * deltaAngle;
            float cosine = (float)Math.cos(angle);
            float sine = (float)Math.sin(angle);
            float centerX = centerRadius * cosine;
            float centerY = centerRadius * sine;
            float[][] local = {
                    { -TOOTH_HALF_WIDTH, -baseHalf, -GEAR_HALF_DEPTH },
                    { -TOOTH_HALF_WIDTH, baseHalf, -GEAR_HALF_DEPTH },
                    { -TOOTH_HALF_WIDTH, baseHalf, GEAR_HALF_DEPTH },
                    { -TOOTH_HALF_WIDTH, -baseHalf, GEAR_HALF_DEPTH },
                    { TOOTH_HALF_WIDTH, -tipHalf, -GEAR_HALF_DEPTH },
                    { TOOTH_HALF_WIDTH, tipHalf, -GEAR_HALF_DEPTH },
                    { TOOTH_HALF_WIDTH, tipHalf, GEAR_HALF_DEPTH },
                    { TOOTH_HALF_WIDTH, -tipHalf, GEAR_HALF_DEPTH }
            };
            float[][] points = new float[8][3];
            for(int j = 0; j < points.length; j++) {
                points[j][0] = centerX + cosine * local[j][0] - sine * local[j][1];
                points[j][1] = centerY + sine * local[j][0] + cosine * local[j][1];
                points[j][2] = zCenter + local[j][2];
            }
            B3Hull tooth = createHull(points);
            dispose(body.CreateHullShape(shapeDef, tooth), tooth);
        }
        dispose(material, shapeDef);
    }

    private B3Body createChain(B3Body topBody, float attachX, float attachY, float attachZ) {
        B3Vec3 center1 = new B3Vec3(0.0f, -LINK_HALF_LENGTH, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, LINK_HALF_LENGTH, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, LINK_RADIUS);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetCustomColor(LIGHT_STEEL_BLUE);
        shapeDef.SetBaseMaterial(material);

        B3RevoluteJointDef jointDef = new B3RevoluteJointDef();
        jointDef.SetMaxMotorTorque(0.05f);
        jointDef.SetEnableMotor(true);
        jointDef.SetDrawScale(0.2f);

        float y = attachY - LINK_HALF_LENGTH;
        B3Body previous = topBody;
        for(int i = 0; i < LINK_COUNT; i++) {
            B3Body body = createBody(B3.DynamicBody(), attachX, y, attachZ, null);
            dispose(body.CreateCapsuleShape(shapeDef, capsule));

            float pivotY = y + LINK_HALF_LENGTH;
            jointDef.SetBodyIdA(previous.GetId());
            jointDef.SetBodyIdB(body.GetId());
            B3Vec3 localA = localPoint(previous, attachX, pivotY, attachZ);
            B3Vec3 localB = localPoint(body, attachX, pivotY, attachZ);
            jointDef.SetLocalPositionA(localA);
            jointDef.SetLocalPositionB(localB);
            dispose(world().CreateRevoluteJoint(jointDef), localB, localA);

            if(previous != topBody) {
                dispose(previous);
            }
            previous = body;
            y -= 2.0f * LINK_HALF_LENGTH;
        }

        dispose(jointDef, material, shapeDef, capsule, center2, center1);
        return previous;
    }

    private void createDoor(B3Body ground, float x, float y, B3Body nearLink, B3Body farLink) {
        B3Body door = createBody(B3.DynamicBody(), x, y, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        shapeDef.SetDensity(0.5f);
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetFriction(0.1f);
        material.SetCustomColor(METALLIC_DARK_CYAN);
        shapeDef.SetBaseMaterial(material);
        B3Hull box = B3Hull.CreateBox(0.05f, DOOR_HALF_HEIGHT, DOOR_HALF_DEPTH);
        dispose(door.CreateHullShape(shapeDef, box));

        B3Body[] links = { nearLink, farLink };
        float[] depths = { -GEAR_Z, GEAR_Z };
        for(int i = 0; i < links.length; i++) {
            B3RevoluteJointDef jointDef = new B3RevoluteJointDef();
            jointDef.SetBodyIdA(links[i].GetId());
            jointDef.SetBodyIdB(door.GetId());
            B3Vec3 localA = localPoint(links[i], x, y + DOOR_HALF_HEIGHT, depths[i]);
            jointDef.SetLocalPositionA(localA);
            JointSampleUtil.setLocalPositionB(jointDef, 0.0f, DOOR_HALF_HEIGHT, depths[i]);
            jointDef.SetEnableMotor(true);
            jointDef.SetMaxMotorTorque(50.0f);
            dispose(world().CreateRevoluteJoint(jointDef), localA, jointDef);
        }

        B3PrismaticJointDef slideDef = new B3PrismaticJointDef();
        slideDef.SetBodyIdA(ground.GetId());
        slideDef.SetBodyIdB(door.GetId());
        B3Vec3 localGround = localPoint(ground, x, y, 0.0f);
        B3Vec3 localDoor = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Quat slideAxis = rotationZ(0.5f * PI);
        B3Transform frameA = new B3Transform(localGround, slideAxis);
        B3Transform frameB = new B3Transform(localDoor, slideAxis);
        slideDef.SetLocalFrameA(frameA);
        slideDef.SetLocalFrameB(frameB);
        slideDef.SetMaxMotorForce(200.0f);
        slideDef.SetEnableMotor(true);
        slideDef.SetCollideConnected(true);
        dispose(world().CreatePrismaticJoint(slideDef), frameB, frameA, slideAxis, localDoor, localGround, slideDef);
        dispose(box, material, shapeDef, door);
    }

    private void createDebris() {
        SampleRandom random = new SampleRandom();
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetRollingResistance(0.3f);
        B3Hull rock = B3Hull.CreateRock(ROCK_RADIUS);

        float x = -5.0f;
        for(int i = 0; i < 12; i++) {
            float y = 6.5f - 0.25f * i;
            for(int j = 0; j < 10; j++) {
                B3Vec3 position = new B3Vec3(x, y, random.nextFloat(-1.65f, 0.35f));
                B3Quat rotation = random.nextQuaternion();
                bodyDef.SetPosition(position);
                bodyDef.SetRotation(rotation);
                B3Body body = world().CreateBody(bodyDef);

                material.SetCustomColor(ROCK_COLORS[random.nextInt(0, 4)]);
                shapeDef.SetBaseMaterial(material);
                dispose(body.CreateHullShape(shapeDef, rock), body, rotation, position);
                y += 0.2f;
            }
            x += 0.3f;
        }
        dispose(rock, material, shapeDef, bodyDef);
    }

    private static B3Vec3 localPoint(B3Body body, float x, float y, float z) {
        B3Vec3 worldPoint = new B3Vec3(x, y, z);
        B3Vec3 localPoint = body.GetLocalPoint(worldPoint);
        dispose(worldPoint);
        return localPoint;
    }

    private static void setLocalPointA(B3RevoluteJointDef jointDef, B3Body body, float x, float y, float z) {
        B3Vec3 point = localPoint(body, x, y, z);
        jointDef.SetLocalPositionA(point);
        dispose(point);
    }
}
