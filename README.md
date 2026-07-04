# jBox3D

jBox3D provides Java bindings for [Box3D](https://github.com/erincatto/box3d), Erin Catto's 3D physics engine.

The project uses jParser WebIDL bindings over a small C/C++ wrapper layer, downloads upstream Box3D source into the build directory, generates Java/native glue, and packages runtime modules for desktop, web, and Android targets. It also includes libGDX and libFDX sample applications with Box3D-style sample selection, solver settings, debug rendering, picking, throwing, and camera controls.

The Java API is still evolving, but generated bindings, native builds, runtime packaging, extensions, samples, and a GitHub Pages web deployment workflow are in place.

## Targets

| Target | Modules | Notes |
| --- | --- | --- |
| Core Java API | `:box3d:base`, `:box3d:core` | Hand-authored support plus generated Box3D API classes. |
| Desktop JNI | `:box3d:shared:jni`, `:box3d:desktop:jni` | Native library packaging for Windows, Linux, and macOS. |
| Desktop FFM | `:box3d:desktop:ffm` | Java 25 FFM runtime for desktop. |
| Desktop C | `:box3d:shared:c`, `:box3d:desktop:c` | TeaVM C generated binding runtime used by the desktop C samples. |
| WebAssembly | `:box3d:web:wasm` | Emscripten WebAssembly runtime used by TeaVM browser samples. |
| Android JNI | `:box3d:android:jni` | Android JNI runtime packaging. |
| Android C | `:box3d:android:c` | Android TeaVM C runtime packaging. |
| libGDX extension | `:extensions:gdx` | ModelBatch-based debug renderer support for libGDX samples. |
| libFDX extension | `:extensions:fdx` | ModelBatch-based debug renderer support for libFDX samples. |

## Samples

The shared sample logic lives in `:samples:shared`, with renderer-specific cores in `:samples:gdx:core` and `:samples:fdx:core`.

Web version: [https://xpenatan.github.io/jBox3D](https://xpenatan.github.io/jBox3D)

libGDX sample launchers:

- `:samples:gdx:platforms:desktop-jni`
- `:samples:gdx:platforms:desktop-ffm`
- `:samples:gdx:platforms:desktop-c`
- `:samples:gdx:platforms:web`
- `:samples:gdx:platforms:android`

libFDX sample launchers:

- `:samples:fdx:platforms:desktop-jni`
- `:samples:fdx:platforms:desktop-ffm`
- `:samples:fdx:platforms:desktop-c`
- `:samples:fdx:platforms:web`
- `:samples:fdx:platforms:android`

## Requirements

- JDK 25 for the full build and FFM/runtime validation.
- Android SDK for Android modules and samples.
- Emscripten for `:box3d:builder:box3d_build_project_web_wasm`.
- Platform native toolchains for desktop native builds: MSVC on Windows, clang/gcc on Linux, and Xcode command line tools on macOS.
- jParser, gdx-teavm, and libFDX are consumed with `-SNAPSHOT` versions from Maven snapshots.

## Native Build Driver

The builder module downloads Box3D source into `box3d/builder/build/box3d-source`, generates Java bindings, and builds native outputs through jParser.

```powershell
.\gradlew.bat :box3d:builder:box3d_download_source
.\gradlew.bat :box3d:builder:box3d_build_project
```

Common native build aliases:

```powershell
.\gradlew.bat :box3d:builder:box3d_build_project_windows64_jni
.\gradlew.bat :box3d:builder:box3d_build_project_windows64_ffm
.\gradlew.bat :box3d:builder:box3d_build_project_windows64_c
.\gradlew.bat :box3d:builder:box3d_build_project_web_wasm
.\gradlew.bat :box3d:builder:box3d_build_project_android_jni
.\gradlew.bat :box3d:builder:box3d_build_project_android_c
```

Equivalent aliases exist for `linux64`, `mac64`, and `macArm` desktop targets. The builder also exposes the jParser iOS JNI target alias; `settings.gradle.kts` does not include an iOS runtime or sample module.

## Running Samples

Desktop libGDX:

```powershell
.\gradlew.bat :box3d:builder:box3d_build_project_windows64_jni
.\gradlew.bat :samples:gdx:platforms:desktop-jni:box3d_gdx_desktop_jni_run

.\gradlew.bat :box3d:builder:box3d_build_project_windows64_ffm
.\gradlew.bat :samples:gdx:platforms:desktop-ffm:box3d_gdx_desktop_ffm_run

.\gradlew.bat :samples:gdx:platforms:desktop-c:box3d_gdx_desktop_c_run
```

Desktop libFDX:

```powershell
.\gradlew.bat :box3d:builder:box3d_build_project_windows64_jni
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_gl_jni_run
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_wgpu_jni_run
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_vulkan_jni_run

.\gradlew.bat :box3d:builder:box3d_build_project_windows64_ffm
.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_gl_ffm_run
.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_wgpu_ffm_run
.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_vulkan_ffm_run
```

Desktop C samples use the current host C native target automatically:

```powershell
.\gradlew.bat :samples:gdx:platforms:desktop-c:box3d_gdx_desktop_c_build
.\gradlew.bat :samples:gdx:platforms:desktop-c:box3d_gdx_desktop_c_run
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_gl_c_build
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_wgpu_c_build
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_vulkan_c_build
```

Web libGDX:

```powershell
.\gradlew.bat :box3d:builder:box3d_build_project_web_wasm
.\gradlew.bat :samples:gdx:platforms:web:gdx_teavm_web_js_run
.\gradlew.bat :samples:gdx:platforms:web:gdx_teavm_web_wasm_run
```

Web libFDX:

```powershell
.\gradlew.bat :box3d:builder:box3d_build_project_web_wasm
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgl_js_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgl_wasm_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgpu_js_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgpu_wasm_run
```

Android:

```powershell
.\gradlew.bat :box3d:builder:box3d_build_project_android_jni
.\gradlew.bat :samples:gdx:platforms:android:box3d_gdx_android_jni_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_gles_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_wgpu_jni_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_vulkan_run
```

Android run tasks require `adb` from `ANDROID_HOME`, `ANDROID_SDK_ROOT`, `local.properties`, or `PATH`.

## License

jBox3D is licensed under the [Apache License 2.0](LICENSE).

Upstream Box3D is developed by Erin Catto and is licensed under the MIT license.
