package com.github.xpenatan.box3d.sample.fdx;

import com.github.xpenatan.box3d.B3AABB;
import com.github.xpenatan.box3d.B3Vec3;
import com.github.xpenatan.box3d.B3World;
import com.github.xpenatan.box3d.sample.shared.Box3DBodyDragController;
import com.github.xpenatan.box3d.sample.shared.Box3DDebugVisualization;
import com.github.xpenatan.box3d.sample.shared.Box3DLaunchShape;
import com.github.xpenatan.box3d.sample.shared.Box3DPlayerTarget;
import com.github.xpenatan.box3d.sample.shared.Box3DSample;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleCamera;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleController;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleEntry;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleHost;
import com.github.xpenatan.box3d.sample.shared.Box3DSampleSettings;
import com.github.xpenatan.box3d.sample.shared.Box3DVisualProbe;
import com.github.xpenatan.box3d.sample.shared.samples.SampleAssets;
import io.github.libfdx.physics.box3d.FdxDebugRenderer;
import io.github.libfdx.Fdx;
import io.github.libfdx.application.Application;
import io.github.libfdx.application.ApplicationAdapter;
import io.github.libfdx.core.FdxException;
import io.github.libfdx.core.Logger;
import io.github.libfdx.display.Display;
import io.github.libfdx.graphics.GraphicsContext;
import io.github.libfdx.graphics.FrameBuffer;
import io.github.libfdx.graphics.camera.Camera;
import io.github.libfdx.graphics.camera.CameraProjection;
import io.github.libfdx.graphics.camera.controller.FreeCameraController3D;
import io.github.libfdx.input.Input;
import io.github.libfdx.input.Key;
import io.github.libfdx.input.MouseButton;
import io.github.libfdx.math.Ray;
import io.github.libfdx.math.Vector3;
import io.github.libfdx.ui.Ui;
import io.github.libfdx.ui.UiBooleanState;
import io.github.libfdx.ui.UiColor;
import io.github.libfdx.ui.UiDrawable;
import io.github.libfdx.ui.UiFloatState;
import io.github.libfdx.ui.UiFonts;
import io.github.libfdx.ui.UiIntState;
import io.github.libfdx.ui.UiRoot;
import io.github.libfdx.ui.UiScope;
import io.github.libfdx.ui.UiState;
import io.github.libfdx.ui.UiStyle;
import io.github.libfdx.ui.UiTextAlign;
import io.github.libfdx.ui.UiTextStyle;
import io.github.libfdx.ui.UiTheme;
import io.github.libfdx.ui.UiToolkit;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class Box3DFdxSampleApplication extends ApplicationAdapter implements Box3DSampleHost {
    private static final int SELECTOR_HIT_WIDTH = 310;
    private static final float FPS_UPDATE_INTERVAL = 0.25f;
    private static final float SAMPLE_DRAW_DISTANCE_MARGIN = 100.0f;
    private static final long PLAYER_VALIDATION_STEPS = 60L;
    private static final float PLAYER_VALIDATION_MIN_HORIZONTAL_DISTANCE = 0.05f;
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
    private final UiIntState selectorTab = Ui.state(0);
    private final UiBooleanState sleepEnabledState = Ui.state(true);
    private final UiBooleanState warmStartingEnabledState = Ui.state(true);
    private final UiBooleanState continuousEnabledState = Ui.state(true);
    private final UiIntState launchShapeIndex = Ui.state(Box3DLaunchShape.SPHERE.ordinal());
    private final UiFloatState launchSpeedState = Ui.state(Box3DSampleSettings.DEFAULT_LAUNCH_SPEED);
    private final UiIntState debugVisualizationIndex = Ui.state(Box3DDebugVisualization.SOLID_WIRE.index());
    private final UiFloatState shadowBiasState = Ui.state(Box3DSampleSettings.DEFAULT_SHADOW_BIAS);
    private final Ray dragRay = new Ray();
    private final Ray throwRay = new Ray();
    private final Box3DPlayerTarget playerTarget = new Box3DPlayerTarget();
    private final Box3DPlayerTarget validationPlayerStart = new Box3DPlayerTarget();
    private final Box3DPlayerTarget validationPlayerEnd = new Box3DPlayerTarget();
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
    private String screenshotPath;
    private long screenshotAfterFrames;
    private boolean screenshotWritten;
    private int autoThrowAfterFrames;
    private boolean autoThrowDone;
    private int validateFramesPerSample;
    private Box3DVisualProbe visualProbe;
    private int validationFrameCount;
    private int validationSampleCount;
    private boolean validationPlayerStartValid;
    private boolean validationComplete;
    private boolean dragButtonDown;
    private boolean throwClickPending;
    private int throwClickX;
    private int throwClickY;
    private boolean preserveCameraOnSampleChange;
    private boolean playerCameraFollowing;
    private float playerCameraRadius;

    public Box3DFdxSampleApplication(long exitAfterFrames, int workerCount) {
        controller = new Box3DSampleController(this, exitAfterFrames, workerCount);
        controller.setStepListener(bodyDrag::step);
    }

    @Override
    public void create(Fdx fdx) {
        SampleAssets.setReader(path -> fdx.files().internal(path).readString(StandardCharsets.UTF_8).get());
        application = fdx.app();
        display = fdx.displays().main();
        logger = fdx.logger();
        graphics = fdx.graphics().main();
        input = fdx.input();
        screenshotPath = System.getProperty("jbox3d.sample.screenshot", "").trim();
        screenshotAfterFrames = Math.max(1L,
                Long.parseLong(System.getProperty("jbox3d.sample.screenshotAfterFrames", "3")));
        autoThrowAfterFrames = parsePositiveInt(System.getProperty("jbox3d.sample.autoThrowAfterFrames"), 0);
        int requestedValidationFrames = parsePositiveInt(System.getProperty("jbox3d.sample.validateAll"), 0);
        validateFramesPerSample = requestedValidationFrames > 0 ? Math.max(3, requestedValidationFrames) : 0;
        debugVisualizationIndex.set(parseDebugVisualization(
                System.getProperty("jbox3d.sample.debugView", "Solid + Wire")).index());
        camera = new Camera()
                .projection(CameraProjection.PERSPECTIVE)
                .fieldOfView(60.0f)
                .nearFar(0.1f, 1000.0f);
        configureCamera(controller.selectedEntry().camera());
        resetFlyCameraController();

        uiRoot = new UiToolkit(fdx.files()).theme(sampleTheme()).root(display, graphics).input(input);
        uiRoot.setContent(this::buildSelector);
        activeSampleName.set(controller.selectedEntry().displayName());
        workerState.set(controller.settings().workerCount());

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
        writeScreenshotIfRequested();
    }

    @Override
    public void onSampleChanged(Box3DSampleEntry entry, Box3DSample sample) {
        if(validateFramesPerSample > 0 && visualProbe == null) {
            visualProbe = new Box3DVisualProbe();
        }
        if(debugRenderer == null) {
            debugRenderer = new FdxDebugRenderer(graphics);
            debugRenderer.setShadowBias(shadowBiasState.get());
            applyDebugVisualization();
        }
        else {
            debugRenderer.clearShapeCache();
        }
        debugRenderer.setDrawDistance(entry.camera().radius + SAMPLE_DRAW_DISTANCE_MARGIN);
        bodyDrag.end();
        if(!preserveCameraOnSampleChange) {
            configureCamera(entry.camera());
            resetFlyCameraController();
        }
        validationFrameCount = 0;
        validationPlayerStartValid = false;
        if(validateFramesPerSample > 0 && controller.isPlayerControlled()) {
            if(!controller.isThirdPerson()) {
                controller.toggleThirdPerson();
            }
            validationPlayerStartValid = controller.getCameraTarget(validationPlayerStart);
            if(!validationPlayerStartValid) {
                throw new FdxException("Player validation could not read the initial target for "
                        + entry.displayName());
            }
        }
        playerCameraFollowing = false;
        activeSampleName.set(entry.displayName());
        if(uiRoot != null) {
            uiRoot.requestCompose();
        }
        if(logger != null) {
            logger.info("Selected jBox3D sample: " + entry.displayName());
            if(validateFramesPerSample > 0) {
                logger.info(sceneAuditLine(entry, sample.world()));
            }
        }
    }

    @Override
    public void renderBox3D(B3World world) {
        updatePlayerCamera();
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
        if(visualProbe != null) {
            visualProbe.disposeResources();
            visualProbe.dispose();
            visualProbe = null;
        }
        if(logger != null) {
            logger.info("jBox3D libfdx sample disposed after " + controller.renderedFrames() + " frames");
        }
    }

    void buildSelector(UiScope ui) {
        ui.row(Ui.modifier().fill().padding(12.0f).gap(12.0f), page -> {
            page.panel(Ui.modifier().width(286.0f).fillHeight().padding(10.0f).gap(6.0f)
                    .style("sample-panel").validationId("sample-panel"), panel -> {
                panel.text("jBox3D Samples", Ui.modifier().fillWidth().style("title"));
                panel.text("Selected sample", Ui.modifier().fillWidth().style("eyebrow"));
                panel.text(activeSampleName.get(), Ui.modifier().fillWidth().height(24.0f)
                        .style("selected-sample"));
                panel.tabs(Ui.modifier().fillWidth().height(34.0f).validationId("selector-tabs"),
                        selectorTab, "Samples", "Settings");
                if(selectorTab.get() == 0) {
                    panel.scrollView(Ui.modifier().fillWidth().minHeight(80.0f).weight(1.0f)
                                    .padding(4.0f).gap(2.0f).style("sample-list")
                                    .validationId("sample-list"),
                            this::buildSampleList);
                    panel.divider(Ui.modifier().fillWidth().height(1.0f));
                    panel.row(Ui.modifier().fillWidth().height(34.0f).gap(6.0f), actions -> {
                        actions.button("Reset Test", Ui.modifier().weight(1.0f).height(34.0f)
                                .validationId("reset-test"), this::resetTest);
                        actions.button("Reset Camera", Ui.modifier().weight(1.0f).height(34.0f)
                                .validationId("reset-camera"), this::resetCamera);
                    });
                    panel.checkbox("Ctrl Drag Bodies", Ui.modifier().fillWidth().height(28.0f),
                            dragBodiesEnabled);
                }
                else {
                    panel.scrollView(Ui.modifier().fillWidth().minHeight(80.0f).weight(1.0f)
                                    .padding(6.0f).gap(6.0f).style("settings-list")
                                    .validationId("settings-list"),
                            this::buildSettings);
                }
            });
            page.spacer(Ui.modifier().weight(1.0f));
            page.panel(Ui.modifier().width(96.0f).padding(8.0f), panel -> {
                panel.text(fpsText.get(), Ui.modifier().fillWidth().style("fps"));
            });
        });
    }

    void buildSampleList(UiScope list) {
        String previousCategory = "";
        for(int i = 0; i < controller.entries().size(); i++) {
            Box3DSampleEntry entry = controller.entries().get(i);
            if(!entry.category().equals(previousCategory)) {
                previousCategory = entry.category();
                list.text(previousCategory, Ui.modifier().fillWidth().height(24.0f).style("sample-category"));
            }
            final int sampleIndex = i;
            boolean selected = sampleIndex == controller.selectedIndex();
            list.button(entry.name(), Ui.modifier().fillWidth().height(30.0f)
                            .style(selected ? "sample-row-selected" : "sample-row")
                            .validationId("sample-" + sampleIndex)
                            .semanticLabel((selected ? "Selected sample: " : "Select sample: ") + entry.displayName()),
                    () -> selectSample(sampleIndex));
        }
    }

    private void buildSettings(UiScope controls) {
        controls.text("Solver", Ui.modifier().fillWidth().style("section"));
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
        controls.checkbox("Warm Starting", Ui.modifier().fillWidth().height(28.0f), warmStartingEnabledState);
        controls.checkbox("Continuous", Ui.modifier().fillWidth().height(28.0f), continuousEnabledState);

        Box3DLaunchShape activeShape = Box3DLaunchShape.byIndex(launchShapeIndex.get());
        controls.text("Throw: " + activeShape.label(), Ui.modifier().fillWidth().style("section"));
        controls.scrollView(Ui.modifier().fillWidth().height(112.0f).padding(3.0f).gap(2.0f)
                .style("option-list"), list -> {
            Box3DLaunchShape[] shapes = Box3DLaunchShape.values();
            for(int i = 0; i < shapes.length; i++) {
                Box3DLaunchShape shape = shapes[i];
                final int shapeIndex = i;
                list.button(shape.label(), Ui.modifier().fillWidth().height(28.0f)
                                .style(shape == activeShape ? "option-row-selected" : "option-row"),
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
        controls.text("Debug View: " + activeDebug.label(), Ui.modifier().fillWidth().style("section"));
        controls.scrollView(Ui.modifier().fillWidth().height(126.0f).padding(3.0f).gap(2.0f)
                .style("option-list"), list -> {
            Box3DDebugVisualization[] visualizations = Box3DDebugVisualization.values();
            for(int i = 0; i < visualizations.length; i++) {
                Box3DDebugVisualization visualization = visualizations[i];
                final int visualizationIndex = i;
                list.button(visualization.label(), Ui.modifier().fillWidth().height(28.0f)
                                .style(visualization == activeDebug ? "option-row-selected" : "option-row"),
                        () -> selectDebugVisualization(visualizationIndex));
            }
        });
    }

    static UiTheme sampleTheme() {
        UiColor textColor = UiColor.rgba8888(0xf2f6fbff);
        UiColor mutedColor = UiColor.rgba8888(0x9fabb9ff);
        UiColor accentColor = UiColor.rgba8888(0x66b8ffff);
        UiTextStyle bodyText = UiTextStyle.text()
                .font(UiFonts.defaultFont(14.0f))
                .size(14.0f)
                .lineHeight(18.0f)
                .color(textColor);
        UiTextStyle buttonText = bodyText.align(UiTextAlign.CENTER).wrap(false).ellipsis(true);
        UiTextStyle rowText = bodyText.align(UiTextAlign.START).wrap(false).ellipsis(true);
        UiTextStyle titleText = UiTextStyle.text()
                .font(UiFonts.defaultFont(20.0f))
                .size(20.0f)
                .lineHeight(25.0f)
                .color(UiColor.WHITE)
                .wrap(false)
                .ellipsis(true);
        UiTextStyle sectionText = UiTextStyle.text()
                .font(UiFonts.defaultFont(13.0f))
                .size(13.0f)
                .lineHeight(17.0f)
                .color(accentColor)
                .wrap(false)
                .ellipsis(true);
        UiTextStyle eyebrowText = UiTextStyle.text()
                .font(UiFonts.defaultFont(11.0f))
                .size(11.0f)
                .lineHeight(14.0f)
                .color(mutedColor)
                .wrap(false)
                .ellipsis(true);
        UiTextStyle selectedSampleText = bodyText.color(UiColor.WHITE).wrap(false).ellipsis(true);

        UiStyle button = UiStyle.button()
                .padding(8.0f, 5.0f)
                .text(buttonText)
                .background(UiDrawable.color(UiColor.rgba8888(0x2b3a4aff)))
                .hover(UiStyle.button().padding(8.0f, 5.0f).text(buttonText)
                        .background(UiDrawable.color(UiColor.rgba8888(0x3a5067ff))))
                .pressed(UiStyle.button().padding(8.0f, 5.0f).text(buttonText)
                        .background(UiDrawable.color(UiColor.rgba8888(0x21303fff))));
        UiStyle row = UiStyle.button()
                .padding(10.0f, 5.0f)
                .text(rowText)
                .background(UiDrawable.color(UiColor.rgba8888(0x111923ff)))
                .hover(UiStyle.button().padding(10.0f, 5.0f).text(rowText)
                        .background(UiDrawable.color(UiColor.rgba8888(0x1d2d3dff))))
                .pressed(UiStyle.button().padding(10.0f, 5.0f).text(rowText)
                        .background(UiDrawable.color(UiColor.rgba8888(0x0b121aff))));
        UiStyle selectedRow = UiStyle.button()
                .padding(10.0f, 5.0f)
                .text(rowText.color(UiColor.WHITE))
                .background(UiDrawable.color(UiColor.rgba8888(0x286da8ff)))
                .hover(UiStyle.button().padding(10.0f, 5.0f).text(rowText.color(UiColor.WHITE))
                        .background(UiDrawable.color(UiColor.rgba8888(0x3486c8ff))))
                .pressed(UiStyle.button().padding(10.0f, 5.0f).text(rowText.color(UiColor.WHITE))
                        .background(UiDrawable.color(UiColor.rgba8888(0x205784ff))));
        UiStyle checkbox = UiStyle.style()
                .text(bodyText.wrap(false).ellipsis(true))
                .background(UiDrawable.color(UiColor.rgba8888(0x263442ff)))
                .foreground(UiDrawable.color(accentColor));
        UiStyle slider = UiStyle.style()
                .background(UiDrawable.color(UiColor.rgba8888(0x263442ff)))
                .foreground(UiDrawable.color(accentColor));
        UiStyle divider = UiStyle.style()
                .foreground(UiDrawable.color(UiColor.rgba8888(0x344555ff)));
        UiStyle tabs = UiStyle.style()
                .padding(2.0f)
                .text(buttonText)
                .background(UiDrawable.color(UiColor.rgba8888(0x111923ff)))
                .foreground(UiDrawable.color(accentColor));

        return Ui.darkTheme()
                .colors(UiColor.rgba8888(0x090e14ff), textColor)
                .text(UiStyle.style().text(bodyText))
                .button(button)
                .checkbox(checkbox)
                .slider(slider)
                .divider(divider)
                .tabs(tabs)
                .style("sample-panel", UiStyle.style()
                        .background(UiDrawable.color(UiColor.rgba8888(0x111923f2))))
                .style("sample-list", UiStyle.style()
                        .background(UiDrawable.color(UiColor.rgba8888(0x0b1118f2))))
                .style("settings-list", UiStyle.style()
                        .background(UiDrawable.color(UiColor.rgba8888(0x0d151df2))))
                .style("option-list", UiStyle.style()
                        .background(UiDrawable.color(UiColor.rgba8888(0x090f15f2))))
                .style("sample-row", row)
                .style("sample-row-selected", selectedRow)
                .style("option-row", row)
                .style("option-row-selected", selectedRow)
                .style("title", UiStyle.style().text(titleText))
                .style("section", UiStyle.style().text(sectionText))
                .style("eyebrow", UiStyle.style().text(eyebrowText))
                .style("sample-category", UiStyle.style().padding(8.0f, 3.0f).text(eyebrowText))
                .style("selected-sample", UiStyle.style().padding(8.0f, 3.0f)
                        .text(selectedSampleText)
                        .background(UiDrawable.color(UiColor.rgba8888(0x1b2b3aff))))
                .style("fps", UiStyle.style().text(bodyText.align(UiTextAlign.CENTER).wrap(false)));
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
        int workerCount = Math.round(workerState.get());
        if(workerCount != settings.workerCount()) {
            settings.setWorkerCount(workerCount);
            controller.restartSample();
        }
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
        int width = displayWidth();
        int height = displayHeight();
        camera.getPickRay(width * 0.5f, height * 0.5f,
                0.0f, 0.0f, width, height, throwRay);
        throwShape(throwRay);
    }

    private void throwShape(Ray ray) {
        if(!controller.isReady()) {
            return;
        }
        Vector3 origin = ray.origin();
        Vector3 direction = ray.direction();
        controller.launchShape(origin.x(), origin.y(), origin.z(), direction.x(), direction.y(), direction.z());
    }

    private void configureCamera(Box3DSampleCamera sampleCamera) {
        camera.position(sampleCamera.positionX, sampleCamera.positionY, sampleCamera.positionZ)
                .lookAt(sampleCamera.targetX, sampleCamera.targetY, sampleCamera.targetZ);
        playerCameraFollowing = false;
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

        boolean actionPressed = input.isKeyPressed(Key.T);
        if(actionPressed && !throwShapeKeyDown) {
            if(controller.isPlayerControlled()) {
                controller.toggleThirdPerson();
                playerCameraFollowing = false;
            }
            else {
                throwSelectedShape();
            }
        }
        throwShapeKeyDown = actionPressed;

        if(flyCamera != null) {
            flyCamera.keyboardEnabled(!controller.isThirdPerson());
            flyCamera.update(deltaSeconds);
        }
        updatePlayerInput();
        updateBodyDrag();
    }

    private void updatePlayerInput() {
        if(validateFramesPerSample > 0 && !validationComplete && controller.isPlayerControlled()) {
            float directionX = camera.direction().x();
            float directionZ = camera.direction().z();
            controller.setPlayerInput(1.0f, 0.0f, directionX, directionZ,
                    -directionZ, directionX, false, true);
            return;
        }
        if(!controller.isThirdPerson()) {
            controller.setPlayerInput(0.0f, 0.0f, camera.direction().x(), camera.direction().z(),
                    -camera.direction().z(), camera.direction().x(), false, false);
            return;
        }
        float moveForward = (input.isKeyPressed(Key.W) ? 1.0f : 0.0f)
                - (input.isKeyPressed(Key.S) ? 1.0f : 0.0f);
        float moveRight = (input.isKeyPressed(Key.D) ? 1.0f : 0.0f)
                - (input.isKeyPressed(Key.A) ? 1.0f : 0.0f);
        float directionX = camera.direction().x();
        float directionZ = camera.direction().z();
        controller.setPlayerInput(moveForward, moveRight, directionX, directionZ,
                -directionZ, directionX, input.isKeyPressed(Key.SPACE),
                input.isKeyPressed(Key.SHIFT_LEFT) || input.isKeyPressed(Key.SHIFT_RIGHT));
    }

    private void updatePlayerCamera() {
        if(camera == null || !controller.getCameraTarget(playerTarget)) {
            playerCameraFollowing = false;
            return;
        }
        Vector3 position = camera.position();
        Vector3 direction = camera.direction();
        if(!playerCameraFollowing) {
            float dx = position.x() - playerTarget.x();
            float dy = position.y() - playerTarget.y();
            float dz = position.z() - playerTarget.z();
            playerCameraRadius = Math.max(0.1f, (float)Math.sqrt(dx * dx + dy * dy + dz * dz));
        }
        float x = playerTarget.x() - playerCameraRadius * direction.x();
        float y = playerTarget.y() - playerCameraRadius * direction.y();
        float z = playerTarget.z() - playerCameraRadius * direction.z();
        camera.position(x, y, z).lookAt(playerTarget.x(), playerTarget.y(), playerTarget.z()).update();
        if(flyCamera != null) {
            flyCamera.position(x, y, z);
        }
        playerCameraFollowing = true;
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
                camera.getPickRay(throwClickX, throwClickY,
                        0.0f, 0.0f, displayWidth(), displayHeight(), throwRay);
                throwShape(throwRay);
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
            camera.getPickRay(pointerX, pointerY,
                    0.0f, 0.0f, displayWidth(), displayHeight(), dragRay);
            Vector3 origin = dragRay.origin();
            Vector3 direction = dragRay.direction();
            boolean canStart = !bodyDrag.isDragging() && !overUi;
            if(canStart) {
                bodyDrag.begin(controller.world(), origin.x(), origin.y(), origin.z(),
                        direction.x(), direction.y(), direction.z());
            }
            else {
                bodyDrag.updateTarget(origin.x(), origin.y(), origin.z(),
                        direction.x(), direction.y(), direction.z());
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
        long requiredSteps = validationPlayerStartValid ? PLAYER_VALIDATION_STEPS : 1L;
        if(validationFrameCount < validateFramesPerSample || controller.sampleStepCount() < requiredSteps) {
            return;
        }

        int primitiveCount = visualProbe.inspect(controller.world());
        if(primitiveCount <= 0) {
            throw new FdxException("Visual validation drew no Box3D geometry for "
                    + controller.selectedEntry().displayName());
        }
        if(!visualProbe.isFinite()) {
            throw new FdxException("Visual validation found a non-finite position for "
                    + controller.selectedEntry().displayName());
        }
        if(logger != null) {
            logger.info("VISUAL_AUDIT\t" + controller.selectedIndex() + "\t"
                    + controller.selectedEntry().displayName() + "\t" + primitiveCount);
        }
        validatePlayerMovement();

        validationSampleCount++;
        if(validationSampleCount >= controller.entries().size()) {
            validationComplete = true;
            if(logger != null) {
                logger.info("Validated " + controller.entries().size() + " jBox3D samples");
            }
            application.requestExit();
            return;
        }
        int nextIndex = (controller.selectedIndex() + 1) % controller.entries().size();
        controller.selectSample(nextIndex);
    }

    private void validatePlayerMovement() {
        if(!validationPlayerStartValid) {
            return;
        }
        if(!controller.getCameraTarget(validationPlayerEnd)) {
            throw new FdxException("Player validation could not read the final target for "
                    + controller.selectedEntry().displayName());
        }
        float dx = validationPlayerEnd.x() - validationPlayerStart.x();
        float dz = validationPlayerEnd.z() - validationPlayerStart.z();
        float distance = (float)Math.sqrt(dx * dx + dz * dz);
        if(!Float.isFinite(distance) || distance < PLAYER_VALIDATION_MIN_HORIZONTAL_DISTANCE) {
            throw new FdxException("Player validation did not move "
                    + controller.selectedEntry().displayName() + " horizontally: " + distance);
        }
        if(logger != null) {
            logger.info("PLAYER_AUDIT\t" + controller.selectedIndex() + "\t"
                    + controller.selectedEntry().displayName() + "\t" + distance);
        }
    }

    private void updateAutoThrow() {
        if(autoThrowDone || autoThrowAfterFrames <= 0 || !controller.isReady()
                || controller.renderedFrames() < autoThrowAfterFrames) {
            return;
        }
        throwSelectedShape();
        autoThrowDone = true;
    }

    private void writeScreenshotIfRequested() {
        if(screenshotWritten || screenshotPath.length() == 0 || !controller.isReady()
                || controller.renderedFrames() < screenshotAfterFrames) {
            return;
        }

        FrameBuffer frameBuffer = graphics.currentFrame().frameBuffer();
        if(!frameBuffer.supportsReadPixelsRgba8()) {
            throw new FdxException("The active libFDX graphics provider does not support framebuffer capture");
        }
        try {
            writePpm(screenshotPath, frameBuffer.width(), frameBuffer.height(),
                    frameBuffer.readPixelsRgba8());
            screenshotWritten = true;
            logger.info("Wrote jBox3D libFDX screenshot: " + screenshotPath);
        }
        catch(Exception exception) {
            throw new FdxException("Could not write jBox3D libFDX screenshot: " + screenshotPath,
                    exception);
        }
    }

    private static void writePpm(String path, int width, int height, ByteBuffer rgba) throws Exception {
        if(width <= 0 || height <= 0 || rgba == null || rgba.limit() < width * height * 4) {
            throw new FdxException("Framebuffer capture does not contain a complete RGBA8 image");
        }
        File file = new File(path);
        File parent = file.getParentFile();
        if(parent != null && !parent.isDirectory() && !parent.mkdirs()) {
            throw new FdxException("Could not create screenshot directory: " + parent);
        }
        try(FileOutputStream output = new FileOutputStream(file)) {
            output.write(("P6\n" + width + " " + height + "\n255\n")
                    .getBytes(StandardCharsets.US_ASCII));
            byte[] row = new byte[width * 3];
            for(int y = height - 1; y >= 0; y--) {
                int source = y * width * 4;
                int target = 0;
                for(int x = 0; x < width; x++) {
                    row[target++] = rgba.get(source);
                    row[target++] = rgba.get(source + 1);
                    row[target++] = rgba.get(source + 2);
                    source += 4;
                }
                output.write(row);
            }
        }
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

    private String sceneAuditLine(Box3DSampleEntry entry, B3World world) {
        B3AABB bounds = world.GetBounds();
        B3Vec3 lower = bounds.GetLowerBound();
        B3Vec3 upper = bounds.GetUpperBound();
        String line = "SCENE_AUDIT\t" + controller.selectedIndex() + "\t" + entry.category() + "\t"
                + entry.name() + "\t" + lower.GetX() + "\t" + lower.GetY() + "\t" + lower.GetZ() + "\t"
                + upper.GetX() + "\t" + upper.GetY() + "\t" + upper.GetZ();
        return line;
    }

    private static Box3DDebugVisualization parseDebugVisualization(String value) {
        String trimmed = value != null ? value.trim() : "";
        String normalized = trimmed.replace('-', '_').replace(' ', '_');
        Box3DDebugVisualization[] values = Box3DDebugVisualization.values();
        for(int i = 0; i < values.length; i++) {
            Box3DDebugVisualization visualization = values[i];
            if(visualization.name().equalsIgnoreCase(normalized)
                    || visualization.label().equalsIgnoreCase(trimmed)) {
                return visualization;
            }
        }
        throw new FdxException("Unknown jbox3d.sample.debugView: " + value);
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

    private int displayWidth() {
        int width = display.width() > 0 ? display.width() : display.framebufferWidth();
        return width > 0 ? width : 960;
    }

    private int displayHeight() {
        int height = display.height() > 0 ? display.height() : display.framebufferHeight();
        return height > 0 ? height : 540;
    }
}
