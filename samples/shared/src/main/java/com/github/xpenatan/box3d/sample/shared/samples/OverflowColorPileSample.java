package com.github.xpenatan.box3d.sample.shared.samples;

/** Exact default scene from shared/overflow_color.c. */
final class OverflowColorPileSample extends AbstractBox3DSample {
    OverflowColorPileSample() {
        addGroundBox(20.0f);

        float hubHalfX = 0.5f;
        float hubHalfY = 2.5f;
        float hubHalfZ = 0.5f;
        addDynamicBox(0.0f, hubHalfY, 0.0f, hubHalfX, hubHalfY, hubHalfZ, null,
                50.0f, 0.6f, 0.0f, 0.0f);

        int ringCount = 5;
        int perRing = 5;
        float neighborHalf = 0.2f;
        float ringRadius = hubHalfX + neighborHalf - 0.03f;
        float ringSpacing = 0.5f;
        float baseY = neighborHalf + 0.05f;
        for(int ring = 0; ring < ringCount; ring++) {
            float y = baseY + ringSpacing * ring;
            float thetaOffset = (ring & 1) != 0 ? (float)Math.PI / perRing : 0.0f;
            for(int slot = 0; slot < perRing; slot++) {
                float theta = thetaOffset + 2.0f * (float)Math.PI * slot / perRing;
                addDynamicBox(ringRadius * (float)Math.cos(theta), y, ringRadius * (float)Math.sin(theta),
                        neighborHalf, neighborHalf, neighborHalf);
            }
        }
    }
}
