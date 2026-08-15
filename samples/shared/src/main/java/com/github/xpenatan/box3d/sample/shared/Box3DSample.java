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

    default boolean supportsPlayerControl() {
        return false;
    }

    default boolean startsInThirdPerson() {
        return false;
    }

    default void setPlayerInput(Box3DPlayerInput input, boolean thirdPerson) {
    }

    default boolean getCameraTarget(Box3DPlayerTarget target) {
        return false;
    }

    B3World world();

    void dispose();
}
