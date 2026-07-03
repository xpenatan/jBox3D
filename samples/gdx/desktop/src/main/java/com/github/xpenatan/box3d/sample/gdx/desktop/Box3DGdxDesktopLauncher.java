package com.github.xpenatan.box3d.sample.gdx.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleApplication;

public final class Box3DGdxDesktopLauncher {
    private Box3DGdxDesktopLauncher() {
    }

    public static void main(String[] args) {
        long exitAfterFrames = Long.parseLong(option(args, "--exit-after-frames=",
                System.getProperty("jbox3d.sample.exitAfterFrames", "0")));

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("jBox3D libGDX");
        config.setWindowedMode(960, 540);
        config.useVsync(true);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new Box3DGdxSampleApplication(exitAfterFrames), config);
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
