package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Vec3;

final class WeebleSample extends AbstractBox3DSample {
    WeebleSample() {
        addGroundBox(30.0f);
        B3BodyDef bodyDef = bodyDef(B3.DynamicBody(), 0.0f, 3.0f, 0.0f, null);
        B3Body weeble = world().CreateBody(bodyDef);
        B3ShapeDef shapeDef = shapeDef(1.0f, 0.6f, 0.0f, 0.1f);
        B3Vec3 center1 = new B3Vec3(0.0f, -1.0f, 0.0f);
        B3Vec3 center2 = new B3Vec3(0.0f, 1.0f, 0.0f);
        B3Capsule capsule = new B3Capsule(center1, center2, 1.0f);
        dispose(weeble.CreateCapsuleShape(shapeDef, capsule));

        float mass = weeble.GetMass();
        B3Vec3 inertiaX = weeble.GetLocalRotationalInertiaColumnX();
        B3Vec3 inertiaY = weeble.GetLocalRotationalInertiaColumnY();
        B3Vec3 inertiaZ = weeble.GetLocalRotationalInertiaColumnZ();
        B3Vec3 massCenter = new B3Vec3(0.0f, -1.5f, 0.0f);
        float steiner = mass * 1.5f * 1.5f;
        inertiaX.SetX(inertiaX.GetX() + steiner);
        inertiaZ.SetZ(inertiaZ.GetZ() + steiner);
        weeble.SetMassData(mass, massCenter, inertiaX, inertiaY, inertiaZ);

        dispose(massCenter, inertiaZ, inertiaY, inertiaX, capsule, center2, center1, shapeDef, bodyDef);
    }
}
