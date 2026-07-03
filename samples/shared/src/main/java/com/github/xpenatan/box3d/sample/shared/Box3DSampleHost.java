package com.github.xpenatan.box3d.sample.shared;

import com.github.xpenatan.box3d.B3World;

public interface Box3DSampleHost {
    default void onSampleChanged(Box3DSampleEntry entry, Box3DSample sample) {
    }

    void renderBox3D(B3World world);

    void requestExit();
}
