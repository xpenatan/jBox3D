package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Hull;

final class ArchSample extends AbstractBox3DSample {
    private static final float[][] INNER = {
            {16.0f, 0.0f},
            {14.93803712795643f, 5.133601056842984f},
            {13.79871746027416f, 10.24928069555078f},
            {12.56252963284711f, 15.34107019122473f},
            {11.20040987372525f, 20.39856541571217f},
            {9.66521217819836f, 25.40369899225096f},
            {7.87179930638133f, 30.3179337000085f},
            {5.635199558196225f, 35.03820717801641f},
            {2.405937953536585f, 39.09554102558315f}
    };
    private static final float[][] OUTER = {
            {24.0f, 0.0f},
            {22.33619528222415f, 6.02299846205841f},
            {20.54936888969905f, 12.00964361211476f},
            {18.60854610798073f, 17.9470321677465f},
            {16.46769273811807f, 23.81367936585418f},
            {14.05325025774858f, 29.57079353071012f},
            {11.23551045834022f, 35.13775818285372f},
            {7.752568160730571f, 40.30450679009583f},
            {3.016931552701656f, 44.28891593799322f}
    };
    private static final float SCALE = 0.25f;
    private static final float HALF_DEPTH = 0.5f;

    ArchSample() {
        addGroundBox(40.0f);

        for(int i = 0; i < 8; i++) {
            addVoussoir(leftVertices(i));
        }
        for(int i = 0; i < 8; i++) {
            addVoussoir(rightVertices(i));
        }
        addVoussoir(topVertices());

        float top = scaledY(OUTER[8]);
        for(int i = 0; i < 4; i++) {
            addDynamicBox(0.0f, 0.5f + top + i, 0.0f, 2.0f, 0.5f, HALF_DEPTH, null, 200.0f, 0.6f,
                    0.0f, 0.0f);
        }
    }

    private void addVoussoir(float[][] vertices) {
        B3Hull hull = createHull(vertices);
        addHull(hull, B3.DynamicBody(), 0.0f, 0.0f, 0.0f, null, 200.0f, 0.6f, 0.0f, 0.0f);
        dispose(hull);
    }

    private static float[][] leftVertices(int i) {
        return prism(
                scaledX(INNER[i]), scaledY(INNER[i]),
                scaledX(OUTER[i]), scaledY(OUTER[i]),
                scaledX(OUTER[i + 1]), scaledY(OUTER[i + 1]),
                scaledX(INNER[i + 1]), scaledY(INNER[i + 1]));
    }

    private static float[][] rightVertices(int i) {
        return prism(
                -scaledX(OUTER[i]), scaledY(OUTER[i]),
                -scaledX(INNER[i]), scaledY(INNER[i]),
                -scaledX(INNER[i + 1]), scaledY(INNER[i + 1]),
                -scaledX(OUTER[i + 1]), scaledY(OUTER[i + 1]));
    }

    private static float[][] topVertices() {
        return prism(
                scaledX(INNER[8]), scaledY(INNER[8]),
                scaledX(OUTER[8]), scaledY(OUTER[8]),
                -scaledX(OUTER[8]), scaledY(OUTER[8]),
                -scaledX(INNER[8]), scaledY(INNER[8]));
    }

    private static float[][] prism(float x0, float y0, float x1, float y1, float x2, float y2, float x3,
            float y3) {
        return new float[][] {
                {x0, y0, -HALF_DEPTH}, {x1, y1, -HALF_DEPTH},
                {x2, y2, -HALF_DEPTH}, {x3, y3, -HALF_DEPTH},
                {x0, y0, HALF_DEPTH}, {x1, y1, HALF_DEPTH},
                {x2, y2, HALF_DEPTH}, {x3, y3, HALF_DEPTH}
        };
    }

    private static float scaledX(float[] point) {
        return SCALE * point[0];
    }

    private static float scaledY(float[] point) {
        return SCALE * point[1];
    }
}
