package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3ShapeDef;

final class EdgeCrossingSample extends AbstractBox3DSample {
    private static final float PI = (float)Math.PI;

    EdgeCrossingSample() {
        addGroundBox(40.0f);

        float hx = 0.2f;
        float hy = 0.02f;
        float hz = 0.04f;
        B3Hull box1 = B3Hull.CreateBox(hx, hy, hz);
        B3Hull box2 = B3Hull.CreateBox(hx, hz, hy);
        B3ShapeDef shapeDef = new B3ShapeDef();

        float inverseLength = 1.0f / (float)Math.sqrt(0.1f * 0.1f + 0.9f * 0.9f);
        float axisX = 0.1f * inverseLength;
        float axisY = 0.9f * inverseLength;
        createRow(-2.0f, hy, box1, box1, shapeDef, axisX, axisY);
        createRow(0.0f, hz, box2, box2, shapeDef, axisX, axisY);
        createRow(2.0f, hy, box1, box2, shapeDef, axisX, axisY);

        dispose(shapeDef, box2, box1);
    }

    private void createRow(float z, float bottomY, B3Hull bottomHull, B3Hull topHull, B3ShapeDef shapeDef,
            float axisX, float axisY) {
        float x = -10.0f;
        for(float angle = -PI; angle < PI + 0.001f; angle += 0.1f * PI) {
            B3Body bottom = createBody(B3.DynamicBody(), x, bottomY, z, null);
            dispose(bottom.CreateHullShape(shapeDef, bottomHull));

            float halfAngle = 0.5f * angle;
            float sine = (float)Math.sin(halfAngle);
            B3Quat rotation = new B3Quat(axisX * sine, axisY * sine, 0.0f, (float)Math.cos(halfAngle));
            B3Body top = createBody(B3.DynamicBody(), x, 20.0f * bottomY, z, rotation);
            dispose(top.CreateHullShape(shapeDef, topHull), rotation);
            x += 1.0f;
        }
    }
}
