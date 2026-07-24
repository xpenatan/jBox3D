# Build and sample usage

This guide covers building jBox3D from source and running its sample applications. For library dependencies and artifact selection, see the main [README](../README.md).

## Requirements

- JDK 25 for the full build, FFM runtime, and libFDX modules.
- A platform C/C++ toolchain: MSVC on Windows, clang or GCC on Linux, or Xcode command-line tools on macOS.
- Emscripten for the WebAssembly target.
- Android SDK and NDK for Android native targets. The build uses compile SDK 36; OpenGL modules require API 21, while libFDX samples require API 29.
- Access to Maven Central and the configured snapshot repository for Gradle dependencies.

The Gradle wrapper uses Gradle 9.6.1. The commands below use PowerShell on Windows; on macOS or Linux, use `./gradlew` instead of `.\gradlew.bat`.

## Version catalog

Build targets, publication versions, plugin versions, and external dependencies are declared in `gradle/libs.versions.toml`. Catalog aliases use lower camelCase so their generated Gradle accessors remain flat.

## Project layout

| Area | Modules | Purpose |
| --- | --- | --- |
| Source and bindings | `:box3d:download`, `:box3d:builder` | Download Box3D, generate bindings, and build native targets. |
| Core Java API | `:box3d:base`, `:box3d:core` | Runtime-loader support and the platform-neutral API. |
| Desktop JNI | `:box3d:shared:jni`, `:box3d:desktop:jni` | JNI bindings and native libraries for Windows, Linux, and macOS. |
| Desktop FFM | `:box3d:desktop:ffm` | Java 25 FFM bindings and native libraries. |
| TeaVM C | `:box3d:shared:c`, `:box3d:desktop:c` | TeaVM C bindings and desktop native packaging. |
| WebAssembly | `:box3d:web:wasm` | TeaVM web API and the Emscripten side module. |
| Android | `:box3d:android:jni`, `:box3d:android:c` | Android JNI and TeaVM C runtime packaging. |
| Integrations | `:extensions:gdx:gl`, `:extensions:fdx` | libGDX and libFDX integrations. |
| Samples | `:samples:shared`, `:samples:gdx:*`, `:samples:fdx:*` | Shared scenarios and platform launchers. |

## Generate the bindings

Download the pinned Box3D source and generate the Java and native bindings:

```powershell
.\gradlew.bat :box3d:download:box3d_download_source
.\gradlew.bat :box3d:builder:jParser_generate
```

The downloaded source is written to `box3d/download/build/box3d-source` and is not vendored in the repository. Generated source directories are removed by their module `clean` tasks, so a clean build must regenerate them:

```powershell
.\gradlew.bat clean
.\gradlew.bat :box3d:builder:jParser_generate
.\gradlew.bat build
```

## Build native runtimes

Build the runtime needed by the application before launching it. Sample launcher tasks package native libraries already produced by these builder tasks.

### Desktop

Windows x64:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_windows64_jni
.\gradlew.bat :box3d:builder:jParser_build_windows64_ffm
.\gradlew.bat :box3d:builder:jParser_build_windows64_teavm_c
```

Replace `windows64` with `linux64`, `mac64`, or `macArm` for the corresponding desktop target.

### WebAssembly

```powershell
.\gradlew.bat :box3d:builder:jParser_build_web_wasm
```

### Android

```powershell
.\gradlew.bat :box3d:builder:jParser_build_android_jni
.\gradlew.bat :box3d:builder:jParser_build_android_teavm_c
```

The builder also exposes `:box3d:builder:jParser_build_ios_jni`, but the repository does not currently include an iOS runtime module or sample application.

## Run the samples

### Desktop

libGDX OpenGL:

```powershell
.\gradlew.bat :samples:gdx:gl:platforms:desktop-jni:box3d_gdx_desktop_jni_run
.\gradlew.bat :samples:gdx:gl:platforms:desktop-ffm:box3d_gdx_desktop_ffm_run
.\gradlew.bat :samples:gdx:gl:platforms:desktop-c:gdx_teavm_glfw_run
```

libFDX OpenGL, WebGPU, and Vulkan:

```powershell
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_gl_jni_run
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_wgpu_jni_run
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_vulkan_jni_run

.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_gl_ffm_run
.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_wgpu_ffm_run
.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_vulkan_ffm_run
```

The libFDX desktop C module currently provides compile-path validation rather than runnable launchers:

```powershell
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_gl_c_build
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_wgpu_c_build
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_vulkan_c_build
```

### Web

Build the WebAssembly runtime first, then launch a JavaScript or WasmGC sample.

libGDX OpenGL:

```powershell
.\gradlew.bat :samples:gdx:gl:platforms:web:gdx_teavm_web_js_run
.\gradlew.bat :samples:gdx:gl:platforms:web:gdx_teavm_web_wasm_run
```

libFDX OpenGL and WebGPU:

```powershell
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgl_js_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgl_wasm_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgpu_js_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgpu_wasm_run
```

### Android

Build the JNI runtime, then install and launch a sample through `adb`:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_android_jni
.\gradlew.bat :samples:gdx:gl:platforms:android:box3d_gdx_android_jni_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_gles_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_wgpu_jni_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_vulkan_run
```

`adb` is resolved from `local.properties`, `ANDROID_HOME`, `ANDROID_SDK_ROOT`, or `PATH`.

## Controls and sample selection

Use the sample menu and settings panel to choose a scenario, renderer options, solver settings, and debug views. In the libGDX front end:

- Hold the right mouse button to look around.
- Use `WASD` or the arrow keys to move and `Q`/`E` to move vertically.
- Press `R` to restart the scenario, `C` to reset the camera, or `T` to throw the selected shape.
- Click to throw toward the pointer.
- Hold `Ctrl` and drag with the primary button to pick a body.

Launchers accept a category and name through `jbox3d.sample.sample`, or a registry index through `jbox3d.sample.sampleIndex`:

```powershell
$env:GRADLE_OPTS = '-Djbox3d.sample.sample=Stacking/Single Box'
.\gradlew.bat :samples:gdx:gl:platforms:desktop-jni:box3d_gdx_desktop_jni_run
```
