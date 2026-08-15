package com.github.xpenatan.box3d.sample.shared;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3AABB;
import com.github.xpenatan.box3d.B3DebugDrawEm;
import com.github.xpenatan.box3d.B3DebugShape;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3World;

/** Counts the actual Box3D debug-draw output used by the visual sample validation. */
public final class Box3DVisualProbe extends B3DebugDrawEm {
    private static final int MAX_DRAW_QUERY_SHAPES = 2048;
    private static final int MAX_DRAW_QUERY_SPLITS = 32;

    private final B3Vec3 queryLower = new B3Vec3();
    private final B3Vec3 queryUpper = new B3Vec3();
    private final B3AABB queryBounds = new B3AABB(queryLower, queryUpper);
    private final B3QueryFilter queryFilter = new B3QueryFilter();
    private int primitiveCount;
    private boolean finite;

    public Box3DVisualProbe() {
        SetDrawShapes(true);
        SetDrawJoints(true);
    }

    public int inspect(B3World world) {
        primitiveCount = 0;
        finite = true;

        B3AABB worldBounds = world.GetBounds();
        B3Vec3 worldLower = worldBounds.GetLowerBound();
        B3Vec3 worldUpper = worldBounds.GetUpperBound();
        float lowerX = worldLower.GetX();
        float lowerY = worldLower.GetY();
        float lowerZ = worldLower.GetZ();
        float upperX = worldUpper.GetX();
        float upperY = worldUpper.GetY();
        float upperZ = worldUpper.GetZ();
        if(!finiteBounds(lowerX, lowerY, lowerZ, upperX, upperY, upperZ)) {
            finite = false;
            return 0;
        }

        setQueryBounds(lowerX, lowerY, lowerZ, upperX, upperY, upperZ);
        int overlapCount = world.CountOverlapsAABB(queryBounds, queryFilter);
        for(int split = 0; overlapCount > MAX_DRAW_QUERY_SHAPES && split < MAX_DRAW_QUERY_SPLITS; split++) {
            float spanX = upperX - lowerX;
            float spanY = upperY - lowerY;
            float spanZ = upperZ - lowerZ;
            if(spanX >= spanY && spanX >= spanZ && spanX > 0.0f) {
                float middle = lowerX + 0.5f * spanX;
                setQueryBounds(lowerX, lowerY, lowerZ, middle, upperY, upperZ);
                int lowerCount = world.CountOverlapsAABB(queryBounds, queryFilter);
                if(lowerCount > 0) {
                    upperX = middle;
                    overlapCount = lowerCount;
                }
                else {
                    lowerX = middle;
                    setQueryBounds(lowerX, lowerY, lowerZ, upperX, upperY, upperZ);
                    overlapCount = world.CountOverlapsAABB(queryBounds, queryFilter);
                }
            }
            else if(spanY >= spanZ && spanY > 0.0f) {
                float middle = lowerY + 0.5f * spanY;
                setQueryBounds(lowerX, lowerY, lowerZ, upperX, middle, upperZ);
                int lowerCount = world.CountOverlapsAABB(queryBounds, queryFilter);
                if(lowerCount > 0) {
                    upperY = middle;
                    overlapCount = lowerCount;
                }
                else {
                    lowerY = middle;
                    setQueryBounds(lowerX, lowerY, lowerZ, upperX, upperY, upperZ);
                    overlapCount = world.CountOverlapsAABB(queryBounds, queryFilter);
                }
            }
            else if(spanZ > 0.0f) {
                float middle = lowerZ + 0.5f * spanZ;
                setQueryBounds(lowerX, lowerY, lowerZ, upperX, upperY, middle);
                int lowerCount = world.CountOverlapsAABB(queryBounds, queryFilter);
                if(lowerCount > 0) {
                    upperZ = middle;
                    overlapCount = lowerCount;
                }
                else {
                    lowerZ = middle;
                    setQueryBounds(lowerX, lowerY, lowerZ, upperX, upperY, upperZ);
                    overlapCount = world.CountOverlapsAABB(queryBounds, queryFilter);
                }
            }
            else {
                break;
            }
        }

        // Drawing the complete bounds of a million-body benchmark can exhaust the
        // browser heap. The adaptive query above still asks Box3D to emit real draw
        // primitives, but confines that proof to a populated part of the world.
        SetDrawingBounds(queryBounds);
        DrawWorld(world, B3.DefaultMaskBits());
        return primitiveCount;
    }

    public void disposeResources() {
        queryFilter.dispose();
        queryBounds.dispose();
        queryUpper.dispose();
        queryLower.dispose();
    }

    public boolean isFinite() {
        return finite;
    }

    @Override
    protected void DrawShape(B3DebugShape shape, B3Transform transform, int color) {
        primitiveCount++;
        check(transform);
        B3Vec3 scale = shape.GetScale();
        check(scale);
    }

    @Override
    protected void DrawSegment(B3Vec3 p1, B3Vec3 p2, int color) {
        primitiveCount++;
        check(p1);
        check(p2);
    }

    @Override
    protected void DrawTransform(B3Transform transform) {
        primitiveCount++;
        check(transform);
    }

    @Override
    protected void DrawPoint(B3Vec3 point, float size, int color) {
        primitiveCount++;
        check(point);
        finite &= Float.isFinite(size);
    }

    @Override
    protected void DrawSphere(B3Vec3 point, float radius, int color, float alpha) {
        primitiveCount++;
        check(point);
        finite &= Float.isFinite(radius) && Float.isFinite(alpha);
    }

    @Override
    protected void DrawCapsule(B3Vec3 p1, B3Vec3 p2, float radius, int color, float alpha) {
        primitiveCount++;
        check(p1);
        check(p2);
        finite &= Float.isFinite(radius) && Float.isFinite(alpha);
    }

    @Override
    protected void DrawBounds(B3AABB bounds, int color) {
        primitiveCount++;
        B3Vec3 lower = bounds.GetLowerBound();
        B3Vec3 upper = bounds.GetUpperBound();
        check(lower);
        check(upper);
    }

    @Override
    protected void DrawBox(B3Vec3 extents, B3Transform transform, int color) {
        primitiveCount++;
        check(extents);
        check(transform);
    }

    private void check(B3Transform transform) {
        B3Vec3 position = transform.GetP();
        B3Quat rotation = transform.GetQ();
        B3Vec3 vector = rotation.GetV();
        check(position);
        check(vector);
        finite &= Float.isFinite(rotation.GetS());
    }

    private void check(B3Vec3 vector) {
        finite &= Float.isFinite(vector.GetX()) && Float.isFinite(vector.GetY()) && Float.isFinite(vector.GetZ());
    }

    private void setQueryBounds(float lowerX, float lowerY, float lowerZ,
                                float upperX, float upperY, float upperZ) {
        queryLower.Set(lowerX, lowerY, lowerZ);
        queryUpper.Set(upperX, upperY, upperZ);
        queryBounds.SetLowerBound(queryLower);
        queryBounds.SetUpperBound(queryUpper);
    }

    private static boolean finiteBounds(float lowerX, float lowerY, float lowerZ,
                                        float upperX, float upperY, float upperZ) {
        return Float.isFinite(lowerX) && Float.isFinite(lowerY) && Float.isFinite(lowerZ)
                && Float.isFinite(upperX) && Float.isFinite(upperY) && Float.isFinite(upperZ)
                && lowerX <= upperX && lowerY <= upperY && lowerZ <= upperZ;
    }
}
