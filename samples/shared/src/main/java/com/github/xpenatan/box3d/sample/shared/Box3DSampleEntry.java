package com.github.xpenatan.box3d.sample.shared;

public final class Box3DSampleEntry {
    private final String category;
    private final String name;
    private final Box3DSampleFactory factory;
    private final Box3DSampleCamera camera;

    public Box3DSampleEntry(String category, String name, Box3DSampleFactory factory, Box3DSampleCamera camera) {
        if(category == null || category.length() == 0) {
            throw new IllegalArgumentException("category cannot be empty");
        }
        if(name == null || name.length() == 0) {
            throw new IllegalArgumentException("name cannot be empty");
        }
        if(factory == null) {
            throw new IllegalArgumentException("factory cannot be null");
        }
        this.category = category;
        this.name = name;
        this.factory = factory;
        this.camera = camera != null ? camera : new Box3DSampleCamera(7.5f, 5.5f, 9.0f, 0.0f, 1.6f, 0.0f);
    }

    public String category() {
        return category;
    }

    public String name() {
        return name;
    }

    public String displayName() {
        return category + " / " + name;
    }

    public Box3DSampleCamera camera() {
        return camera;
    }

    Box3DSample create() {
        return factory.create();
    }
}
