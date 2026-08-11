package com.github.xpenatan.box3d.sample.fdx.desktop;

import com.github.xpenatan.box3d.sample.fdx.Box3DFdxSampleApplication;
import io.github.libfdx.backend.desktop.DesktopApplicationBackend;
import io.github.libfdx.backend.desktop.DesktopApplicationConfig;
import io.github.libfdx.backend.desktop.DesktopOpenGLProvider;
import io.github.libfdx.backend.desktop.DesktopVulkanProvider;
import io.github.libfdx.graphics.GraphicsAttachmentProvider;
import io.github.libfdx.graphics.d3d12.D3D12Provider;
import io.github.libfdx.graphics.wgpu.WGPUProvider;

public final class Box3DFdxDesktopLauncher {
    private Box3DFdxDesktopLauncher() {
    }

    public static void main(String[] args) {
        String graphics = option(args, "--graphics=", System.getProperty("jbox3d.sample.graphics", "gl"));
        String label = option(args, "--graphics-label=", System.getProperty("jbox3d.sample.graphicsLabel"));
        boolean visible = Boolean.parseBoolean(option(args, "--visible=",
                System.getProperty("jbox3d.sample.visible", "true")));
        long exitAfterFrames = Long.parseLong(option(args, "--exit-after-frames=",
                System.getProperty("jbox3d.sample.exitAfterFrames", "0")));

        DesktopApplicationConfig config = new DesktopApplicationConfig()
                .title("jBox3D libFDX - " + graphicsDisplayName(graphics, label) + " jni")
                .size(960, 540)
                .visible(visible)
                .vSync(true)
                .foregroundFps(60)
                .graphics(graphicsProvider(graphics));

        int workerCount = recommendedWorkerCount();
        new DesktopApplicationBackend().start(config, new Box3DFdxSampleApplication(exitAfterFrames, workerCount));
    }

    private static int recommendedWorkerCount() {
        String configured = System.getProperty("jbox3d.sample.workers");
        if(configured != null && !configured.trim().isEmpty()) {
            return Math.max(1, Math.min(Integer.parseInt(configured.trim()), 8));
        }
        return Math.max(1, Math.min(Runtime.getRuntime().availableProcessors() / 2, 8));
    }

    private static GraphicsAttachmentProvider graphicsProvider(String graphics) {
        if("vulkan".equalsIgnoreCase(graphics) || "vk".equalsIgnoreCase(graphics)) {
            return new DesktopVulkanProvider();
        }
        if("wgpu".equalsIgnoreCase(graphics) || "webgpu".equalsIgnoreCase(graphics)) {
            return new WGPUProvider();
        }
        if(isD3D12(graphics)) {
            return new D3D12Provider();
        }
        return new DesktopOpenGLProvider();
    }

    private static String graphicsDisplayName(String graphics, String configured) {
        if(configured != null && configured.trim().length() > 0) {
            return configured.trim();
        }
        if("vulkan".equalsIgnoreCase(graphics) || "vk".equalsIgnoreCase(graphics)) {
            return "Vulkan";
        }
        if("wgpu".equalsIgnoreCase(graphics) || "webgpu".equalsIgnoreCase(graphics)) {
            return "WGPU";
        }
        if(isD3D12(graphics)) {
            return "Direct3D 12";
        }
        return "OpenGL";
    }

    private static boolean isD3D12(String graphics) {
        return "d3d12".equalsIgnoreCase(graphics)
                || "direct3d12".equalsIgnoreCase(graphics)
                || "directx12".equalsIgnoreCase(graphics)
                || "dx12".equalsIgnoreCase(graphics);
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
