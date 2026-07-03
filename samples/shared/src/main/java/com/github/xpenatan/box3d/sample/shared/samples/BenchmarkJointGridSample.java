package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3DistanceJointDef;
import com.github.xpenatan.box3d.B3Filter;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3MotionLocks;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3SphericalJointDef;
import com.github.xpenatan.box3d.B3Vec3;

import java.util.ArrayList;
import java.util.List;

final class BenchmarkJointGridSample extends AbstractBox3DSample {
    BenchmarkJointGridSample() {
        addGroundBox(35.0f);
        B3Body[][] grid = new B3Body[8][8];
        for(int y = 0; y < grid.length; y++) {
            for(int x = 0; x < grid[y].length; x++) {
                grid[y][x] = addDynamicBox(-7.0f + x * 2.0f, 16.0f - y * 1.4f, 0.0f, 0.35f, 0.35f, 0.35f);
                if(x > 0) {
                    SamplePortUtil.createDistance(this, grid[y][x - 1], grid[y][x], 2.0f, 6.0f, 0.7f);
                }
                if(y > 0) {
                    SamplePortUtil.createDistance(this, grid[y - 1][x], grid[y][x], 1.4f, 6.0f, 0.7f);
                }
            }
        }
    }
}
