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
import com.badlogic.gdx.utils.Align;
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
    private static final int SETTINGS_HIT_WIDTH = 326;
    private static final int MENU_BAR_HIT_HEIGHT = 38;
    private static final float SAMPLE_MENU_WIDTH = 430.0f;
    private static final float SAMPLE_MENU_MAX_HEIGHT = 560.0f;
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
    private ScrollPane settingsScrollPane;
    private Table sampleMenuPopup;
    private TextButton samplesMenuButton;
    private com.badlogic.gdx.scenes.scene2d.ui.List<String> launchShapeList;
    private com.badlogic.gdx.scenes.scene2d.ui.List<String> debugVisualizationList;
    private CheckBox sleepCheckBox;
    private CheckBox warmStartingCheckBox;
    private CheckBox continuousCheckBox;
    private float cameraYawRadians;
    private float cameraPitchRadians;
    private float fpsElapsed;
    private int fpsFrames;
    private int debugVisualizationIndex = resolveInitialDebugVisualizationIndex();
    private boolean fpsHasValue;
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
    private boolean initialScrollAligned;

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
            positionSampleMenuPopup();
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
            alignInitialScrollPanes();
            Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
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

        Table panel = new Table();
        panel.setBackground(skin.getDrawable("box3d-panel"));
        panel.top();
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

        root.add(createMenuBar()).colspan(2).growX().height(34.0f).top().left().row();

        settingsScrollPane = new ScrollPane(panel, skin);
        settingsScrollPane.setFadeScrollBars(false);
        settingsScrollPane.setScrollingDisabled(true, false);

        root.add().expand().fill();
        root.add(settingsScrollPane).width(SETTINGS_HIT_WIDTH).growY().pad(8.0f).top().right();
        stage.addActor(root);

        sampleMenuPopup = new Table();
        sampleMenuPopup.setBackground(skin.getDrawable("box3d-panel"));
        sampleMenuPopup.top().left();
        sampleMenuPopup.setVisible(false);
        stage.addActor(sampleMenuPopup);
        rebuildSampleMenuPopup();
        if(shouldOpenSampleMenuInitially()) {
            sampleMenuPopup.setVisible(true);
        }
    }

    private void alignInitialScrollPanes() {
        if(initialScrollAligned) {
            return;
        }
        alignScrollPaneToTop(settingsScrollPane);
        initialScrollAligned = true;
    }

    private void alignScrollPaneToTop(ScrollPane scrollPane) {
        if(scrollPane == null) {
            return;
        }
        scrollPane.validate();
        scrollPane.setScrollY(0.0f);
        scrollPane.updateVisualScroll();
    }

    private Table createMenuBar() {
        Table menuBar = new Table();
        menuBar.setBackground(skin.getDrawable("box3d-menu"));
        menuBar.defaults().height(24.0f).pad(4.0f, 3.0f, 4.0f, 3.0f);
        menuBar.add(new Label("jBox3D", skin, "title")).padLeft(10.0f).padRight(10.0f);

        samplesMenuButton = new TextButton("Samples", skin);
        samplesMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleSampleMenuPopup();
            }
        });
        menuBar.add(samplesMenuButton).width(92.0f);

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

    private void toggleSampleMenuPopup() {
        if(sampleMenuPopup == null) {
            return;
        }
        if(sampleMenuPopup.isVisible()) {
            sampleMenuPopup.setVisible(false);
            return;
        }
        rebuildSampleMenuPopup();
        sampleMenuPopup.setVisible(true);
    }

    private void rebuildSampleMenuPopup() {
        if(sampleMenuPopup == null || stage == null) {
            return;
        }

        float stageHeight = stage.getViewport().getWorldHeight();
        float popupHeight = Math.max(180.0f,
                Math.min(SAMPLE_MENU_MAX_HEIGHT, stageHeight - MENU_BAR_HIT_HEIGHT - 12.0f));
        float contentHeight = Math.max(120.0f, popupHeight - 18.0f);

        Table content = new Table();
        content.top().left();
        content.defaults().left().growX();

        Array<String> categories = sampleCategories();
        for(int categoryIndex = 0; categoryIndex < categories.size; categoryIndex++) {
            String category = categories.get(categoryIndex);
            if(categoryIndex > 0) {
                content.add().height(6.0f).row();
            }
            Label categoryLabel = new Label(category, skin, "title");
            content.add(categoryLabel).pad(8.0f, 10.0f, 4.0f, 10.0f).row();
            for(int i = 0; i < controller.entries().size(); i++) {
                Box3DSampleEntry entry = controller.entries().get(i);
                if(!entry.category().equals(category)) {
                    continue;
                }
                content.add(createSampleMenuItem(entry, i)).height(24.0f)
                        .pad(0.0f, 10.0f, 2.0f, 10.0f).row();
            }
        }

        ScrollPane sampleScrollPane = new ScrollPane(content, skin);
        sampleScrollPane.setFadeScrollBars(false);
        sampleScrollPane.setScrollingDisabled(true, false);

        sampleMenuPopup.clearChildren();
        sampleMenuPopup.add(sampleScrollPane).width(SAMPLE_MENU_WIDTH - 12.0f).height(contentHeight)
                .pad(6.0f).row();
        sampleMenuPopup.setSize(SAMPLE_MENU_WIDTH, popupHeight);
        positionSampleMenuPopup();
    }

    private TextButton createSampleMenuItem(Box3DSampleEntry entry, int sampleIndex) {
        boolean selected = sampleIndex == controller.selectedIndex();
        TextButton item = new TextButton(entry.name(), skin, selected ? "selected" : "default");
        item.getLabel().setAlignment(Align.left);
        item.getLabelCell().left().padLeft(8.0f);
        item.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.selectSample(sampleIndex);
                if(sampleMenuPopup != null) {
                    sampleMenuPopup.setVisible(false);
                }
            }
        });
        return item;
    }

    private Array<String> sampleCategories() {
        Array<String> categories = new Array<String>();
        for(Box3DSampleEntry entry : controller.entries()) {
            if(!containsCategory(categories, entry.category())) {
                categories.add(entry.category());
            }
        }
        return categories;
    }

    private boolean containsCategory(Array<String> categories, String category) {
        for(int i = 0; i < categories.size; i++) {
            if(categories.get(i).equals(category)) {
                return true;
            }
        }
        return false;
    }

    private void positionSampleMenuPopup() {
        if(sampleMenuPopup == null || stage == null) {
            return;
        }
        float stageHeight = stage.getViewport().getWorldHeight();
        sampleMenuPopup.setPosition(8.0f,
                Math.max(8.0f, stageHeight - MENU_BAR_HIT_HEIGHT - sampleMenuPopup.getHeight() - 4.0f));
    }

    private boolean shouldOpenSampleMenuInitially() {
        return Boolean.parseBoolean(System.getProperty("jbox3d.sample.openSamplesMenu", "false"));
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
        if(sampleMenuPopup != null && sampleMenuPopup.isVisible()) {
            rebuildSampleMenuPopup();
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
        Skin selectorSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
        BitmapFont font = selectorSkin.getFont("default-font");

        addUiDrawable(selectorSkin, "panel", "box3d-panel", 1.0f, 1.0f);
        addUiDrawable(selectorSkin, "menu", "box3d-menu", 1.0f, 1.0f);
        addUiDrawable(selectorSkin, "button-up", "box3d-button-up", 1.0f, 1.0f);
        addUiDrawable(selectorSkin, "button-down", "box3d-button-down", 1.0f, 1.0f);
        addUiDrawable(selectorSkin, "button-over", "box3d-button-over", 1.0f, 1.0f);
        addUiDrawable(selectorSkin, "list-bg", "box3d-list-bg", 1.0f, 1.0f);
        addUiDrawable(selectorSkin, "selection", "box3d-selection", 1.0f, 1.0f);
        addUiDrawable(selectorSkin, "scroll", "box3d-scroll", 1.0f, 1.0f);
        addUiDrawable(selectorSkin, "scroll-knob", "box3d-scroll-knob", 12.0f, 12.0f);
        addUiDrawable(selectorSkin, "slider-bg", "box3d-slider-bg", 1.0f, 4.0f);
        addUiDrawable(selectorSkin, "slider-fill", "box3d-slider-fill", 1.0f, 4.0f);
        addUiDrawable(selectorSkin, "slider-knob", "box3d-slider-knob", 12.0f, 18.0f);
        addUiDrawable(selectorSkin, "checkbox-off", "box3d-checkbox-off", 18.0f, 18.0f);
        addUiDrawable(selectorSkin, "checkbox-over", "box3d-checkbox-over", 18.0f, 18.0f);
        addUiDrawable(selectorSkin, "checkbox-on", "box3d-checkbox-on", 18.0f, 18.0f);
        addUiDrawable(selectorSkin, "checkbox-on-over", "box3d-checkbox-on-over", 18.0f, 18.0f);

        selectorSkin.add("default", new Label.LabelStyle(font, new Color(0.93f, 0.95f, 0.97f, 1.0f)));
        selectorSkin.add("title", new Label.LabelStyle(font, new Color(0.86f, 0.70f, 0.32f, 1.0f)));
        selectorSkin.add("muted", new Label.LabelStyle(font, new Color(0.70f, 0.73f, 0.78f, 1.0f)));
        selectorSkin.add("metric", new Label.LabelStyle(font, new Color(0.38f, 0.70f, 0.62f, 1.0f)));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = new Color(0.93f, 0.95f, 0.97f, 1.0f);
        buttonStyle.up = selectorSkin.getDrawable("box3d-button-up");
        buttonStyle.down = selectorSkin.getDrawable("box3d-button-down");
        buttonStyle.over = selectorSkin.getDrawable("box3d-button-over");
        selectorSkin.add("default", buttonStyle);

        TextButton.TextButtonStyle selectedButtonStyle = new TextButton.TextButtonStyle();
        selectedButtonStyle.font = font;
        selectedButtonStyle.fontColor = Color.WHITE;
        selectedButtonStyle.up = selectorSkin.getDrawable("box3d-selection");
        selectedButtonStyle.down = selectorSkin.getDrawable("box3d-button-down");
        selectedButtonStyle.over = selectorSkin.getDrawable("box3d-button-over");
        selectorSkin.add("selected", selectedButtonStyle);

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = font;
        checkBoxStyle.fontColor = new Color(0.93f, 0.95f, 0.97f, 1.0f);
        checkBoxStyle.checkboxOff = selectorSkin.getDrawable("box3d-checkbox-off");
        checkBoxStyle.checkboxOver = selectorSkin.getDrawable("box3d-checkbox-over");
        checkBoxStyle.checkboxOn = selectorSkin.getDrawable("box3d-checkbox-on");
        checkBoxStyle.checkboxOnOver = selectorSkin.getDrawable("box3d-checkbox-on-over");
        selectorSkin.add("default", checkBoxStyle);

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = selectorSkin.getDrawable("box3d-slider-bg");
        sliderStyle.knobBefore = selectorSkin.getDrawable("box3d-slider-fill");
        sliderStyle.knob = selectorSkin.getDrawable("box3d-slider-knob");
        selectorSkin.add("default-horizontal", sliderStyle);

        com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle listStyle =
                new com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle();
        listStyle.font = font;
        listStyle.fontColorUnselected = new Color(0.86f, 0.88f, 0.91f, 1.0f);
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.selection = selectorSkin.getDrawable("box3d-selection");
        listStyle.background = selectorSkin.getDrawable("box3d-list-bg");
        selectorSkin.add("default", listStyle);

        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.background = selectorSkin.getDrawable("box3d-list-bg");
        scrollStyle.vScroll = selectorSkin.getDrawable("box3d-scroll");
        scrollStyle.vScrollKnob = selectorSkin.getDrawable("box3d-scroll-knob");
        scrollStyle.hScroll = selectorSkin.getDrawable("box3d-scroll");
        scrollStyle.hScrollKnob = selectorSkin.getDrawable("box3d-scroll-knob");
        selectorSkin.add("default", scrollStyle);

        return selectorSkin;
    }

    private Drawable addUiDrawable(Skin skin, String fileName, String name, float minWidth, float minHeight) {
        Texture texture = new Texture(Gdx.files.internal("data/ui/" + fileName + ".png"));
        skin.add(name + "-texture", texture);
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        drawable.setMinWidth(minWidth);
        drawable.setMinHeight(minHeight);
        skin.add(name, drawable, Drawable.class);
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
        if(sampleMenuPopup != null && sampleMenuPopup.isVisible() && Gdx.input.justTouched() && !isPointerOverUi()) {
            sampleMenuPopup.setVisible(false);
        }
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

    private static int resolveInitialDebugVisualizationIndex() {
        String index = System.getProperty("jbox3d.sample.debugVisualizationIndex");
        if(index != null && index.trim().length() > 0) {
            try {
                return Math.max(0,
                        Math.min(Integer.parseInt(index.trim()), Box3DDebugVisualization.values().length - 1));
            }
            catch(NumberFormatException ignored) {
            }
        }

        String name = System.getProperty("jbox3d.sample.debugVisualization");
        if(name == null || name.trim().length() == 0) {
            return Box3DDebugVisualization.ALL.index();
        }
        String normalized = normalize(name);
        Box3DDebugVisualization[] values = Box3DDebugVisualization.values();
        for(int i = 0; i < values.length; i++) {
            if(normalize(values[i].label()).equals(normalized) || normalize(values[i].name()).equals(normalized)) {
                return values[i].index();
            }
        }
        return Box3DDebugVisualization.ALL.index();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.toLowerCase().replace(" ", "").replace("_", "").replace("-", "").trim();
    }

    private static float round(float value, float scale) {
        return Math.round(value * scale) / scale;
    }

    private boolean isPointerOverUi() {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        if(y < MENU_BAR_HIT_HEIGHT || x >= Gdx.graphics.getWidth() - SETTINGS_HIT_WIDTH) {
            return true;
        }
        if(sampleMenuPopup == null || !sampleMenuPopup.isVisible()) {
            return false;
        }
        float stageY = Gdx.graphics.getHeight() - y;
        return x >= sampleMenuPopup.getX() && x <= sampleMenuPopup.getX() + sampleMenuPopup.getWidth()
                && stageY >= sampleMenuPopup.getY() && stageY <= sampleMenuPopup.getY() + sampleMenuPopup.getHeight();
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
