package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Vec3;

/** Matches the deterministic XorShift32 helpers in Box3D's shared/utils.h. */
final class SampleRandom {
    static final int DEFAULT_SEED = 12345;
    private static final int LIMIT = 32767;

    private int seed;

    SampleRandom() {
        this(DEFAULT_SEED);
    }

    SampleRandom(int seed) {
        this.seed = seed;
    }

    void setSeed(int seed) {
        this.seed = seed;
    }

    int nextInt() {
        int x = seed;
        x ^= x << 13;
        x ^= x >>> 17;
        x ^= x << 5;
        seed = x;
        return x & LIMIT;
    }

    int nextInt(int minimum, int maximum) {
        return minimum + nextInt() % (maximum - minimum + 1);
    }

    float nextFloat() {
        float value = nextInt();
        value /= LIMIT;
        return 2.0f * value - 1.0f;
    }

    float nextFloat(float minimum, float maximum) {
        float value = nextInt();
        value /= LIMIT;
        return (maximum - minimum) * value + minimum;
    }

    B3Quat nextQuaternion() {
        float u1 = nextFloat(0.0f, 1.0f);
        float u2 = nextFloat(0.0f, 2.0f * SampleMath.PI);
        float u3 = nextFloat(0.0f, 2.0f * SampleMath.PI);
        float sqrt1MinusU1 = (float)Math.sqrt(1.0f - u1);
        float sqrtU1 = (float)Math.sqrt(u1);
        long cosSin2 = SampleMath.computeCosSin(u2);
        long cosSin3 = SampleMath.computeCosSin(u3);
        return new B3Quat(
                sqrt1MinusU1 * SampleMath.sine(cosSin2),
                sqrt1MinusU1 * SampleMath.cosine(cosSin2),
                sqrtU1 * SampleMath.sine(cosSin3),
                sqrtU1 * SampleMath.cosine(cosSin3));
    }

    B3Vec3 nextUnitVector() {
        float u1 = nextFloat(0.0f, 1.0f);
        float u2 = nextFloat(0.0f, 2.0f * SampleMath.PI);
        float u3 = nextFloat(0.0f, 2.0f * SampleMath.PI);
        float sqrt1MinusU1 = (float)Math.sqrt(1.0f - u1);
        float sqrtU1 = (float)Math.sqrt(u1);
        long cosSin2 = SampleMath.computeCosSin(u2);
        long cosSin3 = SampleMath.computeCosSin(u3);
        return new B3Vec3(
                sqrt1MinusU1 * SampleMath.sine(cosSin2),
                sqrt1MinusU1 * SampleMath.cosine(cosSin2),
                sqrtU1 * SampleMath.sine(cosSin3));
    }
}
