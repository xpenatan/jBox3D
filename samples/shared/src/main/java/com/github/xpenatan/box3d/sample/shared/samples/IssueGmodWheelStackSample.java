package com.github.xpenatan.box3d.sample.shared.samples;

import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3BodyDef;
import com.github.xpenatan.box3d.B3Hull;
import com.github.xpenatan.box3d.B3ShapeDef;
import com.github.xpenatan.box3d.B3SurfaceMaterial;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3Vec3Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Exact physical scene from Issues/GMod Wheel Stack. */
final class IssueGmodWheelStackSample extends AbstractBox3DSample {
    private static final int SOURCE_VERTEX_COUNT = 317;
    private static final int HULL_COUNT = 37;
    private static final int WHEEL_COUNT = 30;
    private static final int MAX_WRAPPING_POINTS = 512;
    private static final Pattern VERTEX_PATTERN = Pattern.compile(
            "\\{\\s*([-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][-+]?\\d+)?)f\\s*,\\s*"
                    + "([-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][-+]?\\d+)?)f\\s*,\\s*"
                    + "([-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][-+]?\\d+)?)f\\s*\\}");

    private final B3Hull[] sourceHulls = new B3Hull[HULL_COUNT];

    IssueGmodWheelStackSample() {
        dispose(addGroundBox(10.0f));

        float[][] sourceVertices = loadSourceVertices();
        List<float[]> wrappingPoints = new ArrayList<float[]>(MAX_WRAPPING_POINTS);
        for(int hullIndex = 0; hullIndex < HULL_COUNT; hullIndex++) {
            int offset = hullIndex == 0 ? 0 : 29 + (hullIndex - 1) * 8;
            int count = hullIndex == 0 ? 29 : 8;
            B3Vec3Array points = new B3Vec3Array(count);
            for(int pointIndex = 0; pointIndex < count; pointIndex++) {
                float[] coordinates = sourceVertices[offset + pointIndex];
                B3Vec3 point = new B3Vec3(coordinates[0], coordinates[1], coordinates[2]);
                points.SetValue(pointIndex, point);
                dispose(point);
            }

            B3Hull hull = B3Hull.CreateFromPoints(points, count);
            dispose(points);
            if(!hull.IsValid()) {
                dispose(hull);
                throw new IllegalStateException("Box3D rejected metal_wheel1 hull " + hullIndex);
            }
            sourceHulls[hullIndex] = hull;

            int vertexCount = hull.GetVertexCount();
            for(int pointIndex = 0; pointIndex < vertexCount && wrappingPoints.size() < MAX_WRAPPING_POINTS;
                    pointIndex++) {
                B3Vec3 point = hull.GetPoint(pointIndex);
                wrappingPoints.add(new float[] { point.GetX(), point.GetY(), point.GetZ() });
                dispose(point);
            }
        }

        B3Vec3Array wrappingArray = new B3Vec3Array(wrappingPoints.size());
        for(int pointIndex = 0; pointIndex < wrappingPoints.size(); pointIndex++) {
            float[] coordinates = wrappingPoints.get(pointIndex);
            B3Vec3 point = new B3Vec3(coordinates[0], coordinates[1], coordinates[2]);
            wrappingArray.SetValue(pointIndex, point);
            dispose(point);
        }
        B3Hull wheelHull = B3Hull.CreateFromPoints(wrappingArray, wrappingPoints.size());
        dispose(wrappingArray);
        if(!wheelHull.IsValid()) {
            dispose(wheelHull);
            throw new IllegalStateException("Box3D rejected the metal_wheel1 wrapping hull");
        }

        B3ShapeDef shapeDef = new B3ShapeDef();
        B3SurfaceMaterial material = shapeDef.GetBaseMaterial();
        material.SetFriction(0.6f);

        B3BodyDef bodyDef = new B3BodyDef();
        bodyDef.SetType(B3.DynamicBody());
        float height = 0.171f;
        float spacing = height + 0.006f;
        float startY = 0.5f * height + 0.004f;
        for(int wheelIndex = 0; wheelIndex < WHEEL_COUNT; wheelIndex++) {
            B3Vec3 position = new B3Vec3(0.0f, startY + wheelIndex * spacing, 0.0f);
            bodyDef.SetPosition(position);
            B3Body body = world().CreateBody(bodyDef);
            dispose(body.CreateHullShape(shapeDef, wheelHull), body, position);
        }
        dispose(bodyDef, material, shapeDef, wheelHull);

        world().SetContactTuning(240.0f, 10.0f, 3.0f);
    }

    @Override
    public void dispose() {
        super.dispose();
        dispose(sourceHulls);
    }

    private static float[][] loadSourceVertices() {
        String source = SampleAssets.readUtf8("samples/sample_issues.cpp");
        String declaration = "s_metalWheel1Verts[317]";
        int declarationIndex = source.indexOf(declaration);
        if(declarationIndex < 0) {
            throw new IllegalStateException("Pinned Box3D source does not contain " + declaration);
        }
        int arrayEnd = source.indexOf("};", declarationIndex);
        if(arrayEnd < 0) {
            throw new IllegalStateException("Pinned Box3D source has an unterminated " + declaration);
        }

        Matcher matcher = VERTEX_PATTERN.matcher(source.substring(declarationIndex, arrayEnd));
        float[][] vertices = new float[SOURCE_VERTEX_COUNT][3];
        int count = 0;
        while(matcher.find()) {
            if(count == SOURCE_VERTEX_COUNT) {
                throw new IllegalStateException(declaration + " contains more than 317 vertices");
            }
            vertices[count][0] = Float.parseFloat(matcher.group(1));
            vertices[count][1] = Float.parseFloat(matcher.group(2));
            vertices[count][2] = Float.parseFloat(matcher.group(3));
            count++;
        }
        if(count != SOURCE_VERTEX_COUNT) {
            throw new IllegalStateException(declaration + " contains " + count + " vertices, expected 317");
        }
        return vertices;
    }
}
