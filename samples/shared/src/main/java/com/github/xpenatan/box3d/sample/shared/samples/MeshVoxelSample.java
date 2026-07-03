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

final class MeshVoxelSample extends AbstractBox3DSample {
    MeshVoxelSample() {
        addGroundBox(24.0f);
        for(int x = -4; x <= 4; x++) {
            for(int y = 0; y < 5; y++) {
                for(int z = -4; z <= 4; z++) {
                    if(Math.abs(x) + Math.abs(z) + y < 7 && ((x + y + z) & 1) == 0) {
                        addDynamicBox(x, 1.0f + y, z, 0.42f, 0.42f, 0.42f);
                    }
                }
            }
        }
    }
}
