package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3Quat;

final class CardHouseSample extends AbstractBox3DSample {
    CardHouseSample() {
        addGroundBox(10.0f);

        float cardHeight = 0.2f;
        float cardThickness = 0.01f;
        float cardDepth = 0.1f;
        float angle0 = radians(25.0f);
        float angle1 = radians(-25.0f);
        float angle2 = 0.5f * (float)Math.PI;

        B3Hull card = B3Hull.CreateBox(cardThickness, cardHeight, cardDepth);
        B3Quat leanRight = rotationZ(angle0);
        B3Quat leanLeft = rotationZ(angle1);
        B3Quat flat = rotationZ(angle2);

        int rows = 5;
        float z0 = 0.0f;
        float y = cardHeight - 0.02f;
        while(rows > 0) {
            float x = z0;
            for(int i = 0; i < rows; i++) {
                if(i != rows - 1) {
                    addHull(card, B3.DynamicBody(), x + 0.25f, y + cardHeight - 0.015f, 0.0f, flat, 1.0f, 0.7f,
                            0.0f, 0.0f);
                }

                addHull(card, B3.DynamicBody(), x, y, 0.0f, leanLeft, 1.0f, 0.7f, 0.0f, 0.0f);
                x += 0.175f;
                addHull(card, B3.DynamicBody(), x, y, 0.0f, leanRight, 1.0f, 0.7f, 0.0f, 0.0f);
                x += 0.175f;
            }
            y += cardHeight * 2.0f - 0.03f;
            z0 += 0.175f;
            rows--;
        }

        dispose(flat, leanLeft, leanRight, card);
    }

    private static float radians(float degrees) {
        return degrees * (float)Math.PI / 180.0f;
    }
}
