package com.github.xpenatan.box3d.sample.shared;

public enum Box3DLaunchShape {
    SPHERE("Sphere"),
    BOX("Box"),
    CAPSULE("Capsule"),
    CYLINDER("Cylinder");

    private final String label;

    Box3DLaunchShape(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public static String[] labels() {
        Box3DLaunchShape[] shapes = values();
        String[] labels = new String[shapes.length];
        for(int i = 0; i < shapes.length; i++) {
            labels[i] = shapes[i].label();
        }
        return labels;
    }

    public static Box3DLaunchShape byIndex(int index) {
        Box3DLaunchShape[] shapes = values();
        if(index < 0) {
            return shapes[0];
        }
        if(index >= shapes.length) {
            return shapes[shapes.length - 1];
        }
        return shapes[index];
    }
}
