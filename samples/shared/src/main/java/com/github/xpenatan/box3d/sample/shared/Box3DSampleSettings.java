package com.github.xpenatan.box3d.sample.shared;

public final class Box3DSampleSettings {
    public static final int MIN_SUB_STEPS = 1;
    public static final int MAX_SUB_STEPS = 50;
    public static final int MIN_WORKERS = 1;
    public static final int MAX_WORKERS = 32;
    public static final float MIN_HERTZ = 5.0f;
    public static final float MAX_HERTZ = 240.0f;
    public static final float MIN_RECYCLE_CENTIMETERS = 0.0f;
    public static final float MAX_RECYCLE_CENTIMETERS = 10.0f;
    public static final float MIN_LAUNCH_SPEED = 1.0f;
    public static final float MAX_LAUNCH_SPEED = 80.0f;
    public static final float DEFAULT_LAUNCH_SPEED = 20.0f;
    public static final float MIN_SHADOW_BIAS = 0.0f;
    public static final float MAX_SHADOW_BIAS = 0.08f;
    public static final float SHADOW_BIAS_STEP = 0.001f;
    public static final float DEFAULT_SHADOW_BIAS = 0.001f;

    private int subStepCount = 4;
    private float hertz = 60.0f;
    private int workerCount = 1;
    private float recycleDistance = 0.05f;
    private boolean sleepEnabled = true;
    private boolean warmStartingEnabled = true;
    private boolean continuousEnabled = true;
    private int launchShapeIndex;
    private float launchSpeed = DEFAULT_LAUNCH_SPEED;
    private float shadowBias = DEFAULT_SHADOW_BIAS;

    public int subStepCount() {
        return subStepCount;
    }

    public void setSubStepCount(int subStepCount) {
        this.subStepCount = clamp(subStepCount, MIN_SUB_STEPS, MAX_SUB_STEPS);
    }

    public float hertz() {
        return hertz;
    }

    public void setHertz(float hertz) {
        this.hertz = clamp(hertz, MIN_HERTZ, MAX_HERTZ);
    }

    public int workerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = clamp(workerCount, MIN_WORKERS, MAX_WORKERS);
    }

    public float recycleDistance() {
        return recycleDistance;
    }

    public float recycleCentimeters() {
        return recycleDistance * 100.0f;
    }

    public void setRecycleCentimeters(float centimeters) {
        recycleDistance = clamp(centimeters, MIN_RECYCLE_CENTIMETERS, MAX_RECYCLE_CENTIMETERS) * 0.01f;
    }

    public boolean sleepEnabled() {
        return sleepEnabled;
    }

    public void setSleepEnabled(boolean sleepEnabled) {
        this.sleepEnabled = sleepEnabled;
    }

    public boolean warmStartingEnabled() {
        return warmStartingEnabled;
    }

    public void setWarmStartingEnabled(boolean warmStartingEnabled) {
        this.warmStartingEnabled = warmStartingEnabled;
    }

    public boolean continuousEnabled() {
        return continuousEnabled;
    }

    public void setContinuousEnabled(boolean continuousEnabled) {
        this.continuousEnabled = continuousEnabled;
    }

    public int launchShapeIndex() {
        return launchShapeIndex;
    }

    public void setLaunchShapeIndex(int launchShapeIndex) {
        this.launchShapeIndex = clamp(launchShapeIndex, 0, Box3DLaunchShape.values().length - 1);
    }

    public Box3DLaunchShape launchShape() {
        return Box3DLaunchShape.byIndex(launchShapeIndex);
    }

    public float launchSpeed() {
        return launchSpeed;
    }

    public void setLaunchSpeed(float launchSpeed) {
        this.launchSpeed = clamp(launchSpeed, MIN_LAUNCH_SPEED, MAX_LAUNCH_SPEED);
    }

    public float shadowBias() {
        return shadowBias;
    }

    public void setShadowBias(float shadowBias) {
        this.shadowBias = clamp(shadowBias, MIN_SHADOW_BIAS, MAX_SHADOW_BIAS);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }
}
