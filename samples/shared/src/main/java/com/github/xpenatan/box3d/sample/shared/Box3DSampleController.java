package com.github.xpenatan.box3d.sample.shared;

import com.github.xpenatan.box3d.JBox3DLoader;
import com.github.xpenatan.box3d.B3World;
import com.github.xpenatan.box3d.sample.shared.samples.Box3DSampleRegistry;
import java.util.List;

public final class Box3DSampleController {
    private static final float MAX_ACCUMULATED_FRAME_TIME = 0.25f;
    private static final int MAX_STEPS_PER_FRAME = 8;

    private final Box3DSampleHost host;
    private final long exitAfterFrames;
    private final List<Box3DSampleEntry> entries;
    private final Box3DSampleSettings settings = new Box3DSampleSettings();
    private Box3DSample sample;
    private volatile boolean box3dLoaded;
    private volatile Throwable box3dLoadError;
    private boolean runtimeInitStarted;
    private int selectedIndex;
    private long renderedFrames;
    private float stepAccumulator;
    private Box3DSampleStepListener stepListener;

    public Box3DSampleController(Box3DSampleHost host, long exitAfterFrames) {
        if(host == null) {
            throw new IllegalArgumentException("Box3DSampleHost cannot be null");
        }
        this.host = host;
        this.exitAfterFrames = exitAfterFrames;
        entries = Box3DSampleRegistry.entries();
        selectedIndex = resolveInitialSampleIndex();
    }

    public void create() {
        if(runtimeInitStarted) {
            return;
        }
        runtimeInitStarted = true;
        JBox3DLoader.init((success, throwable) -> {
            if(success) {
                box3dLoaded = true;
            }
            else {
                box3dLoadError = throwable != null ? throwable
                        : new IllegalStateException("jBox3D runtime loader returned failure");
            }
        });
    }

    public void render(float deltaSeconds) {
        ensureSampleReady();
        if(sample != null) {
            stepSample(deltaSeconds);
            host.renderBox3D(sample.world());
        }

        renderedFrames++;
        if(exitAfterFrames > 0L && renderedFrames >= exitAfterFrames) {
            host.requestExit();
        }
    }

    public void selectSample(int index) {
        int nextIndex = Math.max(0, Math.min(index, entries.size() - 1));
        if(selectedIndex == nextIndex && sample != null) {
            return;
        }
        selectedIndex = nextIndex;
        if(box3dLoaded) {
            createSelectedSample();
        }
    }

    public void restartSample() {
        if(box3dLoaded) {
            createSelectedSample();
        }
    }

    public void launchShape(float originX, float originY, float originZ, float directionX, float directionY,
            float directionZ) {
        if(sample == null) {
            return;
        }
        float length = (float)Math.sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);
        if(length <= 0.000001f) {
            return;
        }
        sample.launchShape(settings.launchShape(), originX, originY, originZ, directionX / length, directionY / length,
                directionZ / length, settings.launchSpeed());
    }

    public void setStepListener(Box3DSampleStepListener stepListener) {
        this.stepListener = stepListener;
    }

    public List<Box3DSampleEntry> entries() {
        return entries;
    }

    public int selectedIndex() {
        return selectedIndex;
    }

    public Box3DSampleEntry selectedEntry() {
        return entries.get(selectedIndex);
    }

    public Box3DSampleSettings settings() {
        return settings;
    }

    public long renderedFrames() {
        return renderedFrames;
    }

    public boolean isReady() {
        return sample != null;
    }

    public B3World world() {
        return sample != null ? sample.world() : null;
    }

    public void dispose() {
        disposeSample();
    }

    private void ensureSampleReady() {
        if(sample != null) {
            return;
        }
        if(box3dLoadError != null) {
            throw new IllegalStateException("Failed to load the jBox3D runtime", box3dLoadError);
        }
        if(box3dLoaded) {
            createSelectedSample();
        }
    }

    private void createSelectedSample() {
        disposeSample();
        stepAccumulator = 0.0f;
        Box3DSampleEntry entry = selectedEntry();
        sample = entry.create();
        host.onSampleChanged(entry, sample);
    }

    private void stepSample(float deltaSeconds) {
        float step = 1.0f / settings.hertz();
        stepAccumulator += Math.max(0.0f, Math.min(deltaSeconds, MAX_ACCUMULATED_FRAME_TIME));

        int stepCount = 0;
        while(stepAccumulator >= step && stepCount < MAX_STEPS_PER_FRAME) {
            if(stepListener != null) {
                stepListener.beforePhysicsStep(step);
            }
            sample.step(step, settings);
            stepAccumulator -= step;
            stepCount++;
        }

        if(stepCount == MAX_STEPS_PER_FRAME && stepAccumulator >= step) {
            stepAccumulator = step;
        }
    }

    private void disposeSample() {
        if(sample != null) {
            sample.dispose();
            sample = null;
        }
    }

    private int resolveInitialSampleIndex() {
        String indexProperty = System.getProperty("jbox3d.sample.sampleIndex");
        if(indexProperty != null && indexProperty.trim().length() > 0) {
            try {
                return Math.max(0, Math.min(Integer.parseInt(indexProperty.trim()), entries.size() - 1));
            }
            catch(NumberFormatException ignored) {
            }
        }

        String sampleProperty = System.getProperty("jbox3d.sample.sample");
        if(sampleProperty == null || sampleProperty.trim().length() == 0) {
            return 0;
        }

        String requested = normalize(sampleProperty);
        for(int i = 0; i < entries.size(); i++) {
            Box3DSampleEntry entry = entries.get(i);
            if(normalize(entry.displayName()).equals(requested)
                    || normalize(entry.name()).equals(requested)
                    || normalize(entry.category() + "/" + entry.name()).equals(requested)) {
                return i;
            }
        }
        return 0;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.toLowerCase().replace(" ", "").replace("_", "").replace("-", "")
                .replace("\\", "/").trim();
    }
}
