package com.github.xpenatan.box3d.sample.gdx.wgpu.desktop;

import com.github.xpenatan.box3d.sample.gdx.Box3DGdxSampleApplication;
import com.github.xpenatan.box3d.sample.gdx.wgpu.GdxWgpuSampleBackend;
import com.github.xpenatan.webgpu.JWebGPUBackend;
import com.monstrous.gdx.webgpu.backends.desktop.WgDesktopApplication;
import com.monstrous.gdx.webgpu.backends.desktop.WgDesktopApplicationConfiguration;

public final class Box3DGdxWgpuDesktopLauncher {
    private Box3DGdxWgpuDesktopLauncher() {
    }

    public static void main(String[] args) {
        long exitAfterFrames = Long.parseLong(option(args, "--exit-after-frames=",
                System.getProperty("jbox3d.sample.exitAfterFrames", "0")));

        WgDesktopApplicationConfiguration config = new WgDesktopApplicationConfiguration();
        config.setTitle("jBox3D libGDX WebGPU FFM");
        config.setWindowedMode(960, 540);
        config.setWindowPosition(80, 80);
        config.backendWebGPU = JWebGPUBackend.WGPU;
        config.useVsync(true);
        config.setForegroundFPS(60);
        config.samples = 1;

        new WgDesktopApplication(new Box3DGdxSampleApplication(new GdxWgpuSampleBackend(), exitAfterFrames), config);
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
