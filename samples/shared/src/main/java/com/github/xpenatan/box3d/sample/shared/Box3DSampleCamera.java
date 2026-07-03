package com.github.xpenatan.box3d.sample.shared;

public final class Box3DSampleCamera {
    public final float positionX;
    public final float positionY;
    public final float positionZ;
    public final float targetX;
    public final float targetY;
    public final float targetZ;

    public Box3DSampleCamera(float positionX, float positionY, float positionZ, float targetX, float targetY,
            float targetZ) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }
}
