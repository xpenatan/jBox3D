package com.github.xpenatan.box3d.sample.shared;

/** Mutable camera-follow target, avoiding platform-specific vector types in shared samples. */
public final class Box3DPlayerTarget {
    private float x;
    private float y;
    private float z;

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }
}
