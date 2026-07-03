package com.github.xpenatan.box3d.sample.fdx.web;

import com.github.xpenatan.box3d.sample.fdx.Box3DFdxSampleApplication;
import io.github.libfdx.backend.web.WebApplicationBackend;
import io.github.libfdx.backend.web.WebApplicationConfig;
import io.github.libfdx.graphics.gl.web.WebGLProvider;
import io.github.libfdx.graphics.wgpu.WebWGPUProvider;

final class Box3DFdxWebLauncherSupport {
    private static final String CANVAS_ID = "libfdx-canvas";

    private Box3DFdxWebLauncherSupport() {
    }

    static void start(String runtimeName, String[] args) {
        boolean webgpu = isWebGPU(option(args, "--graphics=", "webgl"));
        long exitAfterFrames = Long.parseLong(option(args, "--exit-after-frames=", "0"));
        applySampleOptions(args);

        String graphicsName = webgpu ? "WebGPU" : "WebGL";
        WebApplicationConfig config = new WebApplicationConfig()
                .title("jBox3D libfdx - " + graphicsName + " " + runtimeName)
                .size(0, 0)
                .canvasId(CANVAS_ID);
        if(webgpu) {
            config.graphics(new WebWGPUProvider());
        }
        else {
            config.graphics(new WebGLProvider());
        }

        new WebApplicationBackend().start(config, new Box3DFdxSampleApplication(exitAfterFrames));
    }

    private static void applySampleOptions(String[] args) {
        setPropertyFromOption(args, "--sample=", "jbox3d.sample.sample");
        setPropertyFromOption(args, "--sample-index=", "jbox3d.sample.sampleIndex");
    }

    private static void setPropertyFromOption(String[] args, String prefix, String property) {
        String value = option(args, prefix, "");
        if(value.length() > 0) {
            System.setProperty(property, value);
        }
    }

    private static boolean isWebGPU(String graphics) {
        return "webgpu".equalsIgnoreCase(graphics) || "wgpu".equalsIgnoreCase(graphics);
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
