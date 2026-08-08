package com.github.xpenatan.box3d.sample.shared;

public final class Box3DSampleCamera {
    public final float yawDegrees;
    public final float pitchDegrees;
    public final float radius;
    public final float positionX;
    public final float positionY;
    public final float positionZ;
    public final float targetX;
    public final float targetY;
    public final float targetZ;

    /**
     * Matches the Box3D sample camera's SetView(yawDegrees, pitchDegrees, radius, pivot) contract.
     */
    public Box3DSampleCamera(float yawDegrees, float pitchDegrees, float radius, float targetX, float targetY,
            float targetZ) {
        this.yawDegrees = yawDegrees;
        this.pitchDegrees = pitchDegrees;
        this.radius = radius;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;

        double yaw = Math.toRadians(yawDegrees);
        double pitch = Math.toRadians(pitchDegrees);
        double cosPitch = Math.cos(pitch);
        this.positionX = targetX + radius * (float)(Math.sin(yaw) * cosPitch);
        this.positionY = targetY + radius * (float)Math.sin(pitch);
        this.positionZ = targetZ + radius * (float)(Math.cos(yaw) * cosPitch);
    }
}
