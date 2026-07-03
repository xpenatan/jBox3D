package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;

final class CardHouseThickSample extends AbstractBox3DSample {
    CardHouseThickSample() {
        addGroundBox(10.0f);

        float alpha = radians(25.0f);
        float width = 0.38f;
        float height = 0.98f;
        float depth = 0.08f;
        float offsetX = 0.5f * height * (float)Math.sin(alpha) + 0.045f;
        float offsetY = 0.5f * height * (float)Math.cos(alpha) + 0.035f;

        B3Hull card = B3Hull.CreateBox(0.5f * depth, 0.5f * height, 0.5f * width);
        addVerticalRow(card, 4, -6.0f * offsetX, offsetX, offsetY, alpha);
        addHorizontalRow(card, 3, -4.0f * offsetX, 4.0f * offsetX, 2.0f * offsetY + 0.04f);
        addVerticalRow(card, 3, -4.0f * offsetX, offsetX, 3.0f * offsetY + 0.08f, alpha);
        addHorizontalRow(card, 2, -2.0f * offsetX, 4.0f * offsetX, 4.0f * offsetY + 0.12f);
        addVerticalRow(card, 2, -2.0f * offsetX, offsetX, 5.0f * offsetY + 0.16f, alpha);
        addHorizontalRow(card, 1, 0.0f, 4.0f * offsetX, 6.0f * offsetY + 0.20f);
        addVerticalRow(card, 1, 0.0f, offsetX, 7.0f * offsetY + 0.24f, alpha);
        dispose(card);
    }

    private void addVerticalRow(B3Hull card, int count, float startX, float offsetX, float startY, float alpha) {
        B3Quat left = rotationZ(-alpha);
        B3Quat right = rotationZ(alpha);
        for(int i = 0; i < count; i++) {
            addHull(card, B3.DynamicBody(), startX - offsetX, startY, 0.0f, left, 1.0f, 0.8f, 0.0f, 0.0f);
            addHull(card, B3.DynamicBody(), startX + offsetX, startY, 0.0f, right, 1.0f, 0.8f, 0.0f, 0.0f);
            startX += 4.0f * offsetX;
        }
        dispose(right, left);
    }

    private void addHorizontalRow(B3Hull card, int count, float startX, float offsetX, float startY) {
        B3Quat rotation = rotationZ(0.5f * (float)Math.PI);
        for(int i = 0; i < count; i++) {
            addHull(card, B3.DynamicBody(), startX + i * offsetX, startY, 0.0f, rotation, 1.0f, 0.8f, 0.0f, 0.0f);
        }
        dispose(rotation);
    }

    private static float radians(float degrees) {
        return degrees * (float)Math.PI / 180.0f;
    }
}
