package com.github.xpenatan.box3d.sample.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
import com.github.xpenatan.box3d.gdx.GdxDebugRenderer;

public final class Box3DGdxSampleApplication extends ApplicationAdapter implements Box3DSampleHost {
    private static final int SELECTOR_HIT_WIDTH = 326;
    private static final int SETTINGS_HIT_WIDTH = 326;
    private static final int MENU_BAR_HIT_HEIGHT = 38;
    private static final float FPS_UPDATE_INTERVAL = 0.25f;
    private static final float FLY_LOOK_RADIANS_PER_PIXEL = 0.0026f;
    private static final float FLY_SPEED = 10.0f;
    private static final float FLY_MAX_PITCH = (float)Math.toRadians(89.0f);
    private static final int THROW_CLICK_MAX_DRAG_PIXELS = 12;
    private static final int THROW_CLICK_MAX_DRAG_PIXELS_SQUARED =
            THROW_CLICK_MAX_DRAG_PIXELS * THROW_CLICK_MAX_DRAG_PIXELS;

    private final Box3DSampleController controller;
    private final Box3DBodyDragController bodyDrag = new Box3DBodyDragController();
    private final Vector3 flyDirection = new Vector3();
    private final Vector3 flyRight = new Vector3();
    private final Vector3 flyMove = new Vector3();
    private PerspectiveCamera camera;
    private GdxDebugRenderer debugRenderer;
    private Stage stage;
    private Skin skin;
    private Label activeSampleNameLabel;
    private Label activeSampleCategoryLabel;
    private Label frameTimeLabel;
    private Label cameraPoseLabel;
    private Label cameraAnglesLabel;
    private Label fpsLabel;
    private Label subStepsLabel;
    private Label hertzLabel;
    private Label workersLabel;
    private Label recycleLabel;
    private Label launchSpeedLabel;
    private Label shadowBiasLabel;
    private Slider shadowBiasSlider;
    private com.badlogic.gdx.scenes.scene2d.ui.List<String> sampleList;
    private com.badlogic.gdx.scenes.scene2d.ui.List<String> launchShapeList;
    private com.badlogic.gdx.scenes.scene2d.ui.List<String> debugVisualizationList;
    private CheckBox sleepCheckBox;
    private CheckBox warmStartingCheckBox;
    private CheckBox continuousCheckBox;
    private float cameraYawRadians;
    private float cameraPitchRadians;
    private float fpsElapsed;
    private int fpsFrames;
    private int debugVisualizationIndex = Box3DDebugVisualization.ALL.index();
    private boolean fpsHasValue;
    private boolean updatingSelection;
    private boolean updatingDebugVisualizationSelection;
    private String screenshotPath;
    private long screenshotAfterFrames;
    private boolean screenshotWritten;
    private int autoThrowAfterFrames;
    private boolean autoThrowDone;
    private int validateFramesPerSample;
    private int validationFrameCount;
    private boolean validationComplete;
    private boolean dragButtonDown;
    private boolean throwClickPending;
    private int throwClickX;
    private int throwClickY;
    private boolean preserveCameraOnSampleChange;

    public Box3DGdxSampleApplication() {
        this(0L);
    }

    public Box3DGdxSampleApplication(long exitAfterFrames) {
        controller = new Box3DSampleController(this, exitAfterFrames);
        controller.setStepListener(bodyDrag::step);
    }

    @Override
    public void create() {
        camera = new PerspectiveCamera(67.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        configureCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), controller.selectedEntry().camera());
        screenshotPath = System.getProperty("jbox3d.sample.screenshot", "");
        screenshotAfterFrames = Long.parseLong(System.getProperty("jbox3d.sample.screenshotAfterFrames", "3"));
        autoThrowAfterFrames = parsePositiveInt(System.getProperty("jbox3d.sample.autoThrowAfterFrames"), 0);
        validateFramesPerSample = parsePositiveInt(System.getProperty("jbox3d.sample.validateAll"), 0);
        createSelector();
        controller.create();
    }

    @Override
    public void resize(int width, int height) {
        updateCameraViewport(width, height);
        if(stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.04f, 0.045f, 0.06f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        float deltaSeconds = Gdx.graphics.getDeltaTime();
        updateControls(deltaSeconds);

        try {
            controller.render(deltaSeconds);
            updateAutoThrow();
            updateValidation();
        }
        catch(RuntimeException exception) {
            throw new GdxRuntimeException("jBox3D libGDX sample failed: " + controller.selectedEntry().displayName(),
                    exception);
        }
        updateFps(deltaSeconds);
        updateInfoLabels(deltaSeconds);

        if(stage != null) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
        writeScreenshotIfRequested();
    }

    @Override
    public void onSampleChanged(Box3DSampleEntry entry, Box3DSample sample) {
        if(debugRenderer == null) {
            debugRenderer = new GdxDebugRenderer();
            debugRenderer.setShadowBias(controller.settings().shadowBias());
            applyDebugVisualization();
        }
        else {
            debugRenderer.clearShapeCache();
        }
        bodyDrag.end();
        if(!preserveCameraOnSampleChange) {
            configureCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), entry.camera());
        }
        validationFrameCount = 0;
        updateSelectorSelection(entry);
        updateDebugVisualizationSelection();
        updateShadowBiasLabel();
        Gdx.app.log("jBox3D", "Selected sample " + (controller.selectedIndex() + 1) + "/"
                + controller.entries().size() + ": " + entry.displayName());
    }

    @Override
    public void renderBox3D(B3World world) {
        if(debugRenderer != null) {
            debugRenderer.render(world, camera);
        }
    }

    @Override
    public void requestExit() {
        Gdx.app.exit();
    }

    @Override
    public void dispose() {
        if(debugRenderer != null) {
            debugRenderer.dispose();
            debugRenderer = null;
        }
        bodyDrag.end();
        controller.dispose();
        if(stage != null) {
            stage.dispose();
            stage = null;
        }
        if(skin != null) {
            skin.dispose();
            skin = null;
        }
    }

    private void createSelector() {
        skin = createSkin();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        root.top().left();

        Table selectorPanel = new Table();
        selectorPanel.setBackground(skin.newDrawable("white", new Color(0.06f, 0.07f, 0.09f, 0.92f)));
        selectorPanel.defaults().left().growX();

        Table panel = new Table();
        panel.setBackground(skin.newDrawable("white", new Color(0.06f, 0.07f, 0.09f, 0.92f)));
        panel.defaults().left().growX();
        Box3DSampleSettings settings = controller.settings();
        Box3DSampleEntry selectedEntry = controller.selectedEntry();

        activeSampleNameLabel = new Label(selectedEntry.name(), skin, "title");
        activeSampleCategoryLabel = new Label(selectedEntry.category(), skin, "muted");
        frameTimeLabel = new Label("", skin, "metric");
        cameraPoseLabel = new Label("", skin, "metric");
        cameraAnglesLabel = new Label("", skin, "metric");
        panel.add(activeSampleNameLabel).pad(10.0f, 10.0f, 2.0f, 10.0f).row();
        panel.add(activeSampleCategoryLabel).pad(0.0f, 10.0f, 8.0f, 10.0f).row();
        panel.add(frameTimeLabel).pad(0.0f, 10.0f, 4.0f, 10.0f).row();
        panel.add(cameraPoseLabel).pad(0.0f, 10.0f, 2.0f, 10.0f).row();
        panel.add(cameraAnglesLabel).pad(0.0f, 10.0f, 10.0f, 10.0f).row();

        selectorPanel.add(new Label("Samples", skin, "title")).pad(10.0f, 10.0f, 6.0f, 10.0f).row();

        sampleList = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(skin);
        sampleList.setItems(sampleNames());
        sampleList.setSelectedIndex(controller.selectedIndex());
        sampleList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!updatingSelection) {
                    controller.selectSample(sampleList.getSelectedIndex());
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane(sampleList, skin);
        scrollPane.setFadeScrollBars(false);
        selectorPanel.add(scrollPane).width(286.0f).growY().pad(0.0f, 10.0f, 10.0f, 10.0f).row();

        TextButton resetTestButton = new TextButton("Reset Test", skin);
        resetTestButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resetTest();
            }
        });
        panel.add(resetTestButton).height(30.0f).pad(0.0f, 10.0f, 6.0f, 10.0f).row();

        TextButton resetCameraButton = new TextButton("Reset Camera", skin);
        resetCameraButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resetCamera();
            }
        });
        panel.add(resetCameraButton).height(30.0f).pad(0.0f, 10.0f, 10.0f, 10.0f).row();

        CheckBox dragBodiesCheckBox = createCheckBox("Ctrl Drag Bodies", bodyDrag.isEnabled());
        dragBodiesCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                bodyDrag.setEnabled(dragBodiesCheckBox.isChecked());
            }
        });
        panel.add(dragBodiesCheckBox).height(28.0f).pad(0.0f, 10.0f, 10.0f, 10.0f).row();

        panel.add(new Label("Solver", skin)).pad(0.0f, 10.0f, 4.0f, 10.0f).row();
        subStepsLabel = new Label("", skin);
        panel.add(subStepsLabel).pad(0.0f, 10.0f, 2.0f, 10.0f).row();
        Slider subStepsSlider = new Slider(Box3DSampleSettings.MIN_SUB_STEPS,
                Box3DSampleSettings.MAX_SUB_STEPS, 1.0f, false, skin);
        subStepsSlider.setValue(settings.subStepCount());
        subStepsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSubStepCount(Math.round(subStepsSlider.getValue()));
                updateSimulationLabels();
            }
        });
        panel.add(subStepsSlider).height(22.0f).pad(0.0f, 10.0f, 6.0f, 10.0f).row();

        hertzLabel = new Label("", skin);
        panel.add(hertzLabel).pad(0.0f, 10.0f, 2.0f, 10.0f).row();
        Slider hertzSlider = new Slider(Box3DSampleSettings.MIN_HERTZ, Box3DSampleSettings.MAX_HERTZ, 1.0f, false,
                skin);
        hertzSlider.setValue(settings.hertz());
        hertzSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setHertz(hertzSlider.getValue());
                updateSimulationLabels();
            }
        });
        panel.add(hertzSlider).height(22.0f).pad(0.0f, 10.0f, 6.0f, 10.0f).row();

        workersLabel = new Label("", skin);
        panel.add(workersLabel).pad(0.0f, 10.0f, 2.0f, 10.0f).row();
        Slider workersSlider = new Slider(Box3DSampleSettings.MIN_WORKERS, Box3DSampleSettings.MAX_WORKERS, 1.0f,
                false, skin);
        workersSlider.setValue(settings.workerCount());
        workersSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setWorkerCount(Math.round(workersSlider.getValue()));
                updateSimulationLabels();
            }
        });
        panel.add(workersSlider).height(22.0f).pad(0.0f, 10.0f, 6.0f, 10.0f).row();

        recycleLabel = new Label("", skin);
        panel.add(recycleLabel).pad(0.0f, 10.0f, 2.0f, 10.0f).row();
        Slider recycleSlider = new Slider(Box3DSampleSettings.MIN_RECYCLE_CENTIMETERS,
                Box3DSampleSettings.MAX_RECYCLE_CENTIMETERS, 0.1f, false, skin);
        recycleSlider.setValue(settings.recycleCentimeters());
        recycleSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setRecycleCentimeters(recycleSlider.getValue());
                updateSimulationLabels();
            }
        });
        panel.add(recycleSlider).height(22.0f).pad(0.0f, 10.0f, 6.0f, 10.0f).row();

        sleepCheckBox = createCheckBox("Sleep", settings.sleepEnabled());
        sleepCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSleepEnabled(sleepCheckBox.isChecked());
            }
        });
        panel.add(sleepCheckBox).height(28.0f).pad(0.0f, 10.0f, 2.0f, 10.0f).row();

        warmStartingCheckBox = createCheckBox("Warm Starting", settings.warmStartingEnabled());
        warmStartingCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setWarmStartingEnabled(warmStartingCheckBox.isChecked());
            }
        });
        panel.add(warmStartingCheckBox).height(28.0f).pad(0.0f, 10.0f, 2.0f, 10.0f).row();

        continuousCheckBox = createCheckBox("Continuous", settings.continuousEnabled());
        continuousCheckBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setContinuousEnabled(continuousCheckBox.isChecked());
            }
        });
        panel.add(continuousCheckBox).height(28.0f).pad(0.0f, 10.0f, 8.0f, 10.0f).row();

        TextButton solverRestartButton = new TextButton("Restart", skin);
        solverRestartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resetTest();
            }
        });
        panel.add(solverRestartButton).height(30.0f).pad(0.0f, 10.0f, 10.0f, 10.0f).row();

        panel.add(new Label("Throw", skin)).pad(0.0f, 10.0f, 4.0f, 10.0f).row();
        launchShapeList = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(skin);
        launchShapeList.setItems(launchShapeNames());
        launchShapeList.setSelectedIndex(settings.launchShapeIndex());
        launchShapeList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setLaunchShapeIndex(launchShapeList.getSelectedIndex());
            }
        });
        ScrollPane launchShapeScrollPane = new ScrollPane(launchShapeList, skin);
        launchShapeScrollPane.setFadeScrollBars(false);
        panel.add(launchShapeScrollPane).width(286.0f).height(88.0f).pad(0.0f, 10.0f, 6.0f, 10.0f).row();

        launchSpeedLabel = new Label("", skin);
        panel.add(launchSpeedLabel).pad(0.0f, 10.0f, 2.0f, 10.0f).row();
        Slider launchSpeedSlider = new Slider(Box3DSampleSettings.MIN_LAUNCH_SPEED,
                Box3DSampleSettings.MAX_LAUNCH_SPEED, 1.0f, false, skin);
        launchSpeedSlider.setValue(settings.launchSpeed());
        launchSpeedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setLaunchSpeed(launchSpeedSlider.getValue());
                updateSimulationLabels();
            }
        });
        panel.add(launchSpeedSlider).height(22.0f).pad(0.0f, 10.0f, 6.0f, 10.0f).row();

        TextButton throwButton = new TextButton("Throw Shape", skin);
        throwButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                throwSelectedShape();
            }
        });
        panel.add(throwButton).height(30.0f).pad(0.0f, 10.0f, 10.0f, 10.0f).row();

        shadowBiasLabel = new Label(shadowBiasText(), skin);
        panel.add(shadowBiasLabel).pad(0.0f, 10.0f, 4.0f, 10.0f).row();
        shadowBiasSlider = new Slider(Box3DSampleSettings.MIN_SHADOW_BIAS,
                Box3DSampleSettings.MAX_SHADOW_BIAS, Box3DSampleSettings.SHADOW_BIAS_STEP, false, skin);
        shadowBiasSlider.setValue(settings.shadowBias());
        shadowBiasSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.settings().setShadowBias(shadowBiasSlider.getValue());
                applyShadowBias();
            }
        });
        panel.add(shadowBiasSlider).height(24.0f).pad(0.0f, 10.0f, 10.0f, 10.0f).row();

        panel.add(new Label("Render", skin)).pad(0.0f, 10.0f, 4.0f, 10.0f).row();
        debugVisualizationList = new com.badlogic.gdx.scenes.scene2d.ui.List<String>(skin);
        debugVisualizationList.setItems(debugVisualizationNames());
        debugVisualizationList.setSelectedIndex(debugVisualizationIndex);
        debugVisualizationList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!updatingDebugVisualizationSelection) {
                    debugVisualizationIndex = debugVisualizationList.getSelectedIndex();
                    applyDebugVisualization();
                }
            }
        });
        ScrollPane debugScrollPane = new ScrollPane(debugVisualizationList, skin);
        debugScrollPane.setFadeScrollBars(false);
        panel.add(debugScrollPane).width(286.0f).height(150.0f).pad(0.0f, 10.0f, 10.0f, 10.0f).row();

        updateSimulationLabels();
        updateInfoLabels(0.0f);

        root.add(createMenuBar()).colspan(3).growX().height(34.0f).top().left().row();

        ScrollPane selectorScrollPane = new ScrollPane(selectorPanel, skin);
        selectorScrollPane.setFadeScrollBars(false);
        ScrollPane panelScrollPane = new ScrollPane(panel, skin);
        panelScrollPane.setFadeScrollBars(false);
        root.add(selectorScrollPane).width(SELECTOR_HIT_WIDTH).growY().pad(8.0f).top().left();
        root.add().expand().fill();
        root.add(panelScrollPane).width(SETTINGS_HIT_WIDTH).growY().pad(8.0f).top().right();
        stage.addActor(root);
    }

    private Table createMenuBar() {
        Table menuBar = new Table();
        menuBar.setBackground(skin.newDrawable("white", new Color(0.035f, 0.04f, 0.055f, 0.94f)));
        menuBar.defaults().height(24.0f).pad(4.0f, 3.0f, 4.0f, 3.0f);
        menuBar.add(new Label("jBox3D", skin, "title")).padLeft(10.0f).padRight(10.0f);

        TextButton restartButton = new TextButton("Restart", skin);
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resetTest();
            }
        });
        menuBar.add(restartButton).width(82.0f);

        TextButton cameraButton = new TextButton("Camera", skin);
        cameraButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resetCamera();
            }
        });
        menuBar.add(cameraButton).width(78.0f);

        TextButton throwButton = new TextButton("Throw", skin);
        throwButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                throwSelectedShape();
            }
        });
        menuBar.add(throwButton).width(70.0f);

        menuBar.add().expandX().fillX();
        fpsLabel = new Label("FPS: 0", skin, "metric");
        menuBar.add(fpsLabel).padRight(10.0f);
        return menuBar;
    }

    private Array<String> sampleNames() {
        Array<String> names = new Array<String>();
        for(Box3DSampleEntry entry : controller.entries()) {
            names.add(entry.displayName());
        }
        return names;
    }

    private Array<String> debugVisualizationNames() {
        Array<String> names = new Array<String>();
        String[] labels = Box3DDebugVisualization.labels();
        for(int i = 0; i < labels.length; i++) {
            names.add(labels[i]);
        }
        return names;
    }

    private Array<String> launchShapeNames() {
        Array<String> names = new Array<String>();
        String[] labels = Box3DLaunchShape.labels();
        for(int i = 0; i < labels.length; i++) {
            names.add(labels[i]);
        }
        return names;
    }

    private void updateSimulationLabels() {
        Box3DSampleSettings settings = controller.settings();
        if(subStepsLabel != null) {
            subStepsLabel.setText("Sub-steps: " + settings.subStepCount());
        }
        if(hertzLabel != null) {
            hertzLabel.setText("Hertz: " + Math.round(settings.hertz()));
        }
        if(workersLabel != null) {
            workersLabel.setText("Workers: " + settings.workerCount());
        }
        if(recycleLabel != null) {
            recycleLabel.setText("Recycle: " + round(settings.recycleCentimeters(), 10.0f) + " cm");
        }
        if(launchSpeedLabel != null) {
            launchSpeedLabel.setText("Speed: " + Math.round(settings.launchSpeed()) + " m/s");
        }
    }

    private void throwSelectedShape() {
        if(camera == null || !controller.isReady()) {
            return;
        }
        Ray ray = camera.getPickRay(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
        throwShape(ray);
    }

    private void throwShape(Ray ray) {
        if(ray == null || !controller.isReady()) {
            return;
        }
        controller.launchShape(ray.origin.x, ray.origin.y, ray.origin.z, ray.direction.x, ray.direction.y,
                ray.direction.z);
    }

    private void updateSelectorSelection(Box3DSampleEntry entry) {
        if(activeSampleNameLabel != null) {
            activeSampleNameLabel.setText(entry.name());
        }
        if(activeSampleCategoryLabel != null) {
            activeSampleCategoryLabel.setText(entry.category());
        }
        if(sampleList != null) {
            updatingSelection = true;
            sampleList.setSelectedIndex(controller.selectedIndex());
            updatingSelection = false;
        }
    }

    private void updateDebugVisualizationSelection() {
        if(debugVisualizationList == null) {
            return;
        }
        updatingDebugVisualizationSelection = true;
        debugVisualizationList.setSelectedIndex(debugVisualizationIndex);
        updatingDebugVisualizationSelection = false;
    }

    private void applyDebugVisualization() {
        if(debugRenderer == null) {
            return;
        }
        Box3DDebugVisualization visualization = Box3DDebugVisualization.byIndex(debugVisualizationIndex);
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
        updateShadowBiasLabel();
    }

    private void updateShadowBiasLabel() {
        if(shadowBiasLabel != null) {
            shadowBiasLabel.setText(shadowBiasText());
        }
    }

    private String shadowBiasText() {
        return "Shadow Bias: " + round(controller.settings().shadowBias(), 1000.0f);
    }

    private CheckBox createCheckBox(String text, boolean checked) {
        CheckBox checkBox = new CheckBox(text, skin);
        checkBox.left();
        checkBox.getImageCell().width(18.0f).height(18.0f);
        checkBox.getLabelCell().padLeft(8.0f);
        checkBox.setChecked(checked);
        return checkBox;
    }

    private Skin createSkin() {
        Skin selectorSkin = new Skin();
        BitmapFont font = new BitmapFont();
        selectorSkin.add("default-font", font);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture white = new Texture(pixmap);
        pixmap.dispose();
        selectorSkin.add("white", white);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, new Color(0.93f, 0.95f, 0.97f, 1.0f));
        selectorSkin.add("default", labelStyle);
        selectorSkin.add("title", new Label.LabelStyle(font, new Color(0.86f, 0.70f, 0.32f, 1.0f)));
        selectorSkin.add("muted", new Label.LabelStyle(font, new Color(0.70f, 0.73f, 0.78f, 1.0f)));
        selectorSkin.add("metric", new Label.LabelStyle(font, new Color(0.38f, 0.70f, 0.62f, 1.0f)));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = new Color(0.93f, 0.95f, 0.97f, 1.0f);
        buttonStyle.up = selectorSkin.newDrawable("white", new Color(0.16f, 0.18f, 0.22f, 1.0f));
        buttonStyle.down = selectorSkin.newDrawable("white", new Color(0.24f, 0.28f, 0.34f, 1.0f));
        buttonStyle.over = selectorSkin.newDrawable("white", new Color(0.20f, 0.23f, 0.28f, 1.0f));
        selectorSkin.add("default", buttonStyle);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = font;
        checkBoxStyle.fontColor = new Color(0.93f, 0.95f, 0.97f, 1.0f);
        checkBoxStyle.checkboxOff = createCheckboxDrawable(selectorSkin, "checkbox-off",
                new Color(0.10f, 0.11f, 0.14f, 1.0f), new Color(0.30f, 0.34f, 0.40f, 1.0f), false);
        checkBoxStyle.checkboxOver = createCheckboxDrawable(selectorSkin, "checkbox-over",
                new Color(0.16f, 0.18f, 0.22f, 1.0f), new Color(0.38f, 0.42f, 0.50f, 1.0f), false);
        checkBoxStyle.checkboxOn = createCheckboxDrawable(selectorSkin, "checkbox-on",
                new Color(0.20f, 0.36f, 0.58f, 1.0f), new Color(0.45f, 0.58f, 0.78f, 1.0f), true);
        checkBoxStyle.checkboxOnOver = createCheckboxDrawable(selectorSkin, "checkbox-on-over",
                new Color(0.25f, 0.43f, 0.68f, 1.0f), new Color(0.52f, 0.66f, 0.86f, 1.0f), true);
        selectorSkin.add("default", checkBoxStyle);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = selectorSkin.newDrawable("white", new Color(0.10f, 0.11f, 0.14f, 1.0f));
        sliderStyle.knobBefore = selectorSkin.newDrawable("white", new Color(0.20f, 0.36f, 0.58f, 1.0f));
        sliderStyle.knob = selectorSkin.newDrawable("white", new Color(0.93f, 0.95f, 0.97f, 1.0f));
        sliderStyle.background.setMinHeight(4.0f);
        sliderStyle.knobBefore.setMinHeight(4.0f);
        sliderStyle.knob.setMinWidth(12.0f);
        sliderStyle.knob.setMinHeight(18.0f);
        selectorSkin.add("default-horizontal", sliderStyle);

        com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle listStyle =
                new com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorUnselected = new Color(0.86f, 0.88f, 0.91f, 1.0f);
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.selection = selectorSkin.newDrawable("white", new Color(0.20f, 0.36f, 0.58f, 1.0f));
        listStyle.background = selectorSkin.newDrawable("white", new Color(0.10f, 0.11f, 0.14f, 0.96f));
        selectorSkin.add("default", listStyle);

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.background = selectorSkin.newDrawable("white", new Color(0.10f, 0.11f, 0.14f, 0.96f));
        scrollStyle.vScroll = selectorSkin.newDrawable("white", new Color(0.08f, 0.09f, 0.11f, 1.0f));
        scrollStyle.vScrollKnob = selectorSkin.newDrawable("white", new Color(0.32f, 0.36f, 0.42f, 1.0f));
        selectorSkin.add("default", scrollStyle);

        return selectorSkin;
    }

    private Drawable createCheckboxDrawable(Skin skin, String name, Color fill, Color border, boolean checked) {
        Pixmap pixmap = new Pixmap(18, 18, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.0f, 0.0f, 0.0f, 0.0f);
        pixmap.fill();
        pixmap.setColor(fill);
        pixmap.fillRectangle(1, 1, 16, 16);
        pixmap.setColor(border);
        pixmap.drawRectangle(0, 0, 18, 18);
        pixmap.drawRectangle(1, 1, 16, 16);
        if(checked) {
            pixmap.setColor(0.92f, 0.95f, 1.0f, 1.0f);
            pixmap.drawLine(4, 9, 7, 12);
            pixmap.drawLine(4, 10, 7, 13);
            pixmap.drawLine(7, 12, 14, 4);
            pixmap.drawLine(7, 13, 14, 5);
        }
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        skin.add(name + "-texture", texture);
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        drawable.setMinWidth(18.0f);
        drawable.setMinHeight(18.0f);
        return drawable;
    }

    private void configureCamera(int width, int height, Box3DSampleCamera sampleCamera) {
        if(camera == null) {
            return;
        }
        updateCameraViewport(width, height);
        camera.near = 0.1f;
        camera.far = 200.0f;
        camera.position.set(sampleCamera.positionX, sampleCamera.positionY, sampleCamera.positionZ);
        camera.up.set(0.0f, 1.0f, 0.0f);
        camera.lookAt(sampleCamera.targetX, sampleCamera.targetY, sampleCamera.targetZ);
        camera.update();
        syncFlyAnglesFromCamera();
    }

    private void updateCameraViewport(int width, int height) {
        if(camera == null) {
            return;
        }
        camera.viewportWidth = Math.max(1, width);
        camera.viewportHeight = Math.max(1, height);
        camera.update();
    }

    private void updateControls(float deltaSeconds) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            resetTest();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            resetCamera();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            throwSelectedShape();
        }
        updateFlyCamera(Math.min(deltaSeconds, 1.0f / 30.0f));
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
        configureCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), controller.selectedEntry().camera());
    }

    private void updateFlyCamera(float deltaSeconds) {
        if(camera == null) {
            return;
        }
        boolean changed = updateFlyLook();
        changed |= updateFlyPosition(deltaSeconds);
        if(changed) {
            applyFlyAngles();
        }
    }

    private boolean updateFlyLook() {
        if(isPointerOverUi()) {
            return false;
        }
        boolean lookPressed = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        if(!lookPressed) {
            return false;
        }
        int deltaX = Gdx.input.getDeltaX();
        int deltaY = Gdx.input.getDeltaY();
        if(deltaX == 0 && deltaY == 0) {
            return false;
        }
        cameraYawRadians -= deltaX * FLY_LOOK_RADIANS_PER_PIXEL;
        cameraPitchRadians += deltaY * FLY_LOOK_RADIANS_PER_PIXEL;
        cameraPitchRadians = Math.max(-FLY_MAX_PITCH, Math.min(FLY_MAX_PITCH, cameraPitchRadians));
        return true;
    }

    private void updateBodyDrag() {
        if(camera == null || !controller.isReady()) {
            bodyDrag.end();
            dragButtonDown = false;
            throwClickPending = false;
            return;
        }

        boolean leftPressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean leftJustPressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        boolean justPressed = leftJustPressed || (leftPressed && !dragButtonDown);
        boolean justReleased = !leftPressed && dragButtonDown;
        boolean overUi = isPointerOverUi();
        boolean ctrlPressed = key(Input.Keys.CONTROL_LEFT, Input.Keys.CONTROL_RIGHT);
        if(leftJustPressed) {
            throwClickPending = !overUi && !ctrlPressed;
            throwClickX = Gdx.input.getX();
            throwClickY = Gdx.input.getY();
        }
        if(!leftPressed) {
            if((justReleased || leftJustPressed) && throwClickPending) {
                Ray ray = camera.getPickRay(throwClickX, throwClickY);
                throwShape(ray);
            }
            bodyDrag.end();
            dragButtonDown = false;
            throwClickPending = false;
            return;
        }

        if(justPressed && !leftJustPressed) {
            throwClickPending = !overUi && !ctrlPressed;
            throwClickX = Gdx.input.getX();
            throwClickY = Gdx.input.getY();
        }
        else if(throwClickPending && pointerMovedPastThrowClickSlop(Gdx.input.getX(), Gdx.input.getY())) {
            throwClickPending = false;
        }

        Ray ray = null;
        if(bodyDrag.isEnabled() && ctrlPressed && (!overUi || bodyDrag.isDragging())) {
            throwClickPending = false;
            ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
            boolean canStart = !bodyDrag.isDragging() && !overUi;
            if(canStart) {
                bodyDrag.begin(controller.world(), ray.origin.x, ray.origin.y, ray.origin.z, ray.direction.x,
                        ray.direction.y, ray.direction.z);
            }
            else {
                bodyDrag.updateTarget(ray.origin.x, ray.origin.y, ray.origin.z, ray.direction.x, ray.direction.y,
                        ray.direction.z);
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

    private boolean updateFlyPosition(float deltaSeconds) {
        flyMove.setZero();
        flyDirection.set(camera.direction).nor();
        flyRight.set((float)Math.cos(cameraYawRadians), 0.0f, -(float)Math.sin(cameraYawRadians)).nor();
        float speed = FLY_SPEED * Math.max(0.0f, deltaSeconds);
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            speed *= 2.5f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
            speed *= 2.0f;
        }
        if(key(Input.Keys.W, Input.Keys.UP)) {
            flyMove.mulAdd(flyDirection, speed);
        }
        if(key(Input.Keys.S, Input.Keys.DOWN)) {
            flyMove.mulAdd(flyDirection, -speed);
        }
        if(key(Input.Keys.D, Input.Keys.RIGHT)) {
            flyMove.mulAdd(flyRight, speed);
        }
        if(key(Input.Keys.A, Input.Keys.LEFT)) {
            flyMove.mulAdd(flyRight, -speed);
        }
        if(key(Input.Keys.E, Input.Keys.PAGE_UP)) {
            flyMove.y += speed;
        }
        if(key(Input.Keys.Q, Input.Keys.PAGE_DOWN)) {
            flyMove.y -= speed;
        }
        if(flyMove.isZero()) {
            return false;
        }
        camera.position.add(flyMove);
        return true;
    }

    private boolean key(int keyA, int keyB) {
        return Gdx.input.isKeyPressed(keyA) || Gdx.input.isKeyPressed(keyB);
    }

    private void updateFps(float deltaSeconds) {
        if(fpsLabel == null) {
            return;
        }
        fpsElapsed += Math.max(0.0f, deltaSeconds);
        fpsFrames++;
        if(fpsElapsed <= 0.000001f || (fpsHasValue && fpsElapsed < FPS_UPDATE_INTERVAL)) {
            return;
        }
        fpsLabel.setText("FPS: " + Math.round(fpsFrames / fpsElapsed));
        fpsHasValue = true;
        if(fpsElapsed >= FPS_UPDATE_INTERVAL) {
            fpsElapsed = 0.0f;
            fpsFrames = 0;
        }
    }

    private void updateInfoLabels(float deltaSeconds) {
        if(frameTimeLabel != null) {
            frameTimeLabel.setText(round(Math.max(0.0f, deltaSeconds) * 1000.0f, 10.0f) + " ms");
        }
        if(camera == null) {
            return;
        }
        if(cameraPoseLabel != null) {
            cameraPoseLabel.setText("camera m (" + round(camera.position.x, 10.0f) + ", "
                    + round(camera.position.y, 10.0f) + ", " + round(camera.position.z, 10.0f) + ")");
        }
        if(cameraAnglesLabel != null) {
            cameraAnglesLabel.setText("yaw/pitch (" + round((float)Math.toDegrees(cameraYawRadians), 10.0f)
                    + ", " + round((float)Math.toDegrees(cameraPitchRadians), 10.0f) + "), speed m/s "
                    + round(FLY_SPEED, 10.0f));
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
            Gdx.app.log("jBox3D", "Validated " + controller.entries().size() + " samples");
            Gdx.app.exit();
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

    private boolean isPointerOverUi() {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        return y < MENU_BAR_HIT_HEIGHT || x <= SELECTOR_HIT_WIDTH
                || x >= Gdx.graphics.getWidth() - SETTINGS_HIT_WIDTH;
    }

    private void syncFlyAnglesFromCamera() {
        flyDirection.set(camera.direction).nor();
        cameraYawRadians = (float)Math.atan2(-flyDirection.x, -flyDirection.z);
        cameraPitchRadians = (float)Math.asin(Math.max(-1.0f, Math.min(1.0f, -flyDirection.y)));
    }

    private void applyFlyAngles() {
        float cosPitch = (float)Math.cos(cameraPitchRadians);
        camera.direction.set(
                -(float)Math.sin(cameraYawRadians) * cosPitch,
                -(float)Math.sin(cameraPitchRadians),
                -(float)Math.cos(cameraYawRadians) * cosPitch).nor();
        camera.up.set(0.0f, 1.0f, 0.0f);
        camera.update();
    }

    private void writeScreenshotIfRequested() {
        if(screenshotWritten || screenshotPath == null || screenshotPath.length() == 0 || !controller.isReady()
                || controller.renderedFrames() < screenshotAfterFrames) {
            return;
        }

        Pixmap pixmap = Pixmap.createFromFrameBuffer(0, 0, Gdx.graphics.getBackBufferWidth(),
                Gdx.graphics.getBackBufferHeight());
        Pixmap flipped = flipVertically(pixmap);
        PixmapIO.writePNG(Gdx.files.absolute(screenshotPath), flipped);
        flipped.dispose();
        pixmap.dispose();
        screenshotWritten = true;
        Gdx.app.log("jBox3D", "Wrote screenshot: " + screenshotPath);
    }

    private Pixmap flipVertically(Pixmap source) {
        Pixmap flipped = new Pixmap(source.getWidth(), source.getHeight(), source.getFormat());
        for(int y = 0; y < source.getHeight(); y++) {
            flipped.drawPixmap(source, 0, y, source.getWidth(), 1, 0, source.getHeight() - y - 1,
                    source.getWidth(), 1);
        }
        return flipped;
    }
}
