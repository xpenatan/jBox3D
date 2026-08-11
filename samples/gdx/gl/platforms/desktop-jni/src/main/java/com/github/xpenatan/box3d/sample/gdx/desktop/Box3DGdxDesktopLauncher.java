package com.github.xpenatan.box3d.sample.gdx.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleApplication;
import com.github.xpenatan.box3d.sample.gdx.gl.GdxGlSampleBackend;

public final class Box3DGdxDesktopLauncher {
    private Box3DGdxDesktopLauncher() {
    }

    public static void main(String[] args) {
        long exitAfterFrames = Long.parseLong(option(args, "--exit-after-frames=",
                System.getProperty("jbox3d.sample.exitAfterFrames", "0")));

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL30, 3, 3);
        config.setTitle("jBox3D libGDX jni");
        config.setWindowedMode(960, 540);
        // Match the original Box3D sample's swap_interval = 0 so benchmark samples
        // expose renderer throughput instead of stopping at the display refresh rate.
        config.useVsync(false);
        config.setForegroundFPS(0);

        int workerCount = recommendedWorkerCount();
        new Lwjgl3Application(
                new Box3DGdxSampleApplication(new GdxGlSampleBackend(), exitAfterFrames, workerCount), config);
    }

    private static int recommendedWorkerCount() {
        return Math.max(1, Math.min(Runtime.getRuntime().availableProcessors() / 2, 8));
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
}
