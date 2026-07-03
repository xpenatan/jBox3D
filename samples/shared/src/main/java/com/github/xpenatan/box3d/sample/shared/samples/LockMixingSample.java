package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

final class LockMixingSample extends AbstractBox3DSample {
    LockMixingSample() {
        addGroundBox(20.0f);
        addLockedCube("free", 0.0f, 2.0f, 0.0f, null);
        addLockedCube("angular xz", 2.0f, 2.0f, 0.0f, motionLocks(false, false, false, true, false, true));
        addLockedCube("linear xyz", -2.0f, 2.0f, 0.0f, motionLocks(true, true, true, false, false, false));
        addLockedCube("full", 0.0f, 1.0f, 2.0f, motionLocks(true, true, true, true, true, true));
        addStaticBox(0.0f, 1.0f, -3.0f, 1.0f, 1.0f, 1.0f, null);
    }

    private void addLockedCube(String ignoredName, float x, float y, float z, B3MotionLocks locks) {
        com.github.xpenatan.box3d.B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), x, y, z, null);
        if(locks != null) {
            bodyDef.SetMotionLocks(locks);
        }
        com.github.xpenatan.box3d.B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.0f);
        com.github.xpenatan.box3d.B3Hull hull = com.github.xpenatan.box3d.B3Hull.CreateBox(1.0f, 1.0f, 1.0f);
        B3Body body = world().CreateBody(bodyDef);
        dispose(body.CreateHullShape(shapeDef, hull));
        dispose(hull, shapeDef, locks, bodyDef);
    }
}
