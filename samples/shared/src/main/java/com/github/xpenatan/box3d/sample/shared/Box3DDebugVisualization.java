package com.github.xpenatan.box3d.sample.shared;

import com.github.xpenatan.box3d.B3DebugDrawEm;

public enum Box3DDebugVisualization {
    ALL("All", true, true, true, true),
    SOLID("Solid", true, true, false, true),
    SOLID_WIRE("Solid + Wire", true, true, true, true),
    WIRE("Wire", true, false, true, false),
    JOINTS("Joints", true, false, true, false),
    BOUNDS("Bounds", true, false, true, false),
    MASS("Mass", true, false, true, false),
    CONTACTS("Contacts", true, false, true, false),
    CONTACT_NORMALS("Contact Normals", true, false, true, false),
    CONTACT_FORCES("Contact Forces", true, false, true, false),
    FRICTION_FORCES("Friction Forces", true, false, true, false),
    ISLANDS("Islands", true, false, true, false),
    OFF("Off", false, false, false, false);

    private static final Box3DDebugVisualization[] VALUES = values();
    private static final String[] LABELS = createLabels();

    private final String label;
    private final boolean rendererEnabled;
    private final boolean drawSolidShapes;
    private final boolean drawWireframe;
    private final boolean shadowsEnabled;

    Box3DDebugVisualization(String label, boolean rendererEnabled, boolean drawSolidShapes, boolean drawWireframe,
            boolean shadowsEnabled) {
        this.label = label;
        this.rendererEnabled = rendererEnabled;
        this.drawSolidShapes = drawSolidShapes;
        this.drawWireframe = drawWireframe;
        this.shadowsEnabled = shadowsEnabled;
    }

    public String label() {
        return label;
    }

    public boolean rendererEnabled() {
        return rendererEnabled;
    }

    public boolean drawSolidShapes() {
        return drawSolidShapes;
    }

    public boolean drawWireframe() {
        return drawWireframe;
    }

    public boolean shadowsEnabled() {
        return shadowsEnabled;
    }

    public int index() {
        return ordinal();
    }

    public void apply(B3DebugDrawEm renderer) {
        if(renderer == null) {
            return;
        }
        renderer.SetDrawShapes(drawsShapes());
        renderer.SetDrawJoints(this == JOINTS || this == ALL);
        renderer.SetDrawJointExtras(this == JOINTS || this == ALL);
        renderer.SetDrawBounds(this == BOUNDS || this == ALL);
        renderer.SetDrawMass(this == MASS || this == ALL);
        renderer.SetDrawBodyNames(this == MASS || this == ALL);
        renderer.SetDrawContacts(drawsContacts());
        renderer.SetDrawAnchorA(this == JOINTS || this == ALL);
        renderer.SetDrawGraphColors(this == ISLANDS || this == ALL);
        renderer.SetDrawContactFeatures(this == CONTACTS || this == ALL);
        renderer.SetDrawContactNormals(this == CONTACT_NORMALS || this == ALL);
        renderer.SetDrawContactForces(this == CONTACT_FORCES || this == ALL);
        renderer.SetDrawFrictionForces(this == FRICTION_FORCES || this == ALL);
        renderer.SetDrawIslands(this == ISLANDS || this == ALL);
    }

    public static Box3DDebugVisualization byIndex(int index) {
        if(index < 0 || index >= VALUES.length) {
            return ALL;
        }
        return VALUES[index];
    }

    public static String[] labels() {
        String[] copy = new String[LABELS.length];
        System.arraycopy(LABELS, 0, copy, 0, LABELS.length);
        return copy;
    }

    private boolean drawsShapes() {
        return rendererEnabled && this != OFF;
    }

    private boolean drawsContacts() {
        return this == CONTACTS || this == CONTACT_NORMALS || this == CONTACT_FORCES || this == FRICTION_FORCES
                || this == ALL;
    }

    private static String[] createLabels() {
        String[] labels = new String[VALUES.length];
        for(int i = 0; i < VALUES.length; i++) {
            labels[i] = VALUES[i].label();
        }
        return labels;
    }
}
