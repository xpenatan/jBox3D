package com.github.xpenatan.box3d.sample.shared.samples;

/** Deterministic sample math matching Box3D's {@code b3ComputeCosSin}. */
final class SampleMath {
    static final float PI = 3.14159265359f;

    private SampleMath() {
    }

    static long computeCosSin(float radians) {
        float x = (float)Math.IEEEremainder(radians, 2.0f * PI);
        float pi2 = PI * PI;

        float cosine;
        if(x < -0.5f * PI) {
            float y = x + PI;
            float y2 = y * y;
            cosine = -(pi2 - 4.0f * y2) / (pi2 + y2);
        }
        else if(x > 0.5f * PI) {
            float y = x - PI;
            float y2 = y * y;
            cosine = -(pi2 - 4.0f * y2) / (pi2 + y2);
        }
        else {
            float y2 = x * x;
            cosine = (pi2 - 4.0f * y2) / (pi2 + y2);
        }

        float sine;
        if(x < 0.0f) {
            float y = x + PI;
            sine = -16.0f * y * (PI - y) / (5.0f * pi2 - 4.0f * y * (PI - y));
        }
        else {
            sine = 16.0f * x * (PI - x) / (5.0f * pi2 - 4.0f * x * (PI - x));
        }

        float magnitude = (float)Math.sqrt(sine * sine + cosine * cosine);
        float inverseMagnitude = magnitude > 0.0f ? 1.0f / magnitude : 0.0f;
        cosine *= inverseMagnitude;
        sine *= inverseMagnitude;
        return ((long)Float.floatToRawIntBits(cosine) << 32)
                | (Float.floatToRawIntBits(sine) & 0xFFFFFFFFL);
    }

    static float cosine(long cosSin) {
        return Float.intBitsToFloat((int)(cosSin >>> 32));
    }

    static float sine(long cosSin) {
        return Float.intBitsToFloat((int)cosSin);
    }
}
