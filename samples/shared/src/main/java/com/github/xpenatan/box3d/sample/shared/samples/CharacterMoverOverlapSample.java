package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Collision;
import com.github.xpenatan.box3d.B3MoverCollision;
import com.github.xpenatan.box3d.B3MoverPlaneResult;
import com.github.xpenatan.box3d.B3PlaneSolverResult;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3Vec3;

/** Direct port of Character/MoverOverlap from the pinned Box3D sample. */
final class CharacterMoverOverlapSample extends AbstractBox3DSample {
    private final B3Vec3 position;
    private final B3Capsule capsule;

    CharacterMoverOverlapSample() {
        B3Vec3 center1 = new B3Vec3(0.0f, -0.5f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, 0.5f, 0.0f);
        capsule = new B3Capsule(center1, center2, 0.35f);
        position = new B3Vec3(0.0f, 3.5f, 0.0f);
        dispose(center2, center1);

        B3Body sphereBody = createBody(B3.StaticBody(), -3.0f, 1.0f, 0.0f, null);
        addSphereShape(sphereBody, 0.0f, 0.0f, 0.0f, 0.6f, 1.0f, 0.6f, 0.0f, 0.0f);
        dispose(sphereBody);
        addCapsule(B3.StaticBody(), 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, -0.7f, 0.0f, 0.0f, 0.7f, 0.4f,
                null, 1.0f, 0.6f, 0.0f, 0.0f);
        addStaticBox(3.0f, 1.0f, 0.0f, 0.6f, 0.6f, 0.6f, null);
    }

    @Override
    public void step(float deltaSeconds) {
        world().ClearDebugOverlay();
        B3QueryFilter filter = new B3QueryFilter();
        B3MoverCollision collision = world().CollideMover(position, capsule, filter, 32);

        drawCapsule(position, 0xFFFF00);
        for(int i = 0; i < collision.GetCount(); ++i) {
            B3MoverPlaneResult result = collision.GetResult(i);
            B3Vec3 point = result.GetPoint();
            B3Vec3 normal = result.GetNormal();
            B3Vec3 worldPoint = offset(position, point.GetX(), point.GetY(), point.GetZ());
            float lengthSquared = normal.GetX() * normal.GetX() + normal.GetY() * normal.GetY()
                    + normal.GetZ() * normal.GetZ();
            int color = Math.abs(lengthSquared - 1.0f) <= 4.0f * 1.1920929E-7f ? 0x32CD32 : 0xFF0000;
            B3Vec3 normalEnd = offset(worldPoint, 0.5f * normal.GetX(), 0.5f * normal.GetY(),
                    0.5f * normal.GetZ());
            world().AddDebugPoint(worldPoint, 6.0f, color);
            world().AddDebugSegment(worldPoint, normalEnd, color);
            dispose(normalEnd, worldPoint, normal, point, result);
        }

        B3Vec3 zero = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3PlaneSolverResult solved = B3Collision.SolveMoverPlanes(zero, collision);
        B3Vec3 delta = solved.GetDelta();
        B3Vec3 pushedPosition = offset(position, delta.GetX(), delta.GetY(), delta.GetZ());
        drawCapsule(pushedPosition, 0x00FFFF);
        addDebugGroundGrid(12);

        dispose(pushedPosition, delta, solved, zero, collision, filter);
    }

    private void drawCapsule(B3Vec3 origin, int color) {
        B3Vec3 p1 = offset(origin, 0.0f, -0.5f, 0.0f);
        B3Vec3 p2 = offset(origin, 0.0f, 0.5f, 0.0f);
        world().AddDebugCapsule(p1, p2, capsule.GetRadius(), color, 1.0f);
        dispose(p2, p1);
    }

    private static B3Vec3 offset(B3Vec3 p, float x, float y, float z) {
        return new B3Vec3(p.GetX() + x, p.GetY() + y, p.GetZ() + z);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(capsule, position);
    }
}
