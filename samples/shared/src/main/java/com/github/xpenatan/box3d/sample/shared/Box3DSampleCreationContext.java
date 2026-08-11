package com.github.xpenatan.box3d.sample.shared;

/**
 * Supplies immutable world-construction settings while a sample factory is running.
 * Box3D worker scheduling is selected when the world is created and cannot be
 * retrofitted correctly after construction.
 */
public final class Box3DSampleCreationContext {
    private static Box3DSampleSettings activeSettings;

    private Box3DSampleCreationContext() {
    }

    static Box3DSample create(Box3DSampleFactory factory, Box3DSampleSettings settings) {
        if(activeSettings != null) {
            throw new IllegalStateException("Nested Box3D sample creation is not supported");
        }
        activeSettings = settings;
        try {
            return factory.create();
        }
        finally {
            activeSettings = null;
        }
    }

    public static int workerCount() {
        if(activeSettings == null) {
            throw new IllegalStateException("A Box3D sample world must be created by Box3DSampleController");
        }
        return activeSettings.workerCount();
    }
}
