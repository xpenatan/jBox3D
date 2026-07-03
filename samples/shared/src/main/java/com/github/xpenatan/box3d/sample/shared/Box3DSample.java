package com.github.xpenatan.box3d.sample.shared;

import com.github.xpenatan.box3d.B3World;

public interface Box3DSample {
    void step(float deltaSeconds);

    default void step(float deltaSeconds, Box3DSampleSettings settings) {
        step(deltaSeconds);
    }

    default void launchShape(Box3DLaunchShape shape, float originX, float originY, float originZ, float directionX,
            float directionY, float directionZ, float speed) {
    }

    B3World world();

    void dispose();
}
