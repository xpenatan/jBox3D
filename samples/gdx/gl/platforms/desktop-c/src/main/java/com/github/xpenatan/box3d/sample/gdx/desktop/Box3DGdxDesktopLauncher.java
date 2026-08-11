package com.github.xpenatan.box3d.sample.gdx.desktop;

import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleApplication;
import com.github.xpenatan.box3d.sample.gdx.gl.GdxGlSampleBackend;
import com.github.xpenatan.gdx.teavm.backends.glfw.GLFWApplication;
import com.github.xpenatan.gdx.teavm.backends.glfw.GLFWApplicationConfiguration;

public final class Box3DGdxDesktopLauncher {
    private Box3DGdxDesktopLauncher() {
    }

    public static void main(String[] args) {
        long exitAfterFrames = Long.parseLong(option(args, "--exit-after-frames=",
                System.getProperty("jbox3d.sample.exitAfterFrames", "0")));
        int workerCount = Integer.parseInt(option(args, "--workers=", "8"));
        setPropertyFromOption(args, "--sample=", "jbox3d.sample.sample");
        setPropertyFromOption(args, "--sample-index=", "jbox3d.sample.sampleIndex");
        setPropertyFromOption(args, "--validate-all=", "jbox3d.sample.validateAll");
        setPropertyFromOption(args, "--auto-throw-after-frames=", "jbox3d.sample.autoThrowAfterFrames");
        setPropertyFromOption(args, "--screenshot=", "jbox3d.sample.screenshot");
        setPropertyFromOption(args, "--screenshot-after-frames=", "jbox3d.sample.screenshotAfterFrames");
        setPropertyFromOption(args, "--debug-visualization=", "jbox3d.sample.debugVisualization");
        setPropertyFromOption(args, "--debug-visualization-index=", "jbox3d.sample.debugVisualizationIndex");

        GLFWApplicationConfiguration config = new GLFWApplicationConfiguration();
        config.setOpenGLEmulation(GLFWApplicationConfiguration.GLEmulation.GL30, 3, 3);
        config.setTitle("jBox3D libGDX c");
        config.setWindowedMode(960, 540);
        config.useVsync(false);
        config.setForegroundFPS(0);
        System.setProperty("os.name", "Windows");
        if(Boolean.parseBoolean(option(args, "--open-samples-menu=", "false"))) {
            System.setProperty("jbox3d.sample.openSamplesMenu", "true");
        }

        new GLFWApplication(
                new Box3DGdxSampleApplication(new GdxGlSampleBackend(), exitAfterFrames, workerCount), config);
    }

    private static String option(String[] args, String prefix, String fallback) {
        if(args == null) {
            return fallback;
        }
        for(String arg : args) {
            if(arg != null && arg.startsWith(prefix)) {
                return arg.substring(prefix.length());
            }
        }
        return fallback;
    }

    private static void setPropertyFromOption(String[] args, String prefix, String propertyName) {
        String value = option(args, prefix, null);
        if(value != null) {
            System.setProperty(propertyName, value);
        }
    }
}
