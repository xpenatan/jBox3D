package com.github.xpenatan.box3d.sample.shared.samples;

/** Exact default scene from Issues/Restitution Overshoot. */
final class IssueRestitutionOvershootSample extends AbstractBox3DSample {
    IssueRestitutionOvershootSample() {
        addStaticBox(0.0f, -0.25f, 0.0f, 0.375f, 0.25f, 0.375f, null);
        addDynamicBox(0.0f, 10.0f, 0.0f, 0.5f, 0.5f, 0.5f, null,
                1.0f, 0.6f, 1.0f, 0.0f);
    }
}
