package com.github.xpenatan.box3d.sample.shared;

/** Platform-neutral input state consumed by the player-controlled Box3D samples. */
public final class Box3DPlayerInput {
    private float moveForward;
    private float moveRight;
    private float cameraForwardX;
    private float cameraForwardZ = -1.0f;
    private float cameraRightX = 1.0f;
    private float cameraRightZ;
    private boolean jump;
    private boolean sprint;

    void set(float moveForward, float moveRight, float cameraForwardX, float cameraForwardZ,
            float cameraRightX, float cameraRightZ, boolean jump, boolean sprint) {
        this.moveForward = clamp(moveForward);
        this.moveRight = clamp(moveRight);
        float forwardLength = length(cameraForwardX, cameraForwardZ);
        float rightLength = length(cameraRightX, cameraRightZ);
        if(forwardLength > 0.000001f) {
            this.cameraForwardX = cameraForwardX / forwardLength;
            this.cameraForwardZ = cameraForwardZ / forwardLength;
        }
        if(rightLength > 0.000001f) {
            this.cameraRightX = cameraRightX / rightLength;
            this.cameraRightZ = cameraRightZ / rightLength;
        }
        this.jump = jump;
        this.sprint = sprint;
    }

    void clearMovement() {
        moveForward = 0.0f;
        moveRight = 0.0f;
        jump = false;
        sprint = false;
    }

    public float moveForward() {
        return moveForward;
    }

    public float moveRight() {
        return moveRight;
    }

    public float cameraForwardX() {
        return cameraForwardX;
    }

    public float cameraForwardZ() {
        return cameraForwardZ;
    }

    public float cameraRightX() {
        return cameraRightX;
    }

    public float cameraRightZ() {
        return cameraRightZ;
    }

    public boolean jump() {
        return jump;
    }

    public boolean sprint() {
        return sprint;
    }

    private static float clamp(float value) {
        return Math.max(-1.0f, Math.min(1.0f, value));
    }

    private static float length(float x, float z) {
        return (float)Math.sqrt(x * x + z * z);
    }
}
