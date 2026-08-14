package com.github.xpenatan.box3d.gdx.gl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
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
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DepthShader;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.InstanceBufferObjectSubData;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.utils.ObjectMap;
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
import java.util.Arrays;
import java.util.Iterator;

public class GdxGlDebugRenderer extends B3DebugDrawEm {
    public static final float DEFAULT_SHADOW_BIAS = 0.001f;

    private static final int CIRCLE_SEGMENTS = 24;
    private static final int SPHERE_SLICES = 24;
    private static final int SPHERE_STACKS = 12;
    private static final float TRANSFORM_AXIS_LENGTH = 0.35f;
    private static final long MODEL_ATTRIBUTES = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal;
    private static final int INITIAL_LINE_CAPACITY = 16384;
    private static final int LINE_VERTEX_STRIDE = 4;
    private static final int LINE_DATA_STRIDE = LINE_VERTEX_STRIDE * 2;
    private static final int MAX_TRIANGLES_PER_MESH = 10000;
    private static final float DEFAULT_DRAW_DISTANCE = 100.0f;
    private static final int INSTANCE_DATA_STRIDE = 21;
    private static final int INITIAL_INSTANCE_CAPACITY = 64;
    private static final String INSTANCE_TRANSFORM = "a_instanceTransform";
    private static final String INSTANCE_COLOR = "a_instanceColor";
    private static final String INSTANCE_LINE_COLOR = "a_instanceLineColor";
    private static final float GRID_HALF_SIZE = 100.0f;
    private static final float GRID_SPACING = 5.0f;
    private static final int GRID_MAJOR_INTERVAL = 5;

    private final ModelBatch modelBatch;
    private final ShadowBiasShaderProvider modelShaderProvider;
    private final ModelBatch shadowBatch;
    private final ShaderProgram lineShader;
    private final ShaderProgram instancedLineShader;
    private final Environment environment;
    private final DirectionalShadowLight shadowLight;
    private final LongMap<ShapeProperties> shapeCache = new LongMap<ShapeProperties>();
    private final LongMap<SharedGeometry> geometryIdCache = new LongMap<SharedGeometry>();
    private final ObjectMap<GeometryDescriptor, SharedGeometry> geometryCache =
            new ObjectMap<GeometryDescriptor, SharedGeometry>();
    private final Array<SharedGeometry> sharedGeometries = new Array<SharedGeometry>(false, 16);
    private float[] lineData = new float[INITIAL_LINE_CAPACITY * LINE_DATA_STRIDE];
    private Mesh lineMesh;
    private int lineMeshVertexCapacity;
    private final float[] transformedPoints = new float[24];
    private final Matrix4 worldTransform = new Matrix4();
    private final Matrix4 combinedTransform = new Matrix4();
    private final Vector3 tempScale = new Vector3(1.0f, 1.0f, 1.0f);
    private final Quaternion tempRotation = new Quaternion();
    private final Vector3 tempAxis = new Vector3();
    private final Vector3 tempMidpoint = new Vector3();
    private final Vector3 shadowCenter = new Vector3();
    private final Vector3 shadowDirection = new Vector3(-0.45f, -0.55f, -0.70f).nor();
    private final Vector3 drawOrigin = new Vector3();
    private final B3Vec3 drawingLowerBound = new B3Vec3();
    private final B3Vec3 drawingUpperBound = new B3Vec3();
    private final B3AABB drawingBounds = new B3AABB();
    private final Color solidColor = new Color(Color.WHITE);
    private boolean enabled = true;
    private boolean drawSolidShapes = true;
    private boolean drawWireframe = true;
    private boolean shadowsEnabled = true;
    private float shadowBias = DEFAULT_SHADOW_BIAS;
    private float drawDistance = DEFAULT_DRAW_DISTANCE;
    private int lineCommandCount;
    private int visibleInstanceCount;
    private int shadowInstanceCount;
    private int wireInstanceCount;
    private int solidDrawCallCount;
    private int shadowDrawCallCount;
    private int lineDrawCallCount;
    private long renderedWorldId = Long.MIN_VALUE;

    public GdxGlDebugRenderer() {
        if(Gdx.gl30 == null) {
            throw new GdxRuntimeException("GdxGlDebugRenderer requires an OpenGL 3.0 context for instanced rendering");
        }
        this.modelShaderProvider = new ShadowBiasShaderProvider();
        this.modelBatch = new ModelBatch(modelShaderProvider);
        this.shadowBatch = new ModelBatch(new DepthShaderProvider(createInstancedDepthShaderConfig()));
        this.lineShader = createLineShader();
        this.instancedLineShader = createInstancedLineShader();
        this.environment = new Environment();
        this.shadowLight = new DirectionalShadowLight(2048, 2048, 90.0f, 90.0f, 0.5f, 300.0f);
        shadowLight.set(1.0f, 0.98f, 0.92f, shadowDirection.x, shadowDirection.y, shadowDirection.z);
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.24f, 0.25f, 0.28f, 1.0f));
        environment.add((DirectionalLight)shadowLight);
        environment.shadowMap = shadowLight;
        modelShaderProvider.setShadowBias(shadowBias);
    }

    public void render(B3World world, Camera camera) {
        if(world == null) {
            throw new GdxRuntimeException("B3World cannot be null");
        }
        if(camera == null) {
            throw new GdxRuntimeException("Camera cannot be null");
        }
        long worldId = world.GetId();
        if(worldId != renderedWorldId) {
            // Native shape and geometry handles are scoped to a world and may be reused after
            // that world is destroyed. Drop both the handle maps and their GPU resources so a
            // newly selected sample can never inherit geometry from the previous world.
            clearShapeCache();
            renderedWorldId = worldId;
        }
        lineCommandCount = 0;
        visibleInstanceCount = 0;
        shadowInstanceCount = 0;
        wireInstanceCount = 0;
        solidDrawCallCount = 0;
        shadowDrawCallCount = 0;
        lineDrawCallCount = 0;
        for(int i = 0; i < sharedGeometries.size; i++) {
            sharedGeometries.get(i).beginFrame();
        }
        if(!enabled) {
            return;
        }

        // Match the Box3D sample renderer: cull in world space around the eye, then submit
        // float geometry in an eye-relative frame so far-world scenes retain render precision.
        drawOrigin.set(camera.position);
        updateDrawingBounds(camera);
        camera.position.setZero();
        camera.update();
        try {
            DrawWorld(world, B3.DefaultMaskBits());
            drawReferenceGrid();

            if(drawSolidShapes && visibleInstanceCount > 0) {
                renderShadows(camera);
                modelBatch.begin(camera);
                for(int geometryIndex = 0; geometryIndex < sharedGeometries.size; geometryIndex++) {
                    SharedGeometry geometry = sharedGeometries.get(geometryIndex);
                    for(int i = 0; i < geometry.models.size; i++) {
                        InstancedModel model = geometry.models.get(i);
                        if(model.uploadVisibleInstances()) {
                            modelBatch.render(model.instance, environment);
                            solidDrawCallCount++;
                        }
                    }
                }
                modelBatch.end();
            }

            if(wireInstanceCount > 0 || lineCommandCount > 0) {
                // ModelBatch resets its RenderContext after the solid pass, including disabling
                // depth testing. Restore it for debug lines so hidden edges stay behind solids.
                Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
                Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
                Gdx.gl.glDepthMask(false);
                try {
                    if(wireInstanceCount > 0) {
                        instancedLineShader.bind();
                        instancedLineShader.setUniformMatrix("u_projTrans", camera.combined);
                        for(int geometryIndex = 0; geometryIndex < sharedGeometries.size; geometryIndex++) {
                            SharedGeometry geometry = sharedGeometries.get(geometryIndex);
                            if(geometry.wireModel != null && geometry.wireModel.uploadInstances()) {
                                geometry.wireModel.render(instancedLineShader);
                                lineDrawCallCount++;
                            }
                        }
                    }
                    if(lineCommandCount > 0) {
                        int vertexCount = lineCommandCount * 2;
                        ensureLineMeshCapacity(vertexCount);
                        lineMesh.setVertices(lineData, 0, lineCommandCount * LINE_DATA_STRIDE);
                        lineShader.bind();
                        lineShader.setUniformMatrix("u_projTrans", camera.combined);
                        lineMesh.render(lineShader, GL20.GL_LINES, 0, vertexCount);
                        lineDrawCallCount++;
                    }
                }
                finally {
                    Gdx.gl.glDepthMask(true);
                }
            }
        }
        finally {
            camera.position.set(drawOrigin);
            camera.update();
        }
    }

    private void updateDrawingBounds(Camera camera) {
        // Keep the draw/cull distance independent of the projection far plane;
        // using camera.far here makes large compounds such as Village submit
        // their entire world instead of only nearby children. Sample hosts can
        // configure this distance for cameras whose initial radius exceeds the default.
        drawingLowerBound.Set(drawOrigin.x - drawDistance, drawOrigin.y - drawDistance,
                drawOrigin.z - drawDistance);
        drawingUpperBound.Set(drawOrigin.x + drawDistance, drawOrigin.y + drawDistance,
                drawOrigin.z + drawDistance);
        drawingBounds.SetLowerBound(drawingLowerBound);
        drawingBounds.SetUpperBound(drawingUpperBound);
        SetDrawingBounds(drawingBounds);
    }

    private void drawReferenceGrid() {
        float centerX = Math.round(drawOrigin.x / GRID_SPACING) * GRID_SPACING;
        float centerZ = Math.round(drawOrigin.z / GRID_SPACING) * GRID_SPACING;
        int lineCount = Math.round(GRID_HALF_SIZE / GRID_SPACING);
        for(int i = -lineCount; i <= lineCount; i++) {
            float offset = i * GRID_SPACING;
            long color = i % GRID_MAJOR_INTERVAL == 0 ? 0x52677AL : 0x3B4C5CL;
            line(centerX + offset, 0.01f, centerZ - GRID_HALF_SIZE,
                    centerX + offset, 0.01f, centerZ + GRID_HALF_SIZE, color, 1.0f);
            line(centerX - GRID_HALF_SIZE, 0.01f, centerZ + offset,
                    centerX + GRID_HALF_SIZE, 0.01f, centerZ + offset, color, 1.0f);
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
        modelShaderProvider.setShadowBias(this.shadowBias);
    }

    public float getDrawDistance() {
        return drawDistance;
    }

    public void setDrawDistance(float drawDistance) {
        if(!Float.isFinite(drawDistance) || drawDistance <= 0.0f) {
            throw new GdxRuntimeException("Debug draw distance must be finite and greater than zero");
        }
        this.drawDistance = drawDistance;
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
        SetDrawIslands(enabled);
    }

    public void clearShapeCache() {
        shapeCache.clear();
        geometryIdCache.clear();
        for(int i = 0; i < sharedGeometries.size; i++) {
            sharedGeometries.get(i).dispose();
        }
        sharedGeometries.clear();
        geometryCache.clear();
        visibleInstanceCount = 0;
        shadowInstanceCount = 0;
    }

    public int getCachedShapeCount() {
        return shapeCache.size;
    }

    public int getSharedGeometryCount() {
        return geometryCache.size;
    }

    public int getVisibleInstanceCount() {
        return visibleInstanceCount;
    }

    public int getShadowInstanceCount() {
        return shadowInstanceCount;
    }

    public int getWireInstanceCount() {
        return wireInstanceCount;
    }

    public int getSolidDrawCallCount() {
        return solidDrawCallCount;
    }

    public int getShadowDrawCallCount() {
        return shadowDrawCallCount;
    }

    public int getLineDrawCallCount() {
        return lineDrawCallCount;
    }

    @Override
    protected void DrawShape(B3DebugShape shape, B3Transform transform, int color) {
        if(!enabled || shape == null || transform == null) {
            return;
        }

        long shapeId = shape.GetShapeId();
        ShapeProperties properties = shapeCache.get(shapeId);
        if(properties == null) {
            properties = ShapeProperties.read(shapeId);
            shapeCache.put(shapeId, properties);
        }
        SharedGeometry geometry = getOrCreateGeometry(shape);
        appendShapeInstances(geometry, properties, shape, transform, color);
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
        lineCommandCount = 0;
        disposeNative(drawingBounds);
        disposeNative(drawingUpperBound);
        disposeNative(drawingLowerBound);
        if(lineMesh != null) {
            lineMesh.dispose();
            lineMesh = null;
        }
        lineShader.dispose();
        instancedLineShader.dispose();
        shadowBatch.dispose();
        shadowLight.dispose();
        modelBatch.dispose();
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
            for(int geometryIndex = 0; geometryIndex < sharedGeometries.size; geometryIndex++) {
                SharedGeometry geometry = sharedGeometries.get(geometryIndex);
                for(int i = 0; i < geometry.models.size; i++) {
                    InstancedModel model = geometry.models.get(i);
                    if(model.uploadShadowInstances()) {
                        shadowBatch.render(model.instance);
                        shadowDrawCallCount++;
                    }
                }
            }
        }
        finally {
            if(batchBegan) {
                shadowBatch.end();
            }
            shadowLight.end();
        }
    }

    private SharedGeometry getOrCreateGeometry(B3DebugShape shape) {
        long geometryId = shape.GetGeometryId();
        SharedGeometry geometry = geometryIdCache.get(geometryId);
        if(geometry != null) {
            return geometry;
        }

        int type = shape.GetType();
        if(type == B3.SphereShape()) {
            geometry = buildUnitSphereGeometry();
        }
        else if(type == B3.CapsuleShape()) {
            geometry = buildUnitCapsuleGeometry();
        }
        else {
            GeometryDescriptor descriptor = GeometryDescriptor.read(shape);
            geometry = geometryCache.get(descriptor);
            if(geometry == null) {
                geometry = buildGeometry(descriptor);
                geometryCache.put(descriptor, geometry);
                sharedGeometries.add(geometry);
            }
        }
        geometryIdCache.put(geometryId, geometry);
        return geometry;
    }

    private SharedGeometry buildUnitSphereGeometry() {
        GeometryDescriptor descriptor = GeometryDescriptor.unitSphere();
        SharedGeometry geometry = geometryCache.get(descriptor);
        if(geometry == null) {
            geometry = buildGeometry(descriptor);
            geometryCache.put(descriptor, geometry);
            sharedGeometries.add(geometry);
        }
        return geometry;
    }

    private SharedGeometry buildUnitCapsuleGeometry() {
        GeometryDescriptor descriptor = GeometryDescriptor.unitCapsule();
        SharedGeometry geometry = geometryCache.get(descriptor);
        if(geometry != null) {
            return geometry;
        }

        ModelBuilder builder = new ModelBuilder();
        geometry = new SharedGeometry();
        Model cylinder = builder.createCylinder(2.0f, 1.0f, 2.0f, SPHERE_SLICES,
                solidMaterial(), MODEL_ATTRIBUTES);
        geometry.models.add(new InstancedModel(cylinder, new Matrix4()));
        Model sphere = builder.createSphere(2.0f, 2.0f, 2.0f, SPHERE_SLICES, SPHERE_STACKS,
                solidMaterial(), MODEL_ATTRIBUTES);
        geometry.models.add(new InstancedModel(sphere, new Matrix4()));
        geometryCache.put(descriptor, geometry);
        sharedGeometries.add(geometry);
        return geometry;
    }

    private SharedGeometry buildGeometry(GeometryDescriptor descriptor) {
        SharedGeometry geometry = new SharedGeometry();
        int triangleCount = descriptor.triangles.length / GeometryDescriptor.TRIANGLE_STRIDE;
        for(int firstTriangle = 0; firstTriangle < triangleCount; firstTriangle += MAX_TRIANGLES_PER_MESH) {
            int endTriangle = Math.min(firstTriangle + MAX_TRIANGLES_PER_MESH, triangleCount);
            Model model = buildTriangleModel(descriptor.triangles, firstTriangle, endTriangle);
            geometry.models.add(new InstancedModel(model, new Matrix4()));
        }

        ModelBuilder primitiveBuilder = new ModelBuilder();
        for(int offset = 0; offset < descriptor.spheres.length; offset += GeometryDescriptor.SPHERE_STRIDE) {
            float radius = descriptor.spheres[offset + 3];
            if(radius <= 0.0f) {
                continue;
            }
            Model model = primitiveBuilder.createSphere(radius * 2.0f, radius * 2.0f, radius * 2.0f,
                    SPHERE_SLICES, SPHERE_STACKS, solidMaterial(), MODEL_ATTRIBUTES);
            Matrix4 localTransform = new Matrix4().setToTranslation(
                    descriptor.spheres[offset], descriptor.spheres[offset + 1], descriptor.spheres[offset + 2]);
            geometry.models.add(new InstancedModel(model, localTransform));
        }

        for(int offset = 0; offset < descriptor.capsules.length; offset += GeometryDescriptor.CAPSULE_STRIDE) {
            InstancedModel capsule = buildCapsuleModel(descriptor.capsules, offset);
            if(capsule != null) {
                geometry.models.add(capsule);
            }
        }
        float[] wireVertices = buildWireVertices(descriptor);
        if(wireVertices.length > 0) {
            InstancedModel sharedInstanceSource = descriptor.spheres.length == 0
                    && descriptor.capsules.length == 0 && geometry.models.size == 1
                    ? geometry.models.first() : null;
            geometry.wireModel = new InstancedWireModel(wireVertices, sharedInstanceSource);
        }
        return geometry;
    }

    private static float[] buildWireVertices(GeometryDescriptor descriptor) {
        int estimatedSize = descriptor.edges.length
                + descriptor.spheres.length / GeometryDescriptor.SPHERE_STRIDE * CIRCLE_SEGMENTS * 18
                + descriptor.capsules.length / GeometryDescriptor.CAPSULE_STRIDE * (6 + CIRCLE_SEGMENTS * 36);
        FloatArray vertices = new FloatArray(false, Math.max(estimatedSize, 16));
        vertices.addAll(descriptor.edges);
        for(int offset = 0; offset < descriptor.spheres.length; offset += GeometryDescriptor.SPHERE_STRIDE) {
            appendWireSphere(vertices,
                    descriptor.spheres[offset], descriptor.spheres[offset + 1], descriptor.spheres[offset + 2],
                    descriptor.spheres[offset + 3]);
        }
        for(int offset = 0; offset < descriptor.capsules.length; offset += GeometryDescriptor.CAPSULE_STRIDE) {
            float x1 = descriptor.capsules[offset];
            float y1 = descriptor.capsules[offset + 1];
            float z1 = descriptor.capsules[offset + 2];
            float x2 = descriptor.capsules[offset + 3];
            float y2 = descriptor.capsules[offset + 4];
            float z2 = descriptor.capsules[offset + 5];
            float radius = descriptor.capsules[offset + 6];
            appendLine(vertices, x1, y1, z1, x2, y2, z2);
            appendWireSphere(vertices, x1, y1, z1, radius);
            appendWireSphere(vertices, x2, y2, z2, radius);
        }
        return Arrays.copyOf(vertices.items, vertices.size);
    }

    private static void appendWireSphere(FloatArray vertices, float x, float y, float z, float radius) {
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
            appendLine(vertices, x + c0, y + s0, z, x + c1, y + s1, z);
            appendLine(vertices, x + c0, y, z + s0, x + c1, y, z + s1);
            appendLine(vertices, x, y + c0, z + s0, x, y + c1, z + s1);
        }
    }

    private static void appendLine(FloatArray vertices,
            float x1, float y1, float z1, float x2, float y2, float z2) {
        vertices.add(x1);
        vertices.add(y1);
        vertices.add(z1);
        vertices.add(x2);
        vertices.add(y2);
        vertices.add(z2);
    }

    private Model buildTriangleModel(float[] triangles, int firstTriangle, int endTriangle) {
        ModelBuilder builder = new ModelBuilder();
        builder.begin();
        MeshPartBuilder part = builder.part("box3d-debug-solid", GL20.GL_TRIANGLES, MODEL_ATTRIBUTES, solidMaterial());
        MeshPartBuilder.VertexInfo v0 = new MeshPartBuilder.VertexInfo();
        MeshPartBuilder.VertexInfo v1 = new MeshPartBuilder.VertexInfo();
        MeshPartBuilder.VertexInfo v2 = new MeshPartBuilder.VertexInfo();
        for(int i = firstTriangle; i < endTriangle; i++) {
            int offset = i * GeometryDescriptor.TRIANGLE_STRIDE;
            float nx = triangles[offset + 9];
            float ny = triangles[offset + 10];
            float nz = triangles[offset + 11];
            v0.setPos(triangles[offset], triangles[offset + 1], triangles[offset + 2]).setNor(nx, ny, nz);
            v1.setPos(triangles[offset + 3], triangles[offset + 4], triangles[offset + 5]).setNor(nx, ny, nz);
            v2.setPos(triangles[offset + 6], triangles[offset + 7], triangles[offset + 8]).setNor(nx, ny, nz);
            part.triangle(v0, v1, v2);
        }
        return builder.end();
    }

    private InstancedModel buildCapsuleModel(float[] capsules, int offset) {
        float c1x = capsules[offset];
        float c1y = capsules[offset + 1];
        float c1z = capsules[offset + 2];
        float c2x = capsules[offset + 3];
        float c2y = capsules[offset + 4];
        float c2z = capsules[offset + 5];
        float radius = capsules[offset + 6];
        float dx = c2x - c1x;
        float dy = c2y - c1y;
        float dz = c2z - c1z;
        float segmentLength = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
        if(radius <= 0.0f) {
            return null;
        }

        Model model = createCapsuleModel(radius, segmentLength, SPHERE_SLICES, Math.max(3, SPHERE_STACKS / 2));
        tempMidpoint.set(
                (c1x + c2x) * 0.5f,
                (c1y + c2y) * 0.5f,
                (c1z + c2z) * 0.5f);
        if(segmentLength > 0.00001f) {
            tempAxis.set(dx / segmentLength, dy / segmentLength, dz / segmentLength);
            tempRotation.setFromCross(Vector3.Y, tempAxis);
        }
        else {
            tempRotation.idt();
        }
        Matrix4 localTransform = new Matrix4().set(tempMidpoint, tempRotation, tempScale);
        return new InstancedModel(model, localTransform);
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

    private void appendShapeInstances(SharedGeometry geometry, ShapeProperties properties, B3DebugShape shape,
            B3Transform transform, int color) {
        GdxBox3DConverter.toGdx(transform, worldTransform);
        worldTransform.val[Matrix4.M03] -= drawOrigin.x;
        worldTransform.val[Matrix4.M13] -= drawOrigin.y;
        worldTransform.val[Matrix4.M23] -= drawOrigin.z;

        int type = shape.GetType();
        if(type == B3.SphereShape()) {
            appendSphereInstances(geometry, properties, shape.GetSphere(), color);
            return;
        }
        if(type == B3.CapsuleShape()) {
            appendCapsuleInstances(geometry, properties, shape.GetCapsule(), transform, color);
            return;
        }

        B3Vec3 scale = shape.GetScale();
        combinedTransform.set(worldTransform).scale(scale.GetX(), scale.GetY(), scale.GetZ());
        if(drawSolidShapes) {
            for(int i = 0; i < geometry.models.size; i++) {
                InstancedModel model = geometry.models.get(i);
                model.appendVisible(combinedTransform, color, solidColor, properties.bodyType);
                if(properties.castsShadow) {
                    model.appendShadow(combinedTransform, color, solidColor, properties.bodyType);
                }
            }
            visibleInstanceCount++;
            if(properties.castsShadow) {
                shadowInstanceCount++;
            }
        }
        if(drawWireframe && geometry.wireModel != null) {
            geometry.wireModel.append(combinedTransform, color, drawSolidShapes);
            wireInstanceCount++;
        }
    }

    private void appendSphereInstances(SharedGeometry geometry, ShapeProperties properties, B3Sphere sphere, int color) {
        B3Vec3 center = sphere.GetCenter();
        float radius = sphere.GetRadius();
        if(radius <= 0.0f) {
            return;
        }
        combinedTransform.set(worldTransform)
                .translate(center.GetX(), center.GetY(), center.GetZ())
                .scale(radius, radius, radius);
        if(drawSolidShapes && geometry.models.size > 0) {
            InstancedModel model = geometry.models.first();
            model.appendVisible(combinedTransform, color, solidColor, properties.bodyType);
            if(properties.castsShadow) {
                model.appendShadow(combinedTransform, color, solidColor, properties.bodyType);
                shadowInstanceCount++;
            }
            visibleInstanceCount++;
        }
        if(drawWireframe && geometry.wireModel != null) {
            geometry.wireModel.append(combinedTransform, color, drawSolidShapes);
            wireInstanceCount++;
        }
    }

    private void appendCapsuleInstances(SharedGeometry geometry, ShapeProperties properties, B3Capsule capsule,
            B3Transform transform, int color) {
        B3Vec3 center1 = capsule.GetCenter1();
        float x1 = center1.GetX();
        float y1 = center1.GetY();
        float z1 = center1.GetZ();
        B3Vec3 center2 = capsule.GetCenter2();
        float x2 = center2.GetX();
        float y2 = center2.GetY();
        float z2 = center2.GetZ();
        float radius = capsule.GetRadius();
        if(radius <= 0.0f) {
            return;
        }

        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        float length = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
        boolean castsShadow = properties.castsShadow;
        if(drawSolidShapes && geometry.models.size >= 2) {
            if(length > 0.00001f) {
                tempAxis.set(dx / length, dy / length, dz / length);
                tempRotation.setFromCross(Vector3.Y, tempAxis);
                combinedTransform.set(worldTransform)
                        .translate((x1 + x2) * 0.5f, (y1 + y2) * 0.5f, (z1 + z2) * 0.5f)
                        .rotate(tempRotation)
                        .scale(radius, length, radius);
                InstancedModel cylinder = geometry.models.get(0);
                cylinder.appendVisible(combinedTransform, color, solidColor, properties.bodyType);
                if(castsShadow) {
                    cylinder.appendShadow(combinedTransform, color, solidColor, properties.bodyType);
                }
            }

            InstancedModel sphere = geometry.models.get(1);
            combinedTransform.set(worldTransform).translate(x1, y1, z1).scale(radius, radius, radius);
            sphere.appendVisible(combinedTransform, color, solidColor, properties.bodyType);
            if(castsShadow) {
                sphere.appendShadow(combinedTransform, color, solidColor, properties.bodyType);
            }
            combinedTransform.set(worldTransform).translate(x2, y2, z2).scale(radius, radius, radius);
            sphere.appendVisible(combinedTransform, color, solidColor, properties.bodyType);
            if(castsShadow) {
                sphere.appendShadow(combinedTransform, color, solidColor, properties.bodyType);
                shadowInstanceCount++;
            }
            visibleInstanceCount++;
        }
        if(drawWireframe) {
            transformPoint(transform, x1, y1, z1, transformedPoints, 0);
            transformPoint(transform, x2, y2, z2, transformedPoints, 3);
            drawWireCapsule(transformedPoints, 0, 3, radius, color, 1.0f);
            wireInstanceCount++;
        }
    }

    private Material solidMaterial() {
        return new Material(
                ColorAttribute.createDiffuse(Color.WHITE),
                ColorAttribute.createSpecular(0.12f, 0.12f, 0.12f, 1.0f),
                FloatAttribute.createShininess(28.0f),
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
        int red = (int)((color >>> 16) & 0xFFL);
        int green = (int)((color >>> 8) & 0xFFL);
        int blue = (int)(color & 0xFFL);
        int alphaByte = Math.round(Math.max(0.0f, Math.min(1.0f, alpha)) * 255.0f);
        float packedColor = Color.toFloatBits(red, green, blue, alphaByte);
        lineData[offset] = x1 - drawOrigin.x;
        lineData[offset + 1] = y1 - drawOrigin.y;
        lineData[offset + 2] = z1 - drawOrigin.z;
        lineData[offset + 3] = packedColor;
        lineData[offset + 4] = x2 - drawOrigin.x;
        lineData[offset + 5] = y2 - drawOrigin.y;
        lineData[offset + 6] = z2 - drawOrigin.z;
        lineData[offset + 7] = packedColor;
        lineCommandCount++;
    }

    private void ensureLineCapacity(int requiredCapacity) {
        int currentCapacity = lineData.length / LINE_DATA_STRIDE;
        if(requiredCapacity <= currentCapacity) {
            return;
        }
        int newCapacity = Math.max(requiredCapacity, currentCapacity * 2);
        float[] newLineData = new float[newCapacity * LINE_DATA_STRIDE];
        System.arraycopy(lineData, 0, newLineData, 0, lineCommandCount * LINE_DATA_STRIDE);
        lineData = newLineData;
    }

    private void ensureLineMeshCapacity(int requiredVertexCapacity) {
        if(lineMesh != null && requiredVertexCapacity <= lineMeshVertexCapacity) {
            return;
        }
        int newCapacity = Math.max(INITIAL_LINE_CAPACITY * 2, lineMeshVertexCapacity);
        while(newCapacity < requiredVertexCapacity) {
            newCapacity *= 2;
        }
        if(lineMesh != null) {
            lineMesh.dispose();
        }
        lineMesh = new Mesh(false, newCapacity, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));
        lineMeshVertexCapacity = newCapacity;
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

    private static ShaderProgram createLineShader() {
        String vertexShader = "attribute vec3 a_position;\n"
                + "attribute vec4 a_color;\n"
                + "uniform mat4 u_projTrans;\n"
                + "varying vec4 v_color;\n"
                + "void main() {\n"
                + "    v_color = a_color;\n"
                + "    gl_Position = u_projTrans * vec4(a_position, 1.0);\n"
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n"
                + "precision mediump float;\n"
                + "#endif\n"
                + "varying vec4 v_color;\n"
                + "void main() {\n"
                + "    gl_FragColor = v_color;\n"
                + "}\n";
        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if(!shader.isCompiled()) {
            String log = shader.getLog();
            shader.dispose();
            throw new GdxRuntimeException("Unable to compile the batched debug-line shader:\n" + log);
        }
        return shader;
    }

    private static ShaderProgram createInstancedLineShader() {
        String vertexShader = "attribute vec3 a_position;\n"
                + "attribute mat4 a_instanceTransform;\n"
                + "attribute vec4 a_instanceLineColor;\n"
                + "uniform mat4 u_projTrans;\n"
                + "varying vec4 v_color;\n"
                + "void main() {\n"
                + "    v_color = a_instanceLineColor;\n"
                + "    gl_Position = u_projTrans * a_instanceTransform * vec4(a_position, 1.0);\n"
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n"
                + "precision mediump float;\n"
                + "#endif\n"
                + "varying vec4 v_color;\n"
                + "void main() {\n"
                + "    gl_FragColor = v_color;\n"
                + "}\n";
        ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if(!shader.isCompiled()) {
            String log = shader.getLog();
            shader.dispose();
            throw new GdxRuntimeException("Unable to compile the instanced debug-line shader:\n" + log);
        }
        return shader;
    }

    private static DefaultShader.Config createShadowBiasShaderConfig() {
        DefaultShader.Config config = new DefaultShader.Config();
        config.vertexShader = createInstancedDefaultVertexShader();
        config.fragmentShader = createShadowBiasFragmentShader();
        return config;
    }

    private static DepthShader.Config createInstancedDepthShaderConfig() {
        DepthShader.Config config = new DepthShader.Config();
        String shader = DepthShader.getDefaultVertexShader();
        String positionAnchor = "attribute vec3 a_position;\n";
        shader = replaceRequired(shader, positionAnchor, positionAnchor
                + "attribute mat4 " + INSTANCE_TRANSFORM + ";\n",
                "libGDX depth shader position attribute");
        shader = replaceRequired(shader,
                "vec4 pos = u_projViewWorldTrans * vec4(a_position, 1.0);",
                "vec4 pos = u_projViewWorldTrans * " + INSTANCE_TRANSFORM + " * vec4(a_position, 1.0);",
                "libGDX depth shader rigid transform");
        config.vertexShader = shader;
        return config;
    }

    private static String createInstancedDefaultVertexShader() {
        String shader = DefaultShader.getDefaultVertexShader();
        String positionAnchor = "attribute vec3 a_position;\n";
        shader = replaceRequired(shader, positionAnchor, positionAnchor
                + "attribute mat4 " + INSTANCE_TRANSFORM + ";\n"
                + "attribute vec4 " + INSTANCE_COLOR + ";\n"
                + "varying vec4 v_instanceColor;\n",
                "libGDX default shader position attribute");
        shader = replaceRequired(shader, "void main() {\n",
                "void main() {\n"
                        + "    v_instanceColor = " + INSTANCE_COLOR + ";\n"
                        + "    float box3dMaterial = floor(" + INSTANCE_COLOR + ".a + 0.5);\n"
                        + "    float box3dRoughness = 0.40;\n"
                        + "    if(box3dMaterial < 1.5) box3dRoughness = 0.85;\n"
                        + "    else if(box3dMaterial < 2.5) box3dRoughness = 0.65;\n"
                        + "    else if(box3dMaterial < 3.5) box3dRoughness = 0.95;\n"
                        + "    else if(box3dMaterial < 4.5) box3dRoughness = 0.30;\n"
                        + "    else if(box3dMaterial < 5.5) box3dRoughness = 0.35;\n"
                        + "    else if(box3dMaterial < 6.5) box3dRoughness = 0.70;\n"
                        + "    else if(box3dMaterial < 7.5) box3dRoughness = 0.55;\n"
                        + "    float box3dShininess = mix(96.0, 4.0, box3dRoughness);\n",
                "libGDX default shader main function");
        shader = replaceRequired(shader,
                "vec4 pos = u_worldTrans * vec4(a_position, 1.0);",
                "vec4 pos = " + INSTANCE_TRANSFORM + " * vec4(a_position, 1.0);",
                "libGDX default shader rigid transform");
        shader = replaceRequired(shader,
                "vec3 normal = normalize(u_normalMatrix * a_normal);",
                "vec3 box3dColumn0 = " + INSTANCE_TRANSFORM + "[0].xyz;\n"
                        + "vec3 box3dColumn1 = " + INSTANCE_TRANSFORM + "[1].xyz;\n"
                        + "vec3 box3dColumn2 = " + INSTANCE_TRANSFORM + "[2].xyz;\n"
                        + "vec3 box3dNormal = box3dColumn0 * (a_normal.x / max(dot(box3dColumn0, box3dColumn0), 1.0e-12))\n"
                        + "    + box3dColumn1 * (a_normal.y / max(dot(box3dColumn1, box3dColumn1), 1.0e-12))\n"
                        + "    + box3dColumn2 * (a_normal.z / max(dot(box3dColumn2, box3dColumn2), 1.0e-12));\n"
                        + "vec3 normal = normalize(u_normalMatrix * box3dNormal);",
                "libGDX default shader normal transform");
        shader = replaceRequired(shader,
                "pow(halfDotView, u_shininess)",
                "pow(halfDotView, box3dShininess)",
                "libGDX default shader shininess calculation");
        return shader;
    }

    private static String createShadowBiasFragmentShader() {
        String shader = DefaultShader.getDefaultFragmentShader();
        String specularAnchor = "#if defined(specularTextureFlag) || defined(specularColorFlag)";
        shader = replaceRequired(shader, specularAnchor,
                "varying vec4 v_instanceColor;\n\n" + specularAnchor,
                "libGDX default shader specular flags");
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
        shader = shader.replace(compare, biasedCompare);
        String mainAnchor = "void main() {\n";
        shader = replaceRequired(shader, mainAnchor,
                "vec3 box3dLinearToSrgb(vec3 value) {\n"
                        + "    vec3 linear = max(value, vec3(0.0));\n"
                        + "    vec3 lower = 12.92 * linear;\n"
                        + "    vec3 upper = 1.055 * pow(linear, vec3(1.0 / 2.4)) - 0.055;\n"
                        + "    return mix(lower, upper, step(vec3(0.0031308), linear));\n"
                        + "}\n\n"
                        + mainAnchor,
                "libGDX default fragment shader main function");
        String emissiveAnchor = "\t#if defined(emissiveTextureFlag) && defined(emissiveColorFlag)\n";
        shader = replaceRequired(shader, emissiveAnchor,
                "\tdiffuse *= vec4(v_instanceColor.rgb, 1.0);\n"
                        + "\tfloat box3dMaterial = floor(v_instanceColor.a + 0.5);\n"
                        + "\tfloat box3dMetallic = 0.85 * step(4.5, box3dMaterial) * "
                        + "(1.0 - step(5.5, box3dMaterial));\n"
                        + "\tvec3 box3dBaseColor = diffuse.rgb;\n\n"
                        + emissiveAnchor,
                "libGDX default shader emissive block");
        String specularOutputAnchor = "\t\t#else\n"
                + "\t\t\tvec3 specular = v_lightSpecular;\n"
                + "\t\t#endif\n\n"
                + "\t\t#if defined(ambientFlag) && defined(separateAmbientFlag)\n";
        shader = replaceRequired(shader, specularOutputAnchor,
                "\t\t#else\n"
                        + "\t\t\tvec3 specular = v_lightSpecular;\n"
                        + "\t\t#endif\n\n"
                        + "\t\tvec3 box3dMetalSpecular = mix(vec3(0.60), min(box3dBaseColor * 2.0, vec3(1.0)), 0.55);\n"
                        + "\t\tspecular *= mix(vec3(1.0), box3dMetalSpecular, box3dMetallic);\n\n"
                        + "\t\t#if defined(ambientFlag) && defined(separateAmbientFlag)\n",
                "libGDX default shader lit output block");
        String alphaAnchor = "\t#ifdef blendedFlag\n\t\tgl_FragColor.a = diffuse.a * v_opacity;";
        shader = replaceRequired(shader, alphaAnchor,
                "\tgl_FragColor.rgb = box3dLinearToSrgb(gl_FragColor.rgb);\n\n" + alphaAnchor,
                "libGDX default shader alpha output");
        return shader;
    }

    private static String replaceRequired(String value, String target, String replacement, String description) {
        if(!value.contains(target)) {
            throw new GdxRuntimeException(description + " is not recognized");
        }
        return value.replace(target, replacement);
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

    private static VertexAttribute[] createInstanceAttributes() {
        return new VertexAttribute[] {
                new VertexAttribute(VertexAttributes.Usage.Generic, 4, INSTANCE_TRANSFORM, 0),
                new VertexAttribute(VertexAttributes.Usage.Generic, 4, INSTANCE_TRANSFORM, 1),
                new VertexAttribute(VertexAttributes.Usage.Generic, 4, INSTANCE_TRANSFORM, 2),
                new VertexAttribute(VertexAttributes.Usage.Generic, 4, INSTANCE_TRANSFORM, 3),
                new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, INSTANCE_COLOR),
                new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, INSTANCE_LINE_COLOR)
        };
    }

    private static float srgbToLinear(float value) {
        return value <= 0.04045f ? value / 12.92f
                : (float)Math.pow((value + 0.055f) / 1.055f, 2.4f);
    }

    private static void appendInstanceData(FloatArray data, Matrix4 transform, int color, Color tint, int bodyType) {
        float[] matrix = transform.val;
        for(int i = 0; i < 16; i++) {
            data.add(matrix[i]);
        }
        data.add(srgbToLinear(((color >>> 16) & 0xFF) / 255.0f) * tint.r);
        data.add(srgbToLinear(((color >>> 8) & 0xFF) / 255.0f) * tint.g);
        data.add(srgbToLinear((color & 0xFF) / 255.0f) * tint.b);
        data.add(debugMaterialCode((color >>> 24) & 0xFF, bodyType));
        data.add(Color.toFloatBits((color >>> 16) & 0xFF, (color >>> 8) & 0xFF, color & 0xFF, 0xFF));
    }

    private static void appendWireOnlyInstanceData(FloatArray data, Matrix4 transform, int color) {
        float[] matrix = transform.val;
        for(int i = 0; i < 16; i++) {
            data.add(matrix[i]);
        }
        data.add(0.0f);
        data.add(0.0f);
        data.add(0.0f);
        data.add(0.0f);
        data.add(Color.toFloatBits((color >>> 16) & 0xFF, (color >>> 8) & 0xFF, color & 0xFF, 0xFF));
    }

    private static float debugMaterialCode(int preset, int bodyType) {
        if(preset >= 1 && preset <= 5) {
            return preset;
        }
        if(bodyType == B3.StaticBody()) {
            return 6.0f;
        }
        if(bodyType == B3.KinematicBody()) {
            return 7.0f;
        }
        return 8.0f;
    }

    private static final class GeometryDescriptor {
        static final int TRIANGLE_STRIDE = 12;
        static final int SPHERE_STRIDE = 4;
        static final int CAPSULE_STRIDE = 7;
        static final int EDGE_STRIDE = 6;

        final float[] triangles;
        final float[] spheres;
        final float[] capsules;
        final float[] edges;
        private final int hashCode;

        private GeometryDescriptor(float[] triangles, float[] spheres, float[] capsules, float[] edges) {
            this.triangles = triangles;
            this.spheres = spheres;
            this.capsules = capsules;
            this.edges = edges;
            int hash = Arrays.hashCode(triangles);
            hash = 31 * hash + Arrays.hashCode(spheres);
            hash = 31 * hash + Arrays.hashCode(capsules);
            hash = 31 * hash + Arrays.hashCode(edges);
            hashCode = hash;
        }

        static GeometryDescriptor unitSphere() {
            return new GeometryDescriptor(new float[0], new float[] { 0.0f, 0.0f, 0.0f, 1.0f },
                    new float[0], new float[0]);
        }

        static GeometryDescriptor unitCapsule() {
            // This descriptor is only a stable cache key. The renderer builds a shared
            // unit cylinder and unit sphere and composes exact capsule instances from them.
            return new GeometryDescriptor(new float[0], new float[0],
                    new float[] { 0.0f, -0.5f, 0.0f, 0.0f, 0.5f, 0.0f, 1.0f }, new float[0]);
        }

        static GeometryDescriptor read(B3DebugShape shape) {
            int triangleCount = shape.GetTriangleCount();
            float[] triangles = new float[triangleCount * TRIANGLE_STRIDE];
            for(int i = 0; i < triangleCount; i++) {
                int offset = i * TRIANGLE_STRIDE;
                write(shape.GetTriangleVertex0(i), triangles, offset);
                write(shape.GetTriangleVertex1(i), triangles, offset + 3);
                write(shape.GetTriangleVertex2(i), triangles, offset + 6);
                write(shape.GetTriangleNormal(i), triangles, offset + 9);
            }

            int sphereCount = shape.GetSphereCount();
            float[] spheres = new float[sphereCount * SPHERE_STRIDE];
            for(int i = 0; i < sphereCount; i++) {
                int offset = i * SPHERE_STRIDE;
                B3Sphere sphere = shape.GetSphereAt(i);
                write(sphere.GetCenter(), spheres, offset);
                spheres[offset + 3] = sphere.GetRadius();
            }

            int capsuleCount = shape.GetCapsuleCount();
            float[] capsules = new float[capsuleCount * CAPSULE_STRIDE];
            for(int i = 0; i < capsuleCount; i++) {
                int offset = i * CAPSULE_STRIDE;
                B3Capsule capsule = shape.GetCapsuleAt(i);
                write(capsule.GetCenter1(), capsules, offset);
                write(capsule.GetCenter2(), capsules, offset + 3);
                capsules[offset + 6] = capsule.GetRadius();
            }

            int edgeCount = shape.GetHullEdgeCount();
            float[] edges = new float[edgeCount * EDGE_STRIDE];
            for(int i = 0; i < edgeCount; i++) {
                int offset = i * EDGE_STRIDE;
                write(shape.GetHullEdgeVertex0(i), edges, offset);
                write(shape.GetHullEdgeVertex1(i), edges, offset + 3);
            }
            return new GeometryDescriptor(triangles, spheres, capsules, edges);
        }

        private static void write(B3Vec3 value, float[] output, int offset) {
            output[offset] = value.GetX();
            output[offset + 1] = value.GetY();
            output[offset + 2] = value.GetZ();
        }

        @Override
        public boolean equals(Object object) {
            if(this == object) {
                return true;
            }
            if(!(object instanceof GeometryDescriptor)) {
                return false;
            }
            GeometryDescriptor other = (GeometryDescriptor)object;
            return Arrays.equals(triangles, other.triangles)
                    && Arrays.equals(spheres, other.spheres)
                    && Arrays.equals(capsules, other.capsules)
                    && Arrays.equals(edges, other.edges);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }

    private static final class SharedGeometry implements Disposable {
        final Array<InstancedModel> models = new Array<InstancedModel>(false, 1);
        InstancedWireModel wireModel;

        void beginFrame() {
            for(int i = 0; i < models.size; i++) {
                models.get(i).beginFrame();
            }
            if(wireModel != null) {
                wireModel.beginFrame();
            }
        }

        @Override
        public void dispose() {
            for(int i = 0; i < models.size; i++) {
                models.get(i).dispose();
            }
            models.clear();
            if(wireModel != null) {
                wireModel.dispose();
                wireModel = null;
            }
        }
    }

    private static final class ShapeProperties {
        final boolean castsShadow;
        final int bodyType;

        private ShapeProperties(boolean castsShadow, int bodyType) {
            this.castsShadow = castsShadow;
            this.bodyType = bodyType;
        }

        static ShapeProperties read(long shapeId) {
            B3Shape shape = new B3Shape(shapeId);
            try {
                if(shape.IsValid()) {
                    B3Body body = new B3Body(shape.GetBodyId());
                    try {
                        if(body.IsValid()) {
                            int bodyType = body.GetType();
                            // Opaque static geometry participates in the Box3D sample's
                            // shadow pass too. Excluding it flattened scenes such as Village
                            // and removed building-to-building and terrain shadows.
                            return new ShapeProperties(true, bodyType);
                        }
                    }
                    finally {
                        disposeNative(body);
                    }
                }
            }
            finally {
                disposeNative(shape);
            }
            return new ShapeProperties(true, B3.StaticBody());
        }
    }

    private static final class ResizableInstancedMesh extends Mesh {
        private boolean ownsInstances = true;

        ResizableInstancedMesh(Mesh source) {
            super(false, source.getNumVertices(), source.getNumIndices(), source.getVertexAttributes());

            int vertexFloatCount = source.getNumVertices() * source.getVertexSize() / 4;
            float[] vertices = new float[vertexFloatCount];
            source.getVertices(vertices);
            setVertices(vertices);

            int indexCount = source.getNumIndices();
            if(indexCount > 0) {
                short[] indices = new short[indexCount];
                source.getIndices(indices);
                setIndices(indices);
            }
        }

        @Override
        public Mesh enableInstancedRendering(boolean isStatic, int maxInstances, VertexAttribute... attributes) {
            if(isInstanced) {
                throw new GdxRuntimeException("Instanced rendering is already enabled for this mesh");
            }
            isInstanced = true;
            ownsInstances = true;
            instances = new ResettingInstanceBufferObjectSubData(isStatic, maxInstances, attributes);
            return this;
        }

        void shareInstancesFrom(ResizableInstancedMesh source) {
            if(source.instances == null) {
                throw new GdxRuntimeException("Cannot share instance data from a non-instanced mesh");
            }
            isInstanced = true;
            ownsInstances = false;
            instances = source.instances;
        }

        @Override
        public Mesh disableInstancedRendering() {
            if(isInstanced && !ownsInstances) {
                isInstanced = false;
                ownsInstances = true;
                instances = null;
                return this;
            }
            return super.disableInstancedRendering();
        }

        @Override
        public void unbind(com.badlogic.gdx.graphics.glutils.ShaderProgram shader, int[] locations,
                int[] instanceLocations) {
            if(instances != null && instances.getNumInstances() > 0) {
                instances.unbind(shader, instanceLocations);
            }
            vertices.unbind(shader, locations);
            if(indices.getNumIndices() > 0) {
                indices.unbind();
            }
        }

        @Override
        public void dispose() {
            if(isInstanced && !ownsInstances) {
                isInstanced = false;
                ownsInstances = true;
                instances = null;
            }
            super.dispose();
        }
    }

    private static final class ResettingInstanceBufferObjectSubData extends InstanceBufferObjectSubData {
        private final VertexAttribute[] instanceAttributes;

        ResettingInstanceBufferObjectSubData(boolean isStatic, int maxInstances, VertexAttribute... attributes) {
            super(isStatic, maxInstances, attributes);
            instanceAttributes = attributes;
        }

        @Override
        public void unbind(com.badlogic.gdx.graphics.glutils.ShaderProgram shader, int[] locations) {
            super.unbind(shader, locations);
            for(int i = 0; i < instanceAttributes.length; i++) {
                VertexAttribute attribute = instanceAttributes[i];
                int location = locations == null ? shader.getAttributeLocation(attribute.alias) : locations[i];
                if(location >= 0) {
                    Gdx.gl30.glVertexAttribDivisor(location + attribute.unit, 0);
                }
            }
        }
    }

    private static final class InstancedWireModel implements Disposable {
        final ResizableInstancedMesh mesh;
        final FloatArray instanceData = new FloatArray(false, INITIAL_INSTANCE_CAPACITY * INSTANCE_DATA_STRIDE);
        final InstancedModel sharedInstanceSource;
        private int instanceCapacity = INITIAL_INSTANCE_CAPACITY;

        InstancedWireModel(float[] vertices, InstancedModel sharedInstanceSource) {
            Mesh source = new Mesh(true, vertices.length / 3, 0,
                    new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));
            source.setVertices(vertices);
            this.mesh = new ResizableInstancedMesh(source);
            this.sharedInstanceSource = sharedInstanceSource;
            source.dispose();
            if(sharedInstanceSource == null) {
                mesh.enableInstancedRendering(false, instanceCapacity, createInstanceAttributes());
            }
        }

        void beginFrame() {
            instanceData.clear();
        }

        void append(Matrix4 transform, int color, boolean solidInstanceAlreadyAppended) {
            if(sharedInstanceSource != null) {
                if(!solidInstanceAlreadyAppended) {
                    sharedInstanceSource.appendWireOnly(transform, color);
                }
                return;
            }
            appendWireOnlyInstanceData(instanceData, transform, color);
        }

        boolean uploadInstances() {
            if(sharedInstanceSource != null) {
                if(!sharedInstanceSource.uploadVisibleInstances()) {
                    return false;
                }
                mesh.shareInstancesFrom(sharedInstanceSource.primaryMesh());
                return true;
            }
            if(instanceData.size == 0) {
                return false;
            }
            int instanceCount = instanceData.size / INSTANCE_DATA_STRIDE;
            ensureCapacity(instanceCount);
            mesh.setInstanceData(instanceData.items, 0, instanceData.size);
            return true;
        }

        void render(ShaderProgram shader) {
            try {
                mesh.render(shader, GL20.GL_LINES, 0, mesh.getNumVertices());
            }
            finally {
                if(sharedInstanceSource != null) {
                    // Keep the shared VBO attached only for the draw. Otherwise libGDX context-loss
                    // recovery would invalidate the same instance buffer once through each mesh.
                    mesh.disableInstancedRendering();
                }
            }
        }

        private void ensureCapacity(int requiredCapacity) {
            if(requiredCapacity <= instanceCapacity) {
                return;
            }
            while(instanceCapacity < requiredCapacity) {
                instanceCapacity *= 2;
            }
            mesh.disableInstancedRendering();
            mesh.enableInstancedRendering(false, instanceCapacity, createInstanceAttributes());
        }

        @Override
        public void dispose() {
            mesh.dispose();
        }
    }

    private static final class InstancedModel implements Disposable {
        final Model model;
        final ModelInstance instance;
        final Matrix4 localTransform;
        final FloatArray visibleData = new FloatArray(false, INITIAL_INSTANCE_CAPACITY * INSTANCE_DATA_STRIDE);
        final FloatArray shadowData = new FloatArray(false, INITIAL_INSTANCE_CAPACITY * INSTANCE_DATA_STRIDE);
        private int instanceCapacity = INITIAL_INSTANCE_CAPACITY;
        private boolean visibleUploaded;

        InstancedModel(Model model, Matrix4 localTransform) {
            this.model = model;
            replaceMeshes(model);
            this.instance = new ModelInstance(model);
            this.localTransform = new Matrix4(localTransform);
            enableInstancing();
        }

        private static void replaceMeshes(Model model) {
            for(int meshIndex = 0; meshIndex < model.meshes.size; meshIndex++) {
                Mesh source = model.meshes.get(meshIndex);
                Mesh replacement = new ResizableInstancedMesh(source);
                model.meshes.set(meshIndex, replacement);
                for(int partIndex = 0; partIndex < model.meshParts.size; partIndex++) {
                    if(model.meshParts.get(partIndex).mesh == source) {
                        model.meshParts.get(partIndex).mesh = replacement;
                    }
                }

                Iterator<Disposable> iterator = model.getManagedDisposables().iterator();
                while(iterator.hasNext()) {
                    if(iterator.next() == source) {
                        iterator.remove();
                        break;
                    }
                }
                source.dispose();
                model.manageDisposable(replacement);
            }
        }

        void beginFrame() {
            visibleData.clear();
            shadowData.clear();
            visibleUploaded = false;
        }

        void appendVisible(Matrix4 transform, int color, Color tint, int bodyType) {
            appendInstanceData(visibleData, transform, color, tint, bodyType);
        }

        void appendShadow(Matrix4 transform, int color, Color tint, int bodyType) {
            appendInstanceData(shadowData, transform, color, tint, bodyType);
        }

        void appendWireOnly(Matrix4 transform, int color) {
            appendWireOnlyInstanceData(visibleData, transform, color);
        }

        boolean uploadVisibleInstances() {
            if(visibleData.size == 0) {
                return false;
            }
            if(!visibleUploaded) {
                upload(visibleData);
                visibleUploaded = true;
            }
            return true;
        }

        boolean uploadShadowInstances() {
            return upload(shadowData);
        }

        private boolean upload(FloatArray data) {
            if(data.size == 0) {
                return false;
            }
            int instanceCount = data.size / INSTANCE_DATA_STRIDE;
            ensureCapacity(instanceCount);
            for(int i = 0; i < model.meshes.size; i++) {
                model.meshes.get(i).setInstanceData(data.items, 0, data.size);
            }
            return true;
        }

        private void ensureCapacity(int requiredCapacity) {
            if(requiredCapacity <= instanceCapacity) {
                return;
            }
            while(instanceCapacity < requiredCapacity) {
                instanceCapacity *= 2;
            }
            for(int i = 0; i < model.meshes.size; i++) {
                Mesh mesh = model.meshes.get(i);
                mesh.disableInstancedRendering();
                mesh.enableInstancedRendering(false, instanceCapacity, createInstanceAttributes());
            }
        }

        private void enableInstancing() {
            for(int i = 0; i < model.meshes.size; i++) {
                model.meshes.get(i).enableInstancedRendering(false, instanceCapacity, createInstanceAttributes());
            }
        }

        ResizableInstancedMesh primaryMesh() {
            return (ResizableInstancedMesh)model.meshes.first();
        }

        @Override
        public void dispose() {
            model.dispose();
        }
    }

}
