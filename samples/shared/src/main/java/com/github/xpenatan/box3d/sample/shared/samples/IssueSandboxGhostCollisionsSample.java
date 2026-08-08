package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Mesh;
import com.github.xpenatan.box3d.B3MeshDef;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact physical scene from Issues/s&amp;box Ghost Collisions. */
final class IssueSandboxGhostCollisionsSample extends AbstractBox3DSample {
    private static final float SRC = 0.0254f;
    private static final int HALF_LENGTH_U = 256;
    private static final int HALF_WIDTH_U = 64;
    private static final int TILE_SIZE_U = 32;
    private static final float BEAM_PITCH_U = 22.0f;
    private static final float BEAM_WIDTH_U = 12.0f;
    private static final float CHAMFER_WIDTH_U = 1.5f;
    private static final float CHAMFER_DROP_U = 1.0f;
    private static final float PIT_DEPTH_U = 24.0f;
    private static final int BEAM_COUNT = 9;
    private static final float BEAM_REGION_0 = -94.0f;
    private static final float BEAM_REGION_1 = 94.0f;
    private static final float BODY_HALF_WIDTH = 16.0f * SRC;
    private static final float BODY_HALF_HEIGHT = 36.0f * SRC;

    private final B3Mesh[] chunkMeshes = new B3Mesh[2];
    private B3Body character;
    private float walkDirectionX = 1.0f;
    private float walkDirectionZ = 1.0f;

    IssueSandboxGhostCollisionsSample() {
        createFloorChunk(0, -HALF_LENGTH_U, 0.0f);
        createFloorChunk(1, 0.0f, HALF_LENGTH_U);

        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), -3.5f, BODY_HALF_HEIGHT + 0.1f, 0.0f, null);
        B3MotionLocks locks = motionLocks(false, false, false, true, true, true);
        bodyDef.SetMotionLocks(locks);
        bodyDef.SetEnableSleep(false);
        bodyDef.SetEnableContactRecycling(false);
        bodyDef.SetGravityScale(2.03f);
        character = world().CreateBody(bodyDef);

        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetFriction(0.0f);
        material.SetRestitution(0.0f);
        shapeDef.SetBaseMaterial(material);
        float volume = 8.0f * BODY_HALF_WIDTH * BODY_HALF_HEIGHT * BODY_HALF_WIDTH;
        shapeDef.SetDensity(500.0f / volume);
        shapeDef.SetEnableSpeculativeContact(false);
        B3Hull box = B3Hull.CreateBox(BODY_HALF_WIDTH, BODY_HALF_HEIGHT, BODY_HALF_WIDTH);
        dispose(character.CreateHullShape(shapeDef, box));
        dispose(box, material, shapeDef, locks, bodyDef);
    }

    @Override
    public void step(float deltaSeconds) {
        B3Vec3 position = character.GetPosition();
        if(position.GetX() > 3.5f) {
            walkDirectionX = -1.0f;
        }
        else if(position.GetX() < -3.5f) {
            walkDirectionX = 1.0f;
        }
        if(position.GetZ() > 0.5f) {
            walkDirectionZ = -1.0f;
        }
        else if(position.GetZ() < -0.5f) {
            walkDirectionZ = 1.0f;
        }
        B3Vec3 velocity = character.GetLinearVelocity();
        velocity.SetX(walkDirectionX * 350.0f * SRC);
        velocity.SetZ(walkDirectionZ * 20.0f * SRC);
        character.SetLinearVelocity(velocity);
        dispose(velocity, position);
        super.step(deltaSeconds);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(character, chunkMeshes[1], chunkMeshes[0]);
    }

    private void createFloorChunk(int chunk, float x0, float x1) {
        MeshBuilder builder = new MeshBuilder();
        float[][] slabSpans = { { -HALF_LENGTH_U, BEAM_REGION_0 }, { BEAM_REGION_1, HALF_LENGTH_U } };
        for(float[] span : slabSpans) {
            float s0 = Math.max(span[0], x0);
            float s1 = Math.min(span[1], x1);
            if(s1 - s0 <= 0.01f) {
                continue;
            }
            for(float tileX = s0; tileX < s1; tileX += TILE_SIZE_U) {
                float tileX1 = Math.min(tileX + TILE_SIZE_U, s1);
                for(int tileZ = -HALF_WIDTH_U; tileZ < HALF_WIDTH_U; tileZ += TILE_SIZE_U) {
                    int hash = hash((int)tileX * 73856093 ^ tileZ * 19349663 ^ chunk * (int)2654435761L);
                    float[] cells = { 4.0f, 8.0f, 16.0f };
                    float cell = cells[(int)(Integer.toUnsignedLong(hash) % 3L)];
                    emitPatch(builder, tileX, tileX1, tileZ, tileZ + TILE_SIZE_U, 0.0f, cell);
                }
            }
        }

        float pitTop = -CHAMFER_DROP_U;
        float pitBottom = -PIT_DEPTH_U;
        for(int k = 0; k < BEAM_COUNT; k++) {
            float beamX = BEAM_REGION_0 + BEAM_PITCH_U * k;
            boolean pitLeft = k > 0;
            boolean pitRight = k < BEAM_COUNT - 1;
            float top0 = pitLeft ? beamX + CHAMFER_WIDTH_U : beamX;
            float top1 = pitRight ? beamX + BEAM_WIDTH_U - CHAMFER_WIDTH_U : beamX + BEAM_WIDTH_U;
            float s0 = Math.max(top0, x0);
            float s1 = Math.min(top1, x1);
            if(s1 - s0 > 0.01f) {
                emitPatch(builder, s0, s1, -HALF_WIDTH_U, HALF_WIDTH_U, 0.0f, 8.0f);
            }
            if(pitLeft && beamX >= x0 && beamX < x1) {
                emitSlope(builder, beamX, pitTop, beamX + CHAMFER_WIDTH_U, 0.0f, 8.0f);
            }
            if(pitRight && beamX + BEAM_WIDTH_U > x0 && beamX + BEAM_WIDTH_U <= x1) {
                emitSlope(builder, beamX + BEAM_WIDTH_U, pitTop,
                        beamX + BEAM_WIDTH_U - CHAMFER_WIDTH_U, 0.0f, 8.0f);
            }
            if(pitRight) {
                float pitLeftX = beamX + BEAM_WIDTH_U;
                float pitRightX = beamX + BEAM_PITCH_U;
                if(pitLeftX >= x0 && pitLeftX < x1) {
                    emitWall(builder, pitLeftX, pitBottom, pitTop, 1, 16.0f);
                }
                if(pitRightX > x0 && pitRightX <= x1) {
                    emitWall(builder, pitRightX, pitBottom, pitTop, -1, 16.0f);
                }
                s0 = Math.max(pitLeftX, x0);
                s1 = Math.min(pitRightX, x1);
                if(s1 - s0 > 0.01f) {
                    emitPatch(builder, s0, s1, -HALF_WIDTH_U, HALF_WIDTH_U, pitBottom, 16.0f);
                }
            }
        }

        B3MeshDef meshDef = builder.meshDef;
        meshDef.SetWeldVertices(true);
        meshDef.SetWeldTolerance(0.005f);
        meshDef.SetIdentifyEdges(true);
        chunkMeshes[chunk] = B3Mesh.CreateFromDef(meshDef);
        B3Body body = createBody(B3.StaticBody(), 0.0f, 0.0f, 0.0f, null);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 scale = new B3Vec3(1.0f, 1.0f, 1.0f);
        dispose(body.CreateMeshShape(shapeDef, chunkMeshes[chunk], scale), scale, shapeDef, body, meshDef);
    }

    private static void emitPatch(MeshBuilder builder, float x0, float x1, float z0, float z1, float y,
            float cell) {
        if(x1 - x0 < 0.01f) {
            return;
        }
        int countX = (int)((x1 - x0) / cell + 0.99f);
        int countZ = (int)((z1 - z0) / cell + 0.99f);
        for(int ix = 0; ix < countX; ix++) {
            for(int iz = 0; iz < countZ; iz++) {
                float cx0 = x0 + (x1 - x0) * ix / countX;
                float cx1 = x0 + (x1 - x0) * (ix + 1) / countX;
                float cz0 = z0 + (z1 - z0) * iz / countZ;
                float cz1 = z0 + (z1 - z0) * (iz + 1) / countZ;
                float[] a = point(cx0, y, cz0);
                float[] b = point(cx1, y, cz0);
                float[] c = point(cx1, y, cz1);
                float[] d = point(cx0, y, cz1);
                if(((ix + iz) & 1) != 0) {
                    builder.triangle(a, d, c);
                    builder.triangle(a, c, b);
                }
                else {
                    builder.triangle(a, d, b);
                    builder.triangle(b, d, c);
                }
            }
        }
    }

    private static void emitSlope(MeshBuilder builder, float xLow, float yLow, float xHigh, float yHigh,
            float zCell) {
        int countZ = (int)(2.0f * HALF_WIDTH_U / zCell + 0.99f);
        for(int iz = 0; iz < countZ; iz++) {
            float z0 = -HALF_WIDTH_U + 2.0f * HALF_WIDTH_U * iz / countZ;
            float z1 = -HALF_WIDTH_U + 2.0f * HALF_WIDTH_U * (iz + 1) / countZ;
            float[] low0 = point(xLow, yLow, z0);
            float[] low1 = point(xLow, yLow, z1);
            float[] high0 = point(xHigh, yHigh, z0);
            float[] high1 = point(xHigh, yHigh, z1);
            builder.triangle(low0, low1, high1);
            builder.triangle(low0, high1, high0);
        }
    }

    private static void emitWall(MeshBuilder builder, float x, float y0, float y1, int facing, float zCell) {
        int countZ = (int)(2.0f * HALF_WIDTH_U / zCell + 0.99f);
        for(int iz = 0; iz < countZ; iz++) {
            float z0 = -HALF_WIDTH_U + 2.0f * HALF_WIDTH_U * iz / countZ;
            float z1 = -HALF_WIDTH_U + 2.0f * HALF_WIDTH_U * (iz + 1) / countZ;
            float[] bottom0 = point(x, y0, z0);
            float[] bottom1 = point(x, y0, z1);
            float[] top0 = point(x, y1, z0);
            float[] top1 = point(x, y1, z1);
            if(facing > 0) {
                builder.triangle(bottom0, bottom1, top1);
                builder.triangle(bottom0, top1, top0);
            }
            else {
                builder.triangle(bottom0, top0, top1);
                builder.triangle(bottom0, top1, bottom1);
            }
        }
    }

    private static float[] point(float x, float y, float z) {
        return new float[] { SRC * x, SRC * y, SRC * z };
    }

    private static int hash(int value) {
        value ^= value >>> 16;
        value *= 0x7feb352d;
        value ^= value >>> 15;
        value *= 0x846ca68b;
        value ^= value >>> 16;
        return value;
    }

    private static final class MeshBuilder {
        private final B3MeshDef meshDef = new B3MeshDef(0, 0);
        private int vertexCount;

        private void triangle(float[] a, float[] b, float[] c) {
            add(a);
            add(b);
            add(c);
            meshDef.AddTriangle(vertexCount, vertexCount + 1, vertexCount + 2, 0);
            vertexCount += 3;
        }

        private void add(float[] point) {
            B3Vec3 vertex = new B3Vec3(point[0], point[1], point[2]);
            meshDef.AddVertex(vertex);
            dispose(vertex);
        }
    }
}
