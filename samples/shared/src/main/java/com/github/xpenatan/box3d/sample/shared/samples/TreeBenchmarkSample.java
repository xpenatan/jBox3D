package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3AABB;
import com.github.xpenatan.box3d.B3Vec3;

/** Exact default visual scene for Tree/Benchmark ({@code bounds01}, leaf level). */
final class TreeBenchmarkSample extends AbstractBox3DSample {
    TreeBenchmarkSample() {
        String[] lines = SampleAssets.readUtf8("data/trees/bounds01.txt").split("\\r?\\n");
        for(String line : lines) {
            String trimmed = line.trim();
            if(trimmed.length() == 0 || trimmed.charAt(0) == '#') {
                continue;
            }
            String[] values = trimmed.split("\\s+");
            if(values.length != 6) {
                continue;
            }
            B3Vec3 lower = new B3Vec3(Float.parseFloat(values[0]), Float.parseFloat(values[1]),
                    Float.parseFloat(values[2]));
            B3Vec3 upper = new B3Vec3(Float.parseFloat(values[3]), Float.parseFloat(values[4]),
                    Float.parseFloat(values[5]));
            B3AABB bounds = new B3AABB(lower, upper);
            world().AddDebugBounds(bounds, 0xADD8E6);
            dispose(bounds, upper, lower);
        }
        addDebugAxes(0.0f, 0.0f, 0.0f, 2.0f);
    }
}
