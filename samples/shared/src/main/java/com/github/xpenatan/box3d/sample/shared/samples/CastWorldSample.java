package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Vec3;

/** Exact default state of Collision / Cast World at the pinned Box3D commit. */
final class CastWorldSample extends AbstractBox3DSample {
    CastWorldSample() {
        B3Vec3 origin = new B3Vec3(-20.0f, 10.0f, 0.0f);
        B3Vec3 end = new B3Vec3(0.0f, 20.0f, 0.0f);
        world().AddDebugSegment(origin, end, 0x00FFFF);
        world().AddDebugPoint(origin, 10.0f, 0x008000);
        addDebugGroundGrid(10);
        addDebugAxes(0.0f, 0.0f, 0.0f, 1.0f);
        dispose(end, origin);
    }
}
