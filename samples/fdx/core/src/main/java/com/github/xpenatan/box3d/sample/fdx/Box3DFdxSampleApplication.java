package com.github.xpenatan.box3d.sample.fdx;

import com.github.xpenatan.box3d.B3World;
import com.github.xpenatan.box3d.sample.shared.Box3DBodyDragController;
import com.github.xpenatan.box3d.sample.shared.Box3DDebugVisualization;
import com.github.xpenatan.box3d.sample.shared.Box3DLaunchShape;
import com.github.xpenatan.box3d.sample.shared.Box3DSample;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleCamera;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleController;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleEntry;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleHost;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleSettings;
import com.github.xpenatan.box3d.fdx.FdxDebugRenderer;
import io.github.libfdx.Fdx;
import io.github.libfdx.application.Application;
import io.github.libfdx.application.ApplicationAdapter;
import io.github.libfdx.core.FdxException;
import io.github.libfdx.core.Logger;
import io.github.libfdx.display.Display;
import io.github.libfdx.graphics.GraphicsContext;
import io.github.libfdx.graphics.camera.Camera;
import io.github.libfdx.graphics.camera.CameraProjection;
import io.github.libfdx.graphics.camera.controller.FreeCameraController3D;
import io.github.libfdx.input.Input;
import io.github.libfdx.input.Key;
import io.github.libfdx.input.MouseButton;
import io.github.libfdx.ui.Ui;
import io.github.libfdx.ui.UiBooleanState;
import io.github.libfdx.ui.UiFloatState;
import io.github.libfdx.ui.UiIntState;
import io.github.libfdx.ui.UiRoot;
import io.github.libfdx.ui.UiScope;
import io.github.libfdx.ui.UiState;
import io.github.libfdx.ui.UiToolkit;

public final class Box3DFdxSampleApplication extends ApplicationAdapter implements Box3DSampleHost {
    private static final int SELECTOR_HIT_WIDTH = 310;
    private static final float FPS_UPDATE_INTERVAL = 0.25f;
    private static final long SAMPLE_DOUBLE_CLICK_NANOS = 450_000_000L;
    private static final int THROW_CLICK_MAX_DRAG_PIXELS = 12;
    private static final int THROW_CLICK_MAX_DRAG_PIXELS_SQUARED =
            THROW_CLICK_MAX_DRAG_PIXELS * THROW_CLICK_MAX_DRAG_PIXELS;

    private final Box3DSampleController controller;
    private final Box3DBodyDragController bodyDrag = new Box3DBodyDragController();
    private final UiState<String> activeSampleName = Ui.state("Loading");
    private final UiState<String> fpsText = Ui.state("FPS: 0");
    private final UiBooleanState dragBodiesEnabled = Ui.state(true);
    private final UiFloatState subStepState = Ui.state(4.0f);
    private final UiFloatState hertzState = Ui.state(60.0f);
    private final UiFloatState workerState = Ui.state(1.0f);
    private final UiFloatState recycleCentimeterState = Ui.state(5.0f);
    private final UiBooleanState sleepEnabledState = Ui.state(true);
    private final UiBooleanState warmStartingEnabledState = Ui.state(true);
    private final UiBooleanState continuousEnabledState = Ui.state(true);
    private final UiIntState launchShapeIndex = Ui.state(Box3DLaunchShape.SPHERE.ordinal());
    private final UiFloatState launchSpeedState = Ui.state(Box3DSampleSettings.DEFAULT_LAUNCH_SPEED);
    private final UiIntState debugVisualizationIndex = Ui.state(Box3DDebugVisualization.ALL.index());
    private final UiFloatState shadowBiasState = Ui.state(Box3DSampleSettings.DEFAULT_SHADOW_BIAS);
    private final float[] dragRayOrigin = new float[3];
    private final float[] dragRayDirection = new float[3];
    private final float[] throwRayOrigin = new float[3];
    private final float[] throwRayDirection = new float[3];
    private Application application;
    private Display display;
    private Logger logger;
    private GraphicsContext graphics;
    private Input input;
    private Camera camera;
    private FreeCameraController3D flyCamera;
    private FdxDebugRenderer debugRenderer;
    private UiRoot uiRoot;
    private boolean resetTestKeyDown;
    private boolean resetCameraKeyDown;
    private boolean throwShapeKeyDown;
    private float fpsElapsed;
    private int fpsFrames;
    private boolean fpsHasValue;
    private int autoThrowAfterFrames;
    private boolean autoThrowDone;
    private int validateFramesPerSample;
    private int validationFrameCount;
    private boolean validationComplete;
    private boolean dragButtonDown;
    private boolean throwClickPending;
    private int throwClickX;
    private int throwClickY;
    private int lastSampleClickIndex = -1;
    private long lastSampleClickNanos;
    private boolean preserveCameraOnSampleChange;

    public Box3DFdxSampleApplication() {
        this(0L);
    }

    public Box3DFdxSampleApplication(long exitAfterFrames) {
        controller = new Box3DSampleController(this, exitAfterFrames);
        controller.setStepListener(bodyDrag::step);
    }

    @Override
    public void create(Fdx fdx) {
        application = fdx.app();
        display = fdx.displays().main();
        logger = fdx.logger();
        graphics = fdx.graphics().main();
        input = fdx.input();
        autoThrowAfterFrames = parsePositiveInt(System.getProperty("jbox3d.sample.autoThrowAfterFrames"), 0);
        validateFramesPerSample = parsePositiveInt(System.getProperty("jbox3d.sample.validateAll"), 0);
        camera = new Camera()
                .projection(CameraProjection.PERSPECTIVE)
                .fieldOfView(67.0f)
                .nearFar(0.1f, 200.0f);
        configureCamera(controller.selectedEntry().camera());
        resetFlyCameraController();

        uiRoot = new UiToolkit(fdx.files()).root(display, graphics).input(input);
        uiRoot.setContent(this::buildSelector);
        activeSampleName.set(controller.selectedEntry().displayName());

        controller.create();
    }

    @Override
    public void resize(int width, int height) {
        if(uiRoot != null) {
            uiRoot.resize(width, height);
        }
    }

    @Override
    public void render() {
        float deltaSeconds = Math.min(application.deltaTime(), 1.0f / 30.0f);
        updateControls(deltaSeconds);
        applySimulationSettingsFromUi();
        applyShadowBias();
        camera.viewport(framebufferWidth(), framebufferHeight()).update();
        graphics.clear(0.04f, 0.045f, 0.06f, 1.0f);

        try {
            controller.render(deltaSeconds);
            updateAutoThrow();
            updateValidation();
        }
        catch(RuntimeException exception) {
            throw new FdxException("jBox3D libfdx sample failed: " + controller.selectedEntry().displayName(),
                    exception);
        }
        updateFps(application.deltaTime());

        if(uiRoot != null) {
            uiRoot.update(application.deltaTime());
            uiRoot.render();
        }
    }

    @Override
    public void onSampleChanged(Box3DSampleEntry entry, Box3DSample sample) {
        if(debugRenderer == null) {
            debugRenderer = new FdxDebugRenderer(graphics);
            debugRenderer.setShadowBias(shadowBiasState.get());
            applyDebugVisualization();
        }
        else {
            debugRenderer.clearShapeCache();
        }
        bodyDrag.end();
        if(!preserveCameraOnSampleChange) {
            configureCamera(entry.camera());
            resetFlyCameraController();
        }
        validationFrameCount = 0;
        activeSampleName.set(entry.displayName());
        if(uiRoot != null) {
            uiRoot.requestCompose();
        }
        if(logger != null) {
            logger.info("Selected jBox3D sample: " + entry.displayName());
        }
    }

    @Override
    public void renderBox3D(B3World world) {
        if(debugRenderer != null) {
            debugRenderer.render(world, camera);
        }
    }

    @Override
    public void requestExit() {
        application.requestExit();
    }

    @Override
    public void dispose() {
        if(uiRoot != null) {
            uiRoot.dispose();
            uiRoot = null;
        }
        if(debugRenderer != null) {
            debugRenderer.dispose();
            debugRenderer = null;
        }
        bodyDrag.end();
        if(flyCamera != null) {
            flyCamera.dispose();
            flyCamera = null;
        }
        controller.dispose();
        if(logger != null) {
            logger.info("jBox3D libfdx sample disposed after " + controller.renderedFrames() + " frames");
        }
    }

    private void buildSelector(UiScope ui) {
        ui.row(Ui.modifier().fill().padding(12.0f).gap(12.0f), page -> {
            page.panel(Ui.modifier().width(286.0f).padding(10.0f).gap(6.0f), panel -> {
                panel.text("jBox3D Samples");
                panel.text(activeSampleName.get());
                panel.scrollView(Ui.modifier().fillWidth().height(660.0f), controls -> {
                    controls.scrollView(Ui.modifier().fillWidth().height(210.0f), list -> {
                        String previousCategory = "";
                        for(int i = 0; i < controller.entries().size(); i++) {
                            Box3DSampleEntry entry = controller.entries().get(i);
                            if(!entry.category().equals(previousCategory)) {
                                previousCategory = entry.category();
                                list.text(previousCategory);
                            }
                            final int sampleIndex = i;
                            list.button(entry.name(), Ui.modifier().fillWidth().height(30.0f),
                                    () -> selectSampleOnDoubleClick(sampleIndex));
                        }
                    });
                    controls.button("Reset Test", Ui.modifier().fillWidth().height(32.0f), this::resetTest);
                    controls.button("Reset Camera", Ui.modifier().fillWidth().height(32.0f), this::resetCamera);
                    controls.checkbox("Ctrl Drag Bodies", Ui.modifier().fillWidth().height(28.0f), dragBodiesEnabled);

                    controls.text("Solver");
                    controls.text("Sub-steps: " + Math.round(subStepState.get()));
                    controls.slider(Ui.modifier().fillWidth().height(24.0f), subStepState,
                            Box3DSampleSettings.MIN_SUB_STEPS, Box3DSampleSettings.MAX_SUB_STEPS);
                    controls.text("Hertz: " + Math.round(hertzState.get()));
                    controls.slider(Ui.modifier().fillWidth().height(24.0f), hertzState,
                            Box3DSampleSettings.MIN_HERTZ, Box3DSampleSettings.MAX_HERTZ);
                    controls.text("Workers: " + Math.round(workerState.get()));
                    controls.slider(Ui.modifier().fillWidth().height(24.0f), workerState,
                            Box3DSampleSettings.MIN_WORKERS, Box3DSampleSettings.MAX_WORKERS);
                    controls.text("Recycle: " + round(recycleCentimeterState.get(), 10.0f) + " cm");
                    controls.slider(Ui.modifier().fillWidth().height(24.0f), recycleCentimeterState,
                            Box3DSampleSettings.MIN_RECYCLE_CENTIMETERS,
                            Box3DSampleSettings.MAX_RECYCLE_CENTIMETERS);
                    controls.checkbox("Sleep", Ui.modifier().fillWidth().height(28.0f), sleepEnabledState);
                    controls.checkbox("Warm Starting", Ui.modifier().fillWidth().height(28.0f),
                            warmStartingEnabledState);
                    controls.checkbox("Continuous", Ui.modifier().fillWidth().height(28.0f), continuousEnabledState);

                    Box3DLaunchShape activeShape = Box3DLaunchShape.byIndex(launchShapeIndex.get());
                    controls.text("Throw: " + activeShape.label());
                    controls.scrollView(Ui.modifier().fillWidth().height(112.0f), list -> {
                        Box3DLaunchShape[] shapes = Box3DLaunchShape.values();
                        for(int i = 0; i < shapes.length; i++) {
                            Box3DLaunchShape shape = shapes[i];
                            final int shapeIndex = i;
                            String label = shape == activeShape ? "> " + shape.label() : shape.label();
                            list.button(label, Ui.modifier().fillWidth().height(28.0f),
                                    () -> selectLaunchShape(shapeIndex));
                        }
                    });
                    controls.text("Speed: " + Math.round(launchSpeedState.get()) + " m/s");
                    controls.slider(Ui.modifier().fillWidth().height(24.0f), launchSpeedState,
                            Box3DSampleSettings.MIN_LAUNCH_SPEED, Box3DSampleSettings.MAX_LAUNCH_SPEED);
                    controls.button("Throw Shape", Ui.modifier().fillWidth().height(32.0f), this::throwSelectedShape);

                    controls.text("Shadow Bias: " + round(shadowBiasState.get(), 1000.0f));
                    controls.slider(Ui.modifier().fillWidth().height(24.0f), shadowBiasState,
                            Box3DSampleSettings.MIN_SHADOW_BIAS, Box3DSampleSettings.MAX_SHADOW_BIAS);
                    Box3DDebugVisualization activeDebug = Box3DDebugVisualization.byIndex(debugVisualizationIndex.get());
                    controls.text("Debug View: " + activeDebug.label());
                    controls.scrollView(Ui.modifier().fillWidth().height(126.0f), list -> {
                        Box3DDebugVisualization[] visualizations = Box3DDebugVisualization.values();
                        for(int i = 0; i < visualizations.length; i++) {
                            Box3DDebugVisualization visualization = visualizations[i];
                            final int visualizationIndex = i;
                            String label = visualization == activeDebug ? "> " + visualization.label()
                                    : visualization.label();
                            list.button(label, Ui.modifier().fillWidth().height(28.0f),
                                    () -> selectDebugVisualization(visualizationIndex));
                        }
                    });
                });
            });
            page.panel(Ui.modifier().width(96.0f).padding(8.0f), panel -> {
                panel.text(fpsText.get());
            });
        });
    }

    private void selectSampleOnDoubleClick(int index) {
        long now = System.nanoTime();
        if(lastSampleClickIndex != index || now - lastSampleClickNanos > SAMPLE_DOUBLE_CLICK_NANOS) {
            lastSampleClickIndex = index;
            lastSampleClickNanos = now;
            return;
        }
        lastSampleClickIndex = -1;
        lastSampleClickNanos = 0L;
        selectSample(index);
    }

    private void selectSample(int index) {
        cancelPointerInteractions();
        Box3DSampleEntry entry = controller.entries().get(index);
        activeSampleName.set(entry.displayName());
        controller.selectSample(index);
        if(uiRoot != null) {
            uiRoot.requestCompose();
        }
    }

    private void selectDebugVisualization(int index) {
        debugVisualizationIndex.set(index);
        applyDebugVisualization();
        if(uiRoot != null) {
            uiRoot.requestCompose();
        }
    }

    private void selectLaunchShape(int index) {
        launchShapeIndex.set(index);
        controller.settings().setLaunchShapeIndex(index);
        if(uiRoot != null) {
            uiRoot.requestCompose();
        }
    }

    private void applySimulationSettingsFromUi() {
        Box3DSampleSettings settings = controller.settings();
        settings.setSubStepCount(Math.round(subStepState.get()));
        settings.setHertz(hertzState.get());
        settings.setWorkerCount(Math.round(workerState.get()));
        settings.setRecycleCentimeters(recycleCentimeterState.get());
        settings.setSleepEnabled(sleepEnabledState.get());
        settings.setWarmStartingEnabled(warmStartingEnabledState.get());
        settings.setContinuousEnabled(continuousEnabledState.get());
        settings.setLaunchShapeIndex(launchShapeIndex.get());
        settings.setLaunchSpeed(launchSpeedState.get());
        settings.setShadowBias(shadowBiasState.get());
    }

    private void applyDebugVisualization() {
        if(debugRenderer == null) {
            return;
        }
        Box3DDebugVisualization visualization = Box3DDebugVisualization.byIndex(debugVisualizationIndex.get());
        debugRenderer.setEnabled(visualization.rendererEnabled());
        debugRenderer.setDrawSolidShapes(visualization.drawSolidShapes());
        debugRenderer.setDrawWireframe(visualization.drawWireframe());
        debugRenderer.setShadowsEnabled(visualization.shadowsEnabled());
        visualization.apply(debugRenderer);
    }

    private void applyShadowBias() {
        if(debugRenderer != null) {
            debugRenderer.setShadowBias(controller.settings().shadowBias());
        }
    }

    private void throwSelectedShape() {
        if(camera == null || !controller.isReady()) {
            return;
        }
        pickRay(framebufferWidth() / 2, framebufferHeight() / 2, throwRayOrigin, throwRayDirection);
        throwShape(throwRayOrigin, throwRayDirection);
    }

    private void throwShape(float[] origin, float[] direction) {
        if(!controller.isReady()) {
            return;
        }
        controller.launchShape(origin[0], origin[1], origin[2], direction[0], direction[1], direction[2]);
    }

    private void configureCamera(Box3DSampleCamera sampleCamera) {
        camera.position(sampleCamera.positionX, sampleCamera.positionY, sampleCamera.positionZ)
                .lookAt(sampleCamera.targetX, sampleCamera.targetY, sampleCamera.targetZ);
    }

    private void updateControls(float deltaSeconds) {
        if(input == null) {
            return;
        }
        boolean resetTestPressed = input.isKeyPressed(Key.R);
        if(resetTestPressed && !resetTestKeyDown) {
            resetTest();
        }
        resetTestKeyDown = resetTestPressed;

        boolean resetCameraPressed = input.isKeyPressed(Key.C);
        if(resetCameraPressed && !resetCameraKeyDown) {
            resetCamera();
        }
        resetCameraKeyDown = resetCameraPressed;

        boolean throwShapePressed = input.isKeyPressed(Key.T);
        if(throwShapePressed && !throwShapeKeyDown) {
            throwSelectedShape();
        }
        throwShapeKeyDown = throwShapePressed;

        if(flyCamera != null) {
            flyCamera.update(deltaSeconds);
        }
        updateBodyDrag();
    }

    private void resetTest() {
        bodyDrag.end();
        preserveCameraOnSampleChange = true;
        try {
            controller.restartSample();
        }
        finally {
            preserveCameraOnSampleChange = false;
        }
    }

    private void resetCamera() {
        configureCamera(controller.selectedEntry().camera());
        resetFlyCameraController();
    }

    private void resetFlyCameraController() {
        if(input == null || camera == null) {
            return;
        }
        if(flyCamera != null) {
            flyCamera.dispose();
        }
        flyCamera = new FreeCameraController3D(input, camera)
                .speed(10.0f)
                .speedMultipliers(2.5f, 2.0f)
                .sensitivity(0.16f)
                .touchEnabled(false)
                .pointerRegion((x, y) -> x > SELECTOR_HIT_WIDTH);
    }

    private void updateBodyDrag() {
        bodyDrag.setEnabled(dragBodiesEnabled.get());
        if(input == null || camera == null || !controller.isReady()) {
            cancelPointerInteractions();
            return;
        }

        boolean leftPressed = input.isMouseButtonPressed(MouseButton.LEFT);
        boolean justPressed = leftPressed && !dragButtonDown;
        boolean justReleased = !leftPressed && dragButtonDown;
        if(!leftPressed) {
            if(justReleased && throwClickPending) {
                pickRay(throwClickX, throwClickY, throwRayOrigin, throwRayDirection);
                throwShape(throwRayOrigin, throwRayDirection);
            }
            bodyDrag.end();
            dragButtonDown = false;
            throwClickPending = false;
            return;
        }

        int pointerX = input.pointerX();
        int pointerY = input.pointerY();
        boolean overUi = pointerX <= SELECTOR_HIT_WIDTH;
        boolean ctrlPressed = input.isKeyPressed(Key.CONTROL_LEFT) || input.isKeyPressed(Key.CONTROL_RIGHT);
        if(justPressed) {
            throwClickPending = !overUi && !ctrlPressed;
            throwClickX = pointerX;
            throwClickY = pointerY;
        }
        else if(throwClickPending && pointerMovedPastThrowClickSlop(pointerX, pointerY)) {
            throwClickPending = false;
        }

        if(bodyDrag.isEnabled() && ctrlPressed && (!overUi || bodyDrag.isDragging())) {
            throwClickPending = false;
            pickRay(pointerX, pointerY, dragRayOrigin, dragRayDirection);
            boolean canStart = !bodyDrag.isDragging() && !overUi;
            if(canStart) {
                bodyDrag.begin(controller.world(), dragRayOrigin[0], dragRayOrigin[1], dragRayOrigin[2],
                        dragRayDirection[0], dragRayDirection[1], dragRayDirection[2]);
            }
            else {
                bodyDrag.updateTarget(dragRayOrigin[0], dragRayOrigin[1], dragRayOrigin[2], dragRayDirection[0],
                        dragRayDirection[1], dragRayDirection[2]);
            }
        }
        else {
            bodyDrag.end();
        }
        dragButtonDown = true;
    }

    private boolean pointerMovedPastThrowClickSlop(int x, int y) {
        int deltaX = x - throwClickX;
        int deltaY = y - throwClickY;
        return deltaX * deltaX + deltaY * deltaY > THROW_CLICK_MAX_DRAG_PIXELS_SQUARED;
    }

    private void cancelPointerInteractions() {
        bodyDrag.end();
        dragButtonDown = false;
        throwClickPending = false;
    }

    private void pickRay(int screenX, int screenY, float[] origin, float[] direction) {
        float width = Math.max(1.0f, framebufferWidth());
        float height = Math.max(1.0f, framebufferHeight());
        float ndcX = screenX / width * 2.0f - 1.0f;
        float ndcY = 1.0f - screenY / height * 2.0f;

        float forwardX = camera.direction().x();
        float forwardY = camera.direction().y();
        float forwardZ = camera.direction().z();
        float upX = camera.up().x();
        float upY = camera.up().y();
        float upZ = camera.up().z();

        float rightX = forwardY * upZ - forwardZ * upY;
        float rightY = forwardZ * upX - forwardX * upZ;
        float rightZ = forwardX * upY - forwardY * upX;
        float rightInvLength = inverseLength(rightX, rightY, rightZ);
        if(rightInvLength == 0.0f) {
            rightX = 1.0f;
            rightY = 0.0f;
            rightZ = 0.0f;
        }
        else {
            rightX *= rightInvLength;
            rightY *= rightInvLength;
            rightZ *= rightInvLength;
        }

        upX = rightY * forwardZ - rightZ * forwardY;
        upY = rightZ * forwardX - rightX * forwardZ;
        upZ = rightX * forwardY - rightY * forwardX;
        float upInvLength = inverseLength(upX, upY, upZ);
        if(upInvLength != 0.0f) {
            upX *= upInvLength;
            upY *= upInvLength;
            upZ *= upInvLength;
        }

        float verticalScale = (float)Math.tan(Math.toRadians(camera.fieldOfView()) * 0.5f);
        float horizontalScale = verticalScale * (width / height);
        float rayX = forwardX + rightX * ndcX * horizontalScale + upX * ndcY * verticalScale;
        float rayY = forwardY + rightY * ndcX * horizontalScale + upY * ndcY * verticalScale;
        float rayZ = forwardZ + rightZ * ndcX * horizontalScale + upZ * ndcY * verticalScale;
        float rayInvLength = inverseLength(rayX, rayY, rayZ);

        origin[0] = camera.position().x();
        origin[1] = camera.position().y();
        origin[2] = camera.position().z();
        direction[0] = rayInvLength != 0.0f ? rayX * rayInvLength : forwardX;
        direction[1] = rayInvLength != 0.0f ? rayY * rayInvLength : forwardY;
        direction[2] = rayInvLength != 0.0f ? rayZ * rayInvLength : forwardZ;
    }

    private static float inverseLength(float x, float y, float z) {
        float length = (float)Math.sqrt(x * x + y * y + z * z);
        return length > 0.000001f ? 1.0f / length : 0.0f;
    }

    private void updateFps(float deltaSeconds) {
        fpsElapsed += Math.max(0.0f, deltaSeconds);
        fpsFrames++;
        if(fpsElapsed <= 0.000001f || (fpsHasValue && fpsElapsed < FPS_UPDATE_INTERVAL)) {
            return;
        }
        fpsText.set("FPS: " + Math.round(fpsFrames / fpsElapsed));
        fpsHasValue = true;
        if(fpsElapsed >= FPS_UPDATE_INTERVAL) {
            fpsElapsed = 0.0f;
            fpsFrames = 0;
        }
    }

    private void updateValidation() {
        if(validateFramesPerSample <= 0 || validationComplete || !controller.isReady()) {
            return;
        }
        validationFrameCount++;
        if(validationFrameCount < validateFramesPerSample) {
            return;
        }

        int nextIndex = controller.selectedIndex() + 1;
        if(nextIndex >= controller.entries().size()) {
            validationComplete = true;
            if(logger != null) {
                logger.info("Validated " + controller.entries().size() + " jBox3D samples");
            }
            application.requestExit();
            return;
        }
        controller.selectSample(nextIndex);
    }

    private void updateAutoThrow() {
        if(autoThrowDone || autoThrowAfterFrames <= 0 || !controller.isReady()
                || controller.renderedFrames() < autoThrowAfterFrames) {
            return;
        }
        throwSelectedShape();
        autoThrowDone = true;
    }

    private static int parsePositiveInt(String value, int fallback) {
        if(value == null || value.trim().length() == 0) {
            return fallback;
        }
        try {
            return Math.max(0, Integer.parseInt(value.trim()));
        }
        catch(NumberFormatException ignored) {
            return fallback;
        }
    }

    private static float round(float value, float scale) {
        return Math.round(value * scale) / scale;
    }

    private int framebufferWidth() {
        int width = display.framebufferWidth() > 0 ? display.framebufferWidth() : display.width();
        return width > 0 ? width : 960;
    }

    private int framebufferHeight() {
        int height = display.framebufferHeight() > 0 ? display.framebufferHeight() : display.height();
        return height > 0 ? height : 540;
    }
}
