package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3MoverCollision;
import com.github.xpenatan.box3d.B3MoverPlaneResult;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3Vec3;

/** Direct port of Character/CapsulePlane from the pinned Box3D sample. */
final class CharacterCapsulePlaneSample extends AbstractBox3DSample {
    private final B3Vec3 position;
    private final B3Capsule capsule;

    CharacterCapsulePlaneSample() {
        position = new B3Vec3(0.0f, 1.0f, 0.4f);
        B3Vec3 center1 = new B3Vec3(0.0f, -0.5f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, 0.5f, 0.0f);
        capsule = new B3Capsule(center1, center2, 0.25f);
        dispose(center2, center1);

        addStaticBox(0.0f, 1.0f, 1.0f, 0.5f, 0.5f, 0.5f, null);
    }

    @Override
    public void step(float deltaSeconds) {
        world().ClearDebugOverlay();
        B3QueryFilter filter = new B3QueryFilter();
        B3MoverCollision collision = world().CollideMover(position, capsule, filter, 3);

        B3Vec3 p1 = offset(position, 0.0f, -0.5f, 0.0f);
        B3Vec3 p2 = offset(position, 0.0f, 0.5f, 0.0f);
        world().AddDebugCapsule(p1, p2, capsule.GetRadius(), 0x008000, 1.0f);
        dispose(p2, p1);

        for(int i = 0; i < collision.GetCount(); ++i) {
            B3MoverPlaneResult result = collision.GetResult(i);
            B3Vec3 normal = result.GetNormal();
            float distance = result.GetOffset() - capsule.GetRadius();
            B3Vec3 point = offset(position, distance * normal.GetX(), distance * normal.GetY(),
                    distance * normal.GetZ());
            B3Vec3 normalEnd = offset(point, 0.1f * normal.GetX(), 0.1f * normal.GetY(),
                    0.1f * normal.GetZ());
            world().AddDebugPoint(point, 5.0f, 0xFFFF00);
            world().AddDebugSegment(point, normalEnd, 0xFFFF00);
            dispose(normalEnd, point, normal, result);
        }

        addDebugGroundGrid(10);
        addDebugAxes(0.0f, 0.0f, 0.0f, 2.0f);
        dispose(collision, filter);
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
