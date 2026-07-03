package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3PrismaticJointDef;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3QueryFilter;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3WheelJointDef;

final class MeshViewerSample extends AbstractBox3DSample {
    MeshViewerSample() {
        addGroundBox(24.0f);
        DiagnosticUtil.addMeshBowl(this, 10, 1.0f);
        B3Hull rock = B3Hull.CreateRock(1.1f);
        addHull(rock, B3.DynamicBody(), 0.0f, 6.0f, 0.0f, null, 1.0f, 0.6f, 0.0f, 0.0f);
        dispose(rock);
    }
}
