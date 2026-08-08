package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3RayResult;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact port of Collision / Capsule Cast Ray. */
final class CapsuleCastRaySample extends AbstractBox3DSample {
    CapsuleCastRaySample() {
        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.KinematicBody());
        B3Body body = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = new B3ShapeDef();
        B3Vec3 center1 = new B3Vec3(0.0f, 0.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, 0.5f);
        dispose(body.CreateCapsuleShape(shapeDef, capsule));

        addDebugGroundGrid(10);
        addDebugAxes(0.0f, 0.0f, 0.0f, 0.4f);
        B3Vec3 origin = new B3Vec3(-1.0f, 0.5f, 0.0f);
        B3Vec3 translation = new B3Vec3(2.0f, 0.0f, 0.0f);
        B3Vec3 end = new B3Vec3(1.0f, 0.5f, 0.0f);
        B3QueryFilter filter = new B3QueryFilter();
        B3Transform identity = new B3Transform();
        B3RayResult result = body.CastRay(origin, translation, filter, 1.0f, identity);
        world().AddDebugSegment(origin, end, 0x808080);
        world().AddDebugPoint(origin, 4.0f, 0x008000);
        world().AddDebugPoint(end, 4.0f, 0xFF0000);
        if(result.GetHit()) {
            B3Vec3 point = result.GetPoint();
            world().AddDebugPoint(point, 4.0f, 0xFFA500);
            dispose(point);
        }
        dispose(result, identity, filter, end, translation, origin, capsule, center2, center1, shapeDef, body,
                bodyDef);
    }
}
