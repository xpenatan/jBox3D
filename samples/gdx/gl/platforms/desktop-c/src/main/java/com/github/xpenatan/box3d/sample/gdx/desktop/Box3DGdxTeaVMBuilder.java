package com.github.xpenatan.box3d.sample.gdx.desktop;

import com.github.xpenatan.gdx.teavm.backends.glfw.config.backend.TeaGLFWBackend;
import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.builder.TeaBuilder;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.teavm.vm.TeaVMOptimizationLevel;

public final class Box3DGdxTeaVMBuilder {
    private static final int NATIVE_MIN_HEAP_SIZE = 64 * 1024 * 1024;
    private static final int NATIVE_MAX_HEAP_SIZE = 512 * 1024 * 1024;
    private static final int NATIVE_MIN_DIRECT_BUFFER_SIZE = 64 * 1024 * 1024;

    private Box3DGdxTeaVMBuilder() {
    }

    public static void main(String[] args) throws IOException {
        TeaGLFWBackend.NativeBuildType buildType = parseBuildType(args);
        String action = args.length > 1 ? normalize(args[1]) : "generate";
        boolean buildExecutable = action.equals("build") || action.equals("run");
        boolean runExecutable = action.equals("run");
        if(!action.equals("generate") && !buildExecutable) {
            throw new IllegalArgumentException("Expected action argument: generate, build, or run");
        }
        boolean consoleLog = runExecutable && (buildType == TeaGLFWBackend.NativeBuildType.DEBUG || hasConsoleArg(args));

        TeaGLFWBackend backend = new TeaGLFWBackend()
                .setBuildType(buildType)
                .setBuildExecutableAfterBuild(buildExecutable)
                .setRunExecutableAfterBuild(runExecutable)
                .setRunExecutableWithConsoleLog(consoleLog);
        AssetFileHandle glAssets = new AssetFileHandle("../../core/src/main/resources");
        AssetFileHandle gdxAssets = new AssetFileHandle("../../../shared/src/main/resources");
        AssetFileHandle sharedAssets = new AssetFileHandle("../../../../shared/src/main/resources");

        new TeaBuilder(backend)
                .addAssets(glAssets)
                .addAssets(gdxAssets)
                .addAssets(sharedAssets)
                .setOutputName("jbox3d-gdx")
                .setObfuscated(false)
                .setOptimizationLevel(TeaVMOptimizationLevel.FULL)
                .setMinHeapSize(NATIVE_MIN_HEAP_SIZE)
                .setMaxHeapSize(NATIVE_MAX_HEAP_SIZE)
                .setMinDirectBuffersSize(NATIVE_MIN_DIRECT_BUFFER_SIZE)
                .setMainClass(Box3DGdxDesktopLauncher.class.getName())
                .addReflectionClass("com.badlogic.gdx.math.Vector2")
                .build(new File("build/dist/glfw"));
    }

    private static TeaGLFWBackend.NativeBuildType parseBuildType(String[] args) {
        if(args.length == 0) {
            return TeaGLFWBackend.NativeBuildType.DEBUG;
        }
        return TeaGLFWBackend.NativeBuildType.fromString(args[0]);
    }

    private static boolean hasConsoleArg(String[] args) {
        for(int i = 2; i < args.length; i++) {
            String value = normalize(args[i]);
            if(value.equals("console")) {
                return true;
            }
            throw new IllegalArgumentException("Unsupported gdx-teavm GLFW option: " + args[i]);
        }
        return false;
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT).replace("--", "");
    }
}
