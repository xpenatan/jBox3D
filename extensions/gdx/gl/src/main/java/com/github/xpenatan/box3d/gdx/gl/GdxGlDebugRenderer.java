package com.github.xpenatan.box3d.gdx.gl;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.LongMap;
import com.github.xpenatan.box3d.B3;
import com.github.xpenatan.box3d.B3AABB;
import com.github.xpenatan.box3d.B3Body;
import com.github.xpenatan.box3d.B3Capsule;
import com.github.xpenatan.box3d.B3DebugDrawEm;
import com.github.xpenatan.box3d.B3DebugShape;
import com.github.xpenatan.box3d.B3Quat;
import com.github.xpenatan.box3d.B3Shape;
import com.github.xpenatan.box3d.B3Sphere;
import com.github.xpenatan.box3d.B3Transform;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3World;
import com.github.xpenatan.box3d.gdx.GdxBox3DConverter;
import com.github.xpenatan.jParser.api.NativeObject;

public class GdxGlDebugRenderer extends B3DebugDrawEm {
    public static final float DEFAULT_SHADOW_BIAS = 0.001f;

    private static final int CIRCLE_SEGMENTS = 24;
    private static final int SPHERE_SLICES = 24;
    private static final int SPHERE_STACKS = 12;
    private static final float TRANSFORM_AXIS_LENGTH = 0.35f;
    private static final long MODEL_ATTRIBUTES = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal;
    private static final int INITIAL_LINE_CAPACITY = 16384;
    private static final int LINE_DATA_STRIDE = 7;

    private final ModelBatch modelBatch;
    private final ShadowBiasShaderProvider modelShaderProvider;
    private final ModelBatch shadowBatch;
    private final ShapeRenderer lineRenderer;
    private final Environment environment;
    private final DirectionalShadowLight shadowLight;
    private final boolean ownsModelBatch;
    private final boolean ownsLineRenderer;
    private final LongMap<DebugModel> modelCache = new LongMap<DebugModel>();
    private final Array<ModelInstance> visibleInstances = new Array<ModelInstance>(false, 64);
    private final Array<ModelInstance> shadowCasterInstances = new Array<ModelInstance>(false, 64);
    private float[] lineData = new float[INITIAL_LINE_CAPACITY * LINE_DATA_STRIDE];
    private int[] lineColors = new int[INITIAL_LINE_CAPACITY];
    private final float[] transformedPoints = new float[24];
    private final Matrix4 worldTransform = new Matrix4();
    private final Matrix4 combinedTransform = new Matrix4();
    private final Vector3 tempScale = new Vector3(1.0f, 1.0f, 1.0f);
    private final Quaternion tempRotation = new Quaternion();
    private final Vector3 tempAxis = new Vector3();
    private final Vector3 tempMidpoint = new Vector3();
    private final Vector3 shadowCenter = new Vector3();
    private final Vector3 shadowDirection = new Vector3(-0.45f, -0.55f, -0.70f).nor();
    private final Color solidColor = new Color(0.58f, 0.60f, 0.62f, 1.0f);
    private boolean enabled = true;
    private boolean drawSolidShapes = true;
    private boolean drawWireframe = true;
    private boolean shadowsEnabled = true;
    private float shadowBias = DEFAULT_SHADOW_BIAS;
    private int lineCommandCount;

    public GdxGlDebugRenderer() {
        this(new ShadowBiasShaderProvider(), new ShapeRenderer(), true, true);
    }

    public GdxGlDebugRenderer(ModelBatch modelBatch) {
        this(modelBatch, null, new ShapeRenderer(), false, true);
    }

    public GdxGlDebugRenderer(ShapeRenderer lineRenderer) {
        this(new ShadowBiasShaderProvider(), lineRenderer, true, false);
    }

    public GdxGlDebugRenderer(ModelBatch modelBatch, ShapeRenderer lineRenderer) {
        this(modelBatch, null, lineRenderer, false, false);
    }

    private GdxGlDebugRenderer(ShadowBiasShaderProvider modelShaderProvider, ShapeRenderer lineRenderer,
            boolean ownsModelBatch, boolean ownsLineRenderer) {
        this(new ModelBatch(modelShaderProvider), modelShaderProvider, lineRenderer, ownsModelBatch, ownsLineRenderer);
    }

    private GdxGlDebugRenderer(ModelBatch modelBatch, ShadowBiasShaderProvider modelShaderProvider,
            ShapeRenderer lineRenderer, boolean ownsModelBatch, boolean ownsLineRenderer) {
        if(modelBatch == null) {
            throw new GdxRuntimeException("ModelBatch cannot be null");
        }
        if(lineRenderer == null) {
            throw new GdxRuntimeException("ShapeRenderer cannot be null");
        }
        this.modelBatch = modelBatch;
        this.modelShaderProvider = modelShaderProvider;
        this.shadowBatch = new ModelBatch(new DepthShaderProvider());
        this.lineRenderer = lineRenderer;
        this.ownsModelBatch = ownsModelBatch;
        this.ownsLineRenderer = ownsLineRenderer;
        this.environment = new Environment();
        this.shadowLight = new DirectionalShadowLight(2048, 2048, 90.0f, 90.0f, 0.5f, 300.0f);
        shadowLight.set(1.0f, 0.98f, 0.92f, shadowDirection.x, shadowDirection.y, shadowDirection.z);
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.18f, 0.19f, 0.21f, 1.0f));
        environment.add((DirectionalLight)shadowLight);
        environment.shadowMap = shadowLight;
        if(modelShaderProvider != null) {
            modelShaderProvider.setShadowBias(shadowBias);
        }
    }

    public void render(B3World world, Camera camera) {
        if(world == null) {
            throw new GdxRuntimeException("B3World cannot be null");
        }
        if(camera == null) {
            throw new GdxRuntimeException("Camera cannot be null");
        }
        lineCommandCount = 0;
        visibleInstances.clear();
        shadowCasterInstances.clear();
        if(!enabled) {
            return;
        }

        DrawWorld(world, B3.DefaultMaskBits());

        if(drawSolidShapes && visibleInstances.size > 0) {
            renderShadows(camera);
            modelBatch.begin(camera);
            for(int i = 0; i < visibleInstances.size; i++) {
                modelBatch.render(visibleInstances.get(i), environment);
            }
            modelBatch.end();
        }

        if(lineCommandCount > 0) {
            lineRenderer.setProjectionMatrix(camera.combined);
            lineRenderer.begin(ShapeRenderer.ShapeType.Line);
            for(int i = 0; i < lineCommandCount; i++) {
                int offset = i * LINE_DATA_STRIDE;
                int color = lineColors[i];
                lineRenderer.setColor(
                        ((color >>> 16) & 0xFF) / 255.0f,
                        ((color >>> 8) & 0xFF) / 255.0f,
                        (color & 0xFF) / 255.0f,
                        lineData[offset + 6]);
                lineRenderer.line(
                        lineData[offset], lineData[offset + 1], lineData[offset + 2],
                        lineData[offset + 3], lineData[offset + 4], lineData[offset + 5]);
            }
            lineRenderer.end();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDrawSolidShapes() {
        return drawSolidShapes;
    }

    public void setDrawSolidShapes(boolean drawSolidShapes) {
        this.drawSolidShapes = drawSolidShapes;
    }

    public boolean isDrawWireframe() {
        return drawWireframe;
    }

    public void setDrawWireframe(boolean drawWireframe) {
        this.drawWireframe = drawWireframe;
    }

    public boolean isShadowsEnabled() {
        return shadowsEnabled;
    }

    public void setShadowsEnabled(boolean shadowsEnabled) {
        this.shadowsEnabled = shadowsEnabled;
        environment.shadowMap = shadowsEnabled ? shadowLight : null;
    }

    public float getShadowBias() {
        return shadowBias;
    }

    public void setShadowBias(float shadowBias) {
        this.shadowBias = Math.max(0.0f, shadowBias);
        if(modelShaderProvider != null) {
            modelShaderProvider.setShadowBias(this.shadowBias);
        }
    }

    public void setSolidColor(float red, float green, float blue, float alpha) {
        solidColor.set(red, green, blue, alpha);
        clearShapeCache();
    }

    public void setDrawAllModes(boolean enabled) {
        SetDrawShapes(enabled);
        SetDrawJoints(enabled);
        SetDrawJointExtras(enabled);
        SetDrawBounds(enabled);
        SetDrawMass(enabled);
        SetDrawBodyNames(enabled);
        SetDrawContacts(enabled);
        SetDrawAnchorA(enabled);
        SetDrawGraphColors(enabled);
        SetDrawContactFeatures(enabled);
        SetDrawContactNormals(enabled);
        SetDrawContactForces(enabled);
        SetDrawFrictionForces(enabled);
        SetDrawIslands(enabled);
    }

    public void clearShapeCache() {
        for(DebugModel debugModel : modelCache.values()) {
            debugModel.dispose();
        }
        modelCache.clear();
    }

    @Override
    protected boolean DrawShape(B3DebugShape shape, B3Transform transform, int color) {
        if(!enabled || shape == null || transform == null) {
            return true;
        }

        if(drawSolidShapes) {
            DebugModel model = modelCache.get(shape.GetShapeId());
            if(model == null) {
                model = buildModel(shape);
                modelCache.put(shape.GetShapeId(), model);
            }
            appendSolidInstances(model, transform);
        }

        if(drawWireframe) {
            drawShapeWire(shape, transform, color);
        }
        return true;
    }

    @Override
    protected void DrawSegment(B3Vec3 p1, B3Vec3 p2, int color) {
        if(enabled) {
            line(p1, p2, color, 1.0f);
        }
    }

    @Override
    protected void DrawTransform(B3Transform transform) {
        if(!enabled || transform == null) {
            return;
        }
        transformPoint(transform, 0.0f, 0.0f, 0.0f, transformedPoints, 0);
        transformPoint(transform, TRANSFORM_AXIS_LENGTH, 0.0f, 0.0f, transformedPoints, 3);
        line(transformedPoints, 0, 3, 0xFF0000L, 1.0f);
        transformPoint(transform, 0.0f, TRANSFORM_AXIS_LENGTH, 0.0f, transformedPoints, 3);
        line(transformedPoints, 0, 3, 0x00FF00L, 1.0f);
        transformPoint(transform, 0.0f, 0.0f, TRANSFORM_AXIS_LENGTH, transformedPoints, 3);
        line(transformedPoints, 0, 3, 0x0000FFL, 1.0f);
    }

    @Override
    protected void DrawPoint(B3Vec3 p, float size, int color) {
        if(!enabled || p == null) {
            return;
        }
        float r = Math.max(0.02f, size * 0.01f);
        float x = p.GetX();
        float y = p.GetY();
        float z = p.GetZ();
        line(x - r, y, z, x + r, y, z, color, 1.0f);
        line(x, y - r, z, x, y + r, z, color, 1.0f);
        line(x, y, z - r, x, y, z + r, color, 1.0f);
    }

    @Override
    protected void DrawSphere(B3Vec3 p, float radius, int color, float alpha) {
        if(enabled && p != null) {
            drawWireSphere(p.GetX(), p.GetY(), p.GetZ(), radius, color, alpha);
        }
    }

    @Override
    protected void DrawCapsule(B3Vec3 p1, B3Vec3 p2, float radius, int color, float alpha) {
        if(enabled && p1 != null && p2 != null) {
            writePoint(p1, transformedPoints, 0);
            writePoint(p2, transformedPoints, 3);
            drawWireCapsule(transformedPoints, 0, 3, radius, color, alpha);
        }
    }

    @Override
    protected void DrawBounds(B3AABB aabb, int color) {
        if(!enabled || aabb == null) {
            return;
        }
        drawAABB(aabb.GetLowerBound(), aabb.GetUpperBound(), color);
    }

    @Override
    protected void DrawBox(B3Vec3 extents, B3Transform transform, int color) {
        if(!enabled || extents == null || transform == null) {
            return;
        }
        drawBox(extents.GetX(), extents.GetY(), extents.GetZ(), transform, color);
    }

    @Override
    protected void onNativeDispose() {
        clearShapeCache();
        visibleInstances.clear();
        shadowCasterInstances.clear();
        lineCommandCount = 0;
        if(ownsLineRenderer) {
            lineRenderer.dispose();
        }
        shadowBatch.dispose();
        shadowLight.dispose();
        if(ownsModelBatch) {
            modelBatch.dispose();
        }
    }

    private void renderShadows(Camera camera) {
        if(!shadowsEnabled) {
            environment.shadowMap = null;
            return;
        }
        environment.shadowMap = shadowLight;
        shadowCenter.set(camera.position).mulAdd(camera.direction, 42.0f);
        shadowLight.begin(shadowCenter, shadowDirection);
        boolean batchBegan = false;
        try {
            shadowBatch.begin(shadowLight.getCamera());
            batchBegan = true;
            for(int i = 0; i < shadowCasterInstances.size; i++) {
                shadowBatch.render(shadowCasterInstances.get(i));
            }
        }
        finally {
            if(batchBegan) {
                shadowBatch.end();
            }
            shadowLight.end();
        }
    }

    private DebugModel buildModel(B3DebugShape shape) {
        DebugModel debugModel = new DebugModel();
        int triangleCount = shape.GetTriangleCount();
        if(triangleCount > 0) {
            ModelBuilder builder = new ModelBuilder();
            builder.begin();
            MeshPartBuilder part = builder.part("box3d-debug-solid", GL20.GL_TRIANGLES, MODEL_ATTRIBUTES, solidMaterial());
            MeshPartBuilder.VertexInfo v0 = new MeshPartBuilder.VertexInfo();
            MeshPartBuilder.VertexInfo v1 = new MeshPartBuilder.VertexInfo();
            MeshPartBuilder.VertexInfo v2 = new MeshPartBuilder.VertexInfo();
            for(int i = 0; i < triangleCount; i++) {
                B3Vec3 p0 = shape.GetTriangleVertex0(i);
                B3Vec3 p1 = shape.GetTriangleVertex1(i);
                B3Vec3 p2 = shape.GetTriangleVertex2(i);
                B3Vec3 n = shape.GetTriangleNormal(i);
                v0.setPos(p0.GetX(), p0.GetY(), p0.GetZ()).setNor(n.GetX(), n.GetY(), n.GetZ());
                v1.setPos(p1.GetX(), p1.GetY(), p1.GetZ()).setNor(n.GetX(), n.GetY(), n.GetZ());
                v2.setPos(p2.GetX(), p2.GetY(), p2.GetZ()).setNor(n.GetX(), n.GetY(), n.GetZ());
                part.triangle(v0, v1, v2);
            }
            debugModel.meshModel = builder.end();
            debugModel.meshInstance = new ModelInstance(debugModel.meshModel);
        }

        ModelBuilder primitiveBuilder = new ModelBuilder();
        for(int i = 0; i < shape.GetSphereCount(); i++) {
            B3Sphere sphere = shape.GetSphereAt(i);
            float radius = sphere.GetRadius();
            if(radius <= 0.0f) {
                continue;
            }
            Model model = primitiveBuilder.createSphere(radius * 2.0f, radius * 2.0f, radius * 2.0f,
                    SPHERE_SLICES, SPHERE_STACKS, solidMaterial(), MODEL_ATTRIBUTES);
            PrimitiveInstance primitive = new PrimitiveInstance(model, new ModelInstance(model));
            B3Vec3 center = sphere.GetCenter();
            primitive.localTransform.setToTranslation(center.GetX(), center.GetY(), center.GetZ());
            debugModel.primitives.add(primitive);
        }

        for(int i = 0; i < shape.GetCapsuleCount(); i++) {
            B3Capsule capsule = shape.GetCapsuleAt(i);
            PrimitiveInstance primitive = buildCapsulePrimitive(primitiveBuilder, capsule);
            if(primitive != null) {
                debugModel.primitives.add(primitive);
            }
        }
        cacheShadowBody(debugModel, shape.GetShapeId());
        return debugModel;
    }

    private PrimitiveInstance buildCapsulePrimitive(ModelBuilder builder, B3Capsule capsule) {
        B3Vec3 c1 = capsule.GetCenter1();
        B3Vec3 c2 = capsule.GetCenter2();
        float radius = capsule.GetRadius();
        float dx = c2.GetX() - c1.GetX();
        float dy = c2.GetY() - c1.GetY();
        float dz = c2.GetZ() - c1.GetZ();
        float segmentLength = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
        if(radius <= 0.0f) {
            return null;
        }

        Model model = createCapsuleModel(radius, segmentLength, SPHERE_SLICES, Math.max(3, SPHERE_STACKS / 2));
        PrimitiveInstance primitive = new PrimitiveInstance(model, new ModelInstance(model));
        tempMidpoint.set(
                (c1.GetX() + c2.GetX()) * 0.5f,
                (c1.GetY() + c2.GetY()) * 0.5f,
                (c1.GetZ() + c2.GetZ()) * 0.5f);
        if(segmentLength > 0.00001f) {
            tempAxis.set(dx / segmentLength, dy / segmentLength, dz / segmentLength);
            tempRotation.setFromCross(Vector3.Y, tempAxis);
        }
        else {
            tempRotation.idt();
        }
        primitive.localTransform.set(tempMidpoint, tempRotation, tempScale);
        return primitive;
    }

    private Model createCapsuleModel(float radius, float segmentLength, int slices, int hemisphereStacks) {
        ModelBuilder builder = new ModelBuilder();
        builder.begin();
        MeshPartBuilder part = builder.part("box3d-debug-capsule", GL20.GL_TRIANGLES, MODEL_ATTRIBUTES,
                solidMaterial());
        Array<float[]> rings = new Array<float[]>(false, hemisphereStacks * 2 + 2);
        float half = segmentLength * 0.5f;
        for(int i = 0; i <= hemisphereStacks; i++) {
            float t = i / (float)hemisphereStacks;
            float theta = (float)(-Math.PI * 0.5 + Math.PI * 0.5 * t);
            rings.add(new float[] { (float)Math.cos(theta) * radius, -half + (float)Math.sin(theta) * radius });
        }
        rings.add(new float[] { radius, half });
        for(int i = 1; i <= hemisphereStacks; i++) {
            float t = i / (float)hemisphereStacks;
            float theta = (float)(Math.PI * 0.5 * t);
            rings.add(new float[] { (float)Math.cos(theta) * radius, half + (float)Math.sin(theta) * radius });
        }

        for(int ringIndex = 0; ringIndex + 1 < rings.size; ringIndex++) {
            float[] ring0 = rings.get(ringIndex);
            float[] ring1 = rings.get(ringIndex + 1);
            for(int slice = 0; slice < slices; slice++) {
                float u0 = slice / (float)slices;
                float u1 = (slice + 1) / (float)slices;
                float[] p00 = ringPoint(ring0[0], ring0[1], u0);
                float[] p01 = ringPoint(ring0[0], ring0[1], u1);
                float[] p10 = ringPoint(ring1[0], ring1[1], u0);
                float[] p11 = ringPoint(ring1[0], ring1[1], u1);
                triangle(part, p00, p10, p01);
                triangle(part, p01, p10, p11);
            }
        }
        return builder.end();
    }

    private void triangle(MeshPartBuilder part, float[] p0, float[] p1, float[] p2) {
        float ux = p1[0] - p0[0];
        float uy = p1[1] - p0[1];
        float uz = p1[2] - p0[2];
        float vx = p2[0] - p0[0];
        float vy = p2[1] - p0[1];
        float vz = p2[2] - p0[2];
        float nx = uy * vz - uz * vy;
        float ny = uz * vx - ux * vz;
        float nz = ux * vy - uy * vx;
        float length = (float)Math.sqrt(nx * nx + ny * ny + nz * nz);
        if(length > 0.000001f) {
            nx /= length;
            ny /= length;
            nz /= length;
        }
        else {
            nx = 0.0f;
            ny = 1.0f;
            nz = 0.0f;
        }
        MeshPartBuilder.VertexInfo v0 = new MeshPartBuilder.VertexInfo().setPos(p0[0], p0[1], p0[2]).setNor(nx, ny, nz);
        MeshPartBuilder.VertexInfo v1 = new MeshPartBuilder.VertexInfo().setPos(p1[0], p1[1], p1[2]).setNor(nx, ny, nz);
        MeshPartBuilder.VertexInfo v2 = new MeshPartBuilder.VertexInfo().setPos(p2[0], p2[1], p2[2]).setNor(nx, ny, nz);
        part.triangle(v0, v1, v2);
    }

    private static float[] ringPoint(float ringRadius, float y, float u) {
        float phi = (float)(Math.PI * 2.0 * u);
        return new float[] { (float)Math.cos(phi) * ringRadius, y, (float)Math.sin(phi) * ringRadius };
    }

    private void appendSolidInstances(DebugModel model, B3Transform transform) {
        GdxBox3DConverter.toGdx(transform, worldTransform);
        boolean castsShadow = model.castsShadow();
        if(model.meshInstance != null) {
            model.meshInstance.transform.set(worldTransform);
            visibleInstances.add(model.meshInstance);
            if(castsShadow) {
                shadowCasterInstances.add(model.meshInstance);
            }
        }
        for(int i = 0; i < model.primitives.size; i++) {
            PrimitiveInstance primitive = model.primitives.get(i);
            combinedTransform.set(worldTransform).mul(primitive.localTransform);
            primitive.instance.transform.set(combinedTransform);
            visibleInstances.add(primitive.instance);
            if(castsShadow) {
                shadowCasterInstances.add(primitive.instance);
            }
        }
    }

    private static void cacheShadowBody(DebugModel model, long shapeId) {
        B3Shape shape = new B3Shape(shapeId);
        try {
            if(shape.IsValid()) {
                model.shadowBody = new B3Body(shape.GetBodyId());
            }
        }
        finally {
            disposeNative(shape);
        }
    }

    private void drawShapeWire(B3DebugShape shape, B3Transform transform, int color) {
        for(int i = 0; i < shape.GetSphereCount(); i++) {
            B3Sphere sphere = shape.GetSphereAt(i);
            transformPoint(transform, sphere.GetCenter(), transformedPoints, 0);
            drawWireSphere(transformedPoints[0], transformedPoints[1], transformedPoints[2],
                    sphere.GetRadius(), color, 1.0f);
        }
        for(int i = 0; i < shape.GetCapsuleCount(); i++) {
            B3Capsule capsule = shape.GetCapsuleAt(i);
            transformPoint(transform, capsule.GetCenter1(), transformedPoints, 0);
            transformPoint(transform, capsule.GetCenter2(), transformedPoints, 3);
            drawWireCapsule(transformedPoints, 0, 3, capsule.GetRadius(), color, 1.0f);
        }
        int edgeCount = shape.GetHullEdgeCount();
        for(int i = 0; i < edgeCount; i++) {
            transformPoint(transform, shape.GetHullEdgeVertex0(i), transformedPoints, 0);
            transformPoint(transform, shape.GetHullEdgeVertex1(i), transformedPoints, 3);
            line(transformedPoints, 0, 3, color, 1.0f);
        }
    }

    private Material solidMaterial() {
        return new Material(
                ColorAttribute.createDiffuse(solidColor),
                IntAttribute.createCullFace(GL20.GL_NONE));
    }

    private void drawAABB(B3Vec3 lower, B3Vec3 upper, long color) {
        float lx = lower.GetX();
        float ly = lower.GetY();
        float lz = lower.GetZ();
        float ux = upper.GetX();
        float uy = upper.GetY();
        float uz = upper.GetZ();
        line(lx, ly, lz, ux, ly, lz, color, 1.0f);
        line(ux, ly, lz, ux, ly, uz, color, 1.0f);
        line(ux, ly, uz, lx, ly, uz, color, 1.0f);
        line(lx, ly, uz, lx, ly, lz, color, 1.0f);
        line(lx, uy, lz, ux, uy, lz, color, 1.0f);
        line(ux, uy, lz, ux, uy, uz, color, 1.0f);
        line(ux, uy, uz, lx, uy, uz, color, 1.0f);
        line(lx, uy, uz, lx, uy, lz, color, 1.0f);
        line(lx, ly, lz, lx, uy, lz, color, 1.0f);
        line(ux, ly, lz, ux, uy, lz, color, 1.0f);
        line(ux, ly, uz, ux, uy, uz, color, 1.0f);
        line(lx, ly, uz, lx, uy, uz, color, 1.0f);
    }

    private void drawBox(float hx, float hy, float hz, B3Transform transform, long color) {
        transformPoint(transform, -hx, -hy, -hz, transformedPoints, 0);
        transformPoint(transform, hx, -hy, -hz, transformedPoints, 3);
        transformPoint(transform, hx, -hy, hz, transformedPoints, 6);
        transformPoint(transform, -hx, -hy, hz, transformedPoints, 9);
        transformPoint(transform, -hx, hy, -hz, transformedPoints, 12);
        transformPoint(transform, hx, hy, -hz, transformedPoints, 15);
        transformPoint(transform, hx, hy, hz, transformedPoints, 18);
        transformPoint(transform, -hx, hy, hz, transformedPoints, 21);
        edge(0, 1, color);
        edge(1, 2, color);
        edge(2, 3, color);
        edge(3, 0, color);
        edge(4, 5, color);
        edge(5, 6, color);
        edge(6, 7, color);
        edge(7, 4, color);
        edge(0, 4, color);
        edge(1, 5, color);
        edge(2, 6, color);
        edge(3, 7, color);
    }

    private void edge(int a, int b, long color) {
        line(transformedPoints, a * 3, b * 3, color, 1.0f);
    }

    private void drawWireSphere(float x, float y, float z, float radius, long color, float alpha) {
        if(radius <= 0.0f) {
            return;
        }
        for(int i = 0; i < CIRCLE_SEGMENTS; i++) {
            double a0 = Math.PI * 2.0 * i / CIRCLE_SEGMENTS;
            double a1 = Math.PI * 2.0 * (i + 1) / CIRCLE_SEGMENTS;
            float c0 = (float)Math.cos(a0) * radius;
            float s0 = (float)Math.sin(a0) * radius;
            float c1 = (float)Math.cos(a1) * radius;
            float s1 = (float)Math.sin(a1) * radius;
            line(x + c0, y + s0, z, x + c1, y + s1, z, color, alpha);
            line(x + c0, y, z + s0, x + c1, y, z + s1, color, alpha);
            line(x, y + c0, z + s0, x, y + c1, z + s1, color, alpha);
        }
    }

    private void drawWireCapsule(float[] points, int p1, int p2, float radius, long color, float alpha) {
        line(points, p1, p2, color, alpha);
        drawWireSphere(points[p1], points[p1 + 1], points[p1 + 2], radius, color, alpha);
        drawWireSphere(points[p2], points[p2 + 1], points[p2 + 2], radius, color, alpha);
    }

    private void line(B3Vec3 p1, B3Vec3 p2, long color, float alpha) {
        line(p1.GetX(), p1.GetY(), p1.GetZ(), p2.GetX(), p2.GetY(), p2.GetZ(), color, alpha);
    }

    private void line(float[] points, int p1, int p2, long color, float alpha) {
        line(points[p1], points[p1 + 1], points[p1 + 2],
                points[p2], points[p2 + 1], points[p2 + 2], color, alpha);
    }

    private void line(float x1, float y1, float z1, float x2, float y2, float z2, long color, float alpha) {
        ensureLineCapacity(lineCommandCount + 1);
        int offset = lineCommandCount * LINE_DATA_STRIDE;
        lineData[offset] = x1;
        lineData[offset + 1] = y1;
        lineData[offset + 2] = z1;
        lineData[offset + 3] = x2;
        lineData[offset + 4] = y2;
        lineData[offset + 5] = z2;
        lineData[offset + 6] = Math.max(0.0f, Math.min(1.0f, alpha));
        lineColors[lineCommandCount] = (int)(color & 0x00FFFFFFL);
        lineCommandCount++;
    }

    private void ensureLineCapacity(int requiredCapacity) {
        if(requiredCapacity <= lineColors.length) {
            return;
        }
        int newCapacity = Math.max(requiredCapacity, lineColors.length * 2);
        float[] newLineData = new float[newCapacity * LINE_DATA_STRIDE];
        int[] newLineColors = new int[newCapacity];
        System.arraycopy(lineData, 0, newLineData, 0, lineCommandCount * LINE_DATA_STRIDE);
        System.arraycopy(lineColors, 0, newLineColors, 0, lineCommandCount);
        lineData = newLineData;
        lineColors = newLineColors;
    }

    private static void transformPoint(B3Transform transform, B3Vec3 point, float[] out, int offset) {
        transformPoint(transform, point.GetX(), point.GetY(), point.GetZ(), out, offset);
    }

    private static void transformPoint(B3Transform transform, float x, float y, float z, float[] out, int offset) {
        B3Quat q = transform.GetQ();
        B3Vec3 qv = q.GetV();
        float qx = qv.GetX();
        float qy = qv.GetY();
        float qz = qv.GetZ();
        float qw = q.GetS();
        float tx = 2.0f * (qy * z - qz * y);
        float ty = 2.0f * (qz * x - qx * z);
        float tz = 2.0f * (qx * y - qy * x);
        float rx = x + qw * tx + qy * tz - qz * ty;
        float ry = y + qw * ty + qz * tx - qx * tz;
        float rz = z + qw * tz + qx * ty - qy * tx;
        B3Vec3 p = transform.GetP();
        out[offset] = p.GetX() + rx;
        out[offset + 1] = p.GetY() + ry;
        out[offset + 2] = p.GetZ() + rz;
    }

    private static void writePoint(B3Vec3 value, float[] out, int offset) {
        out[offset] = value.GetX();
        out[offset + 1] = value.GetY();
        out[offset + 2] = value.GetZ();
    }

    private static void disposeNative(NativeObject object) {
        if(object != null && object.native_hasOwnership() && !object.isDisposed()) {
            object.dispose();
        }
    }

    private static DefaultShader.Config createShadowBiasShaderConfig() {
        DefaultShader.Config config = new DefaultShader.Config();
        config.fragmentShader = createShadowBiasFragmentShader();
        return config;
    }

    private static String createShadowBiasFragmentShader() {
        String shader = DefaultShader.getDefaultFragmentShader();
        String uniformAnchor = "uniform float u_shadowPCFOffset;\n";
        if(!shader.contains(uniformAnchor)) {
            throw new GdxRuntimeException("libGDX default shader no longer exposes u_shadowPCFOffset");
        }
        if(!shader.contains("uniform float u_shadowBias;")) {
            shader = shader.replace(uniformAnchor, uniformAnchor + "uniform float u_shadowBias;\n");
        }
        String compare = "return step(v_shadowMapUv.z, dot(texture2D(u_shadowTexture, v_shadowMapUv.xy + offset), bitShifts));";
        String biasedCompare = "return step(v_shadowMapUv.z - u_shadowBias, dot(texture2D(u_shadowTexture, v_shadowMapUv.xy + offset), bitShifts));";
        if(!shader.contains(compare)) {
            throw new GdxRuntimeException("libGDX default shader shadow comparison is not recognized");
        }
        return shader.replace(compare, biasedCompare);
    }

    private static final class ShadowBiasShaderProvider extends DefaultShaderProvider {
        private float shadowBias = DEFAULT_SHADOW_BIAS;

        ShadowBiasShaderProvider() {
            super(createShadowBiasShaderConfig());
        }

        void setShadowBias(float shadowBias) {
            this.shadowBias = shadowBias;
        }

        @Override
        protected Shader createShader(Renderable renderable) {
            return new ShadowBiasShader(renderable, config, this);
        }
    }

    private static final class ShadowBiasShader extends DefaultShader {
        private final ShadowBiasShaderProvider provider;
        private final int u_shadowBias;

        ShadowBiasShader(Renderable renderable, Config config, ShadowBiasShaderProvider provider) {
            super(renderable, config);
            this.provider = provider;
            this.u_shadowBias = register(new Uniform("u_shadowBias"));
        }

        @Override
        public boolean canRender(Renderable renderable) {
            boolean renderableShadowMap = renderable.environment != null && renderable.environment.shadowMap != null;
            return renderableShadowMap == shadowMap && super.canRender(renderable);
        }

        @Override
        public void render(Renderable renderable, Attributes combinedAttributes) {
            if(has(u_shadowBias)) {
                set(u_shadowBias, provider.shadowBias);
            }
            super.render(renderable, combinedAttributes);
        }
    }

    private static final class DebugModel implements Disposable {
        Model meshModel;
        ModelInstance meshInstance;
        B3Body shadowBody;
        final Array<PrimitiveInstance> primitives = new Array<PrimitiveInstance>(false, 4);

        boolean castsShadow() {
            return shadowBody != null && shadowBody.IsValid() && shadowBody.GetType() != B3.StaticBody();
        }

        @Override
        public void dispose() {
            if(meshModel != null) {
                meshModel.dispose();
                meshModel = null;
                meshInstance = null;
            }
            for(int i = 0; i < primitives.size; i++) {
                primitives.get(i).model.dispose();
            }
            primitives.clear();
            disposeNative(shadowBody);
            shadowBody = null;
        }
    }

    private static final class PrimitiveInstance {
        final Model model;
        final ModelInstance instance;
        final Matrix4 localTransform = new Matrix4();

        PrimitiveInstance(Model model, ModelInstance instance) {
            this.model = model;
            this.instance = instance;
        }
    }

}
