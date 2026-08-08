package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3HeightField;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default port of Collision / Long Ray Cast from the pinned Box3D commit. */
final class LongRayCastSample extends AbstractBox3DSample {
    private static final int SHAPE_COUNT = 5;
    private static final int TRAIL_COUNT = 180;
    private static final float PI = (float)Math.PI;

    private final B3Hull hull;
    private final B3Mesh mesh;
    private final B3HeightField heightField;
    private final float[][] targets = new float[SHAPE_COUNT][3];
    private final float[][][] trail = new float[SHAPE_COUNT][TRAIL_COUNT][3];
    private final int[] trailNext = new int[SHAPE_COUNT];
    private final int[] trailCount = new int[SHAPE_COUNT];
    private final float[] failRate = new float[SHAPE_COUNT];
    private float phase;

    LongRayCastSample() {
        B3Vec3 zeroGravity = new B3Vec3();
        world().SetGravity(zeroGravity);
        hull = B3Hull.CreateRock(1.0f);
        mesh = B3Mesh.CreateWave(8, 8, 0.5f, 0.25f, 0.2f, 0.2f);
        B3Vec3 heightScale = new B3Vec3(0.5f, 0.5f, 0.5f);
        heightField = B3HeightField.CreateWave(9, 9, heightScale, 0.08f, 0.16f, false);

        for(int i = 0; i < SHAPE_COUNT; ++i) {
            targets[i][0] = (i - 2) * 5.0f;
            targets[i][1] = 2.5f;
        }

        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Body sphereBody = createBody(B3.StaticBody(), targets[0][0], 0.0f, 0.0f, null);
        B3Vec3 center = new B3Vec3();
        B3Sphere sphere = new B3Sphere(center, 1.0f);
        dispose(sphereBody.CreateSphereShape(shapeDef, sphere), sphereBody, sphere, center);

        B3Body capsuleBody = createBody(B3.StaticBody(), targets[1][0], 0.0f, 0.0f, null);
        B3Vec3 capsule1 = new B3Vec3(-1.0f, 0.0f, 0.0f);
        B3Vec3 capsule2 = new B3Vec3(1.0f, 0.0f, 0.0f);
        com.github.xpenatan.box3d.B3Capsule capsule =
                new com.github.xpenatan.box3d.B3Capsule(capsule1, capsule2, 0.7f);
        dispose(capsuleBody.CreateCapsuleShape(shapeDef, capsule), capsuleBody, capsule, capsule2, capsule1);

        B3Body hullBody = createBody(B3.StaticBody(), targets[2][0], 0.0f, 0.0f, null);
        dispose(hullBody.CreateHullShape(shapeDef, hull), hullBody);

        B3Body meshBody = createBody(B3.StaticBody(), targets[3][0], 0.0f, 0.0f, null);
        B3Vec3 one = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(meshBody.CreateMeshShape(shapeDef, mesh, one), meshBody, one);

        float extent = 0.5f * (9 - 1);
        B3Body heightBody = createBody(B3.StaticBody(), targets[4][0] - 0.5f * extent, 0.0f,
                -0.5f * extent, null);
        dispose(heightBody.CreateHeightFieldShape(shapeDef, heightField), heightBody);
        dispose(shapeDef, heightScale, zeroGravity);
    }

    @Override
    public void step(float deltaSeconds) {
        super.step(deltaSeconds);
        drawRays();
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(heightField, mesh, hull);
    }

    private void drawRays() {
        world().ClearDebugOverlay();
        phase += 2.0f * PI / TRAIL_COUNT;
        if(phase > 2.0f * PI) {
            phase -= 2.0f * PI;
        }

        float angle = 5.0f * PI / 180.0f;
        float sinAngle = (float)Math.sin(angle);
        float[] coneDir = {
                (float)Math.sin(phase) * sinAngle,
                (float)Math.cos(angle),
                (float)Math.cos(phase) * sinAngle
        };
        B3QueryFilter filter = new B3QueryFilter();

        for(int i = 0; i < SHAPE_COUNT; ++i) {
            CastHit truth = castAlong(targets[i], coneDir, 50.0f, 5.0f, filter);
            CastHit cast = castAlong(targets[i], coneDir, 1000.0f, 5.0f, filter);
            float fail = 0.0f;

            if(cast.hit) {
                float error = truth.hit ? distance(cast.point, truth.point) : 0.0f;
                int color = error < 0.05f ? 0x008000 : 0xFFA500;
                float[] slot = trail[i][trailNext[i]];
                slot[0] = cast.point[0];
                slot[1] = cast.point[1];
                slot[2] = cast.point[2];
                trailNext[i] = (trailNext[i] + 1) % TRAIL_COUNT;
                if(trailCount[i] < TRAIL_COUNT) {
                    trailCount[i] += 1;
                }

                addSegment(cast.point[0] + 3.0f * coneDir[0], cast.point[1] + 3.0f * coneDir[1],
                        cast.point[2] + 3.0f * coneDir[2], cast.point[0], cast.point[1], cast.point[2], 0x00FFFF);
                addSegment(cast.point[0], cast.point[1], cast.point[2],
                        cast.point[0] + 1.5f * cast.normal[0], cast.point[1] + 1.5f * cast.normal[1],
                        cast.point[2] + 1.5f * cast.normal[2], 0xFFFF00);
                addPoint(cast.point, 8.0f, color);
            }
            else if(truth.hit) {
                fail = 1.0f;
                addPoint(truth.point, 14.0f, 0xFF0000);
                addSegment(truth.point[0] + 2.0f * coneDir[0], truth.point[1] + 2.0f * coneDir[1],
                        truth.point[2] + 2.0f * coneDir[2], truth.point[0] - 2.0f * coneDir[0],
                        truth.point[1] - 2.0f * coneDir[1], truth.point[2] - 2.0f * coneDir[2], 0xFF0000);
            }
            else {
                float[] aim = targets[i];
                addSegment(aim[0] + 2.0f * coneDir[0], aim[1] + 2.0f * coneDir[1],
                        aim[2] + 2.0f * coneDir[2], aim[0] - 4.0f * coneDir[0],
                        aim[1] - 4.0f * coneDir[1], aim[2] - 4.0f * coneDir[2], 0x333333);
            }

            failRate[i] = 0.95f * failRate[i] + 0.05f * fail;
            int start = (trailNext[i] - trailCount[i] + TRAIL_COUNT) % TRAIL_COUNT;
            for(int j = 0; j < trailCount[i]; ++j) {
                int index = (start + j) % TRAIL_COUNT;
                float alpha = (j + 1.0f) / trailCount[i];
                int green = Math.max(1, Math.round(128.0f * alpha));
                addPoint(trail[i][index], 4.0f, green << 8);
            }
        }

        dispose(filter);
    }

    private CastHit castAlong(float[] aim, float[] direction, float distance, float reach, B3QueryFilter filter) {
        B3Vec3 origin = new B3Vec3(aim[0] + distance * direction[0], aim[1] + distance * direction[1],
                aim[2] + distance * direction[2]);
        float length = -(distance + reach);
        B3Vec3 translation = new B3Vec3(length * direction[0], length * direction[1], length * direction[2]);
        B3RayResult result = world().CastRayClosest(origin, translation, filter);
        CastHit hit = new CastHit();
        hit.hit = result.GetHit();
        if(hit.hit) {
            B3Vec3 point = result.GetPoint();
            B3Vec3 normal = result.GetNormal();
            hit.point[0] = point.GetX();
            hit.point[1] = point.GetY();
            hit.point[2] = point.GetZ();
            hit.normal[0] = normal.GetX();
            hit.normal[1] = normal.GetY();
            hit.normal[2] = normal.GetZ();
            dispose(normal, point);
        }
        dispose(result, translation, origin);
        return hit;
    }

    private void addPoint(float[] point, float size, int color) {
        B3Vec3 value = new B3Vec3(point[0], point[1], point[2]);
        world().AddDebugPoint(value, size, color);
        dispose(value);
    }

    private void addSegment(float x1, float y1, float z1, float x2, float y2, float z2, int color) {
        B3Vec3 p1 = new B3Vec3(x1, y1, z1);
        B3Vec3 p2 = new B3Vec3(x2, y2, z2);
        world().AddDebugSegment(p1, p2, color);
        dispose(p2, p1);
    }

    private static float distance(float[] a, float[] b) {
        float x = a[0] - b[0];
        float y = a[1] - b[1];
        float z = a[2] - b[2];
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    private static final class CastHit {
        final float[] point = new float[3];
        final float[] normal = new float[3];
        boolean hit;
    }
}
