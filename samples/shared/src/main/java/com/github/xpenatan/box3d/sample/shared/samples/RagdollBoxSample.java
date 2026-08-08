package com.github.xpenatan.box3d.sample.shared.samples;

/** Exact default scene from Ragdoll/Box. */
final class RagdollBoxSample extends AbstractBox3DSample {
    RagdollBoxSample() {
        dispose(addGroundBox(20.0f));
        ExactHuman.create(world(), 0.0f, 2.0f, 0.0f, 5.0f, 1.0f, 0.7f, 1, false);
    }
}
