# jBox3D

jBox3D provides Java bindings for [Box3D](https://github.com/erincatto/box3d), Erin Catto's 3D physics engine.

The project uses jParser WebIDL bindings over a small C/C++ wrapper layer, downloads upstream Box3D source into the build directory, generates Java/native glue, and packages runtime modules for desktop, web, and Android targets. It also includes libGDX and libFDX sample applications with Box3D-style sample selection, solver settings, debug rendering, picking, throwing, and camera controls.

The Java API is still evolving, but generated bindings, native builds, runtime packaging, extensions, samples, and a GitHub Pages web deployment workflow are in place.

## Targets

| Target | Modules | Notes |
| --- | --- | --- |
| Core Java API | `:box3d:base`, `:box3d:core` | Hand-authored support plus generated Box3D API classes. |
| Box3D source download | `:box3d:download` | Downloads upstream Box3D source used by the builder. |
| Desktop JNI | `:box3d:shared:jni`, `:box3d:desktop:jni` | Native library packaging for Windows, Linux, and macOS. |
| Desktop FFM | `:box3d:desktop:ffm` | Java 25 FFM runtime for desktop. |
| Desktop C | `:box3d:shared:c`, `:box3d:desktop:c` | TeaVM C generated binding runtime used by the desktop C samples. |
| WebAssembly | `:box3d:web:wasm` | Emscripten WebAssembly runtime used by TeaVM browser samples. |
| Android JNI | `:box3d:android:jni` | Android JNI runtime packaging. |
| Android C | `:box3d:android:c` | Android TeaVM C runtime packaging. |
| libGDX GL extension | `:extensions:gdx:gl` | Base libGDX integration with allocation-free math conversions and ModelBatch debug rendering. |
| libGDX WebGPU extension | `:extensions:gdx:wgpu` | WebGPU-specific rendering support layered on the libGDX GL extension. |
| libFDX extension | `:extensions:fdx` | ModelBatch-based debug renderer support for libFDX samples. |

The interop converters write into caller-owned output objects so they can be reused in render loops:

```java
GdxBox3DConverter.toGdx(box3dVector, reusableGdxVector);
GdxBox3DConverter.toBox3D(gdxVector, reusableBox3dVector);

FdxBox3DConverter.toFdx(box3dVector, reusableFdxVector);
FdxBox3DConverter.toBox3D(fdxVector, reusableBox3dVector);
```

## Samples

The shared Box3D sample logic lives in `:samples:shared`. The shared libGDX app/UI layer lives in `:samples:gdx:shared`, with renderer-specific cores in `:samples:gdx:gl:core` and `:samples:gdx:wgpu:core`.

Web version: [https://xpenatan.github.io/jBox3D](https://xpenatan.github.io/jBox3D)

libGDX WebGL sample launchers:

- `:samples:gdx:gl:platforms:desktop-jni`
- `:samples:gdx:gl:platforms:desktop-ffm`
- `:samples:gdx:gl:platforms:desktop-c`
- `:samples:gdx:gl:platforms:web`
- `:samples:gdx:gl:platforms:android`

libGDX WebGPU sample launchers:

- `:samples:gdx:wgpu:platforms:desktop-jni`
- `:samples:gdx:wgpu:platforms:desktop-ffm`
- `:samples:gdx:wgpu:platforms:web`
- `:samples:gdx:wgpu:platforms:android`

libFDX sample launchers:

- `:samples:fdx:platforms:desktop-jni`
- `:samples:fdx:platforms:desktop-ffm`
- `:samples:fdx:platforms:desktop-c`
- `:samples:fdx:platforms:web`
- `:samples:fdx:platforms:android`

## Requirements

- JDK 25 for the full build and FFM/runtime validation.
- Android SDK for Android modules and samples.
- Emscripten for `:box3d:builder:jParser_build_web_wasm`.
- Platform native toolchains for desktop native builds: MSVC on Windows, clang/gcc on Linux, and Xcode command line tools on macOS.
- jParser, gdx-teavm, gdx-webgpu, and libFDX are consumed with `-SNAPSHOT` versions from Maven snapshots.

## Native Build Driver

The download module downloads Box3D source into `box3d/download/build/box3d-source`. The builder module consumes that source, generates Java bindings, and builds native outputs through jParser.

```powershell
.\gradlew.bat :box3d:download:box3d_download_source
.\gradlew.bat :box3d:builder:jParser_generate
```

Common native build tasks:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_windows64_jni
.\gradlew.bat :box3d:builder:jParser_build_windows64_ffm
.\gradlew.bat :box3d:builder:jParser_build_windows64_teavm_c
.\gradlew.bat :box3d:builder:jParser_build_web_wasm
.\gradlew.bat :box3d:builder:jParser_build_android_jni
.\gradlew.bat :box3d:builder:jParser_build_android_teavm_c
```

Equivalent `jParser_build_*` tasks exist for `linux64`, `mac64`, and `macArm` desktop targets. The builder also exposes the jParser iOS JNI task; `settings.gradle.kts` does not include an iOS runtime or sample module.

## Running Samples

Desktop libGDX WebGL:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_windows64_jni
.\gradlew.bat :samples:gdx:gl:platforms:desktop-jni:box3d_gdx_desktop_jni_run

.\gradlew.bat :box3d:builder:jParser_build_windows64_ffm
.\gradlew.bat :samples:gdx:gl:platforms:desktop-ffm:box3d_gdx_desktop_ffm_run

.\gradlew.bat :samples:gdx:gl:platforms:desktop-c:box3d_gdx_desktop_c_run
```

Desktop libGDX WebGPU:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_windows64_jni
.\gradlew.bat :samples:gdx:wgpu:platforms:desktop-jni:box3d_gdx_wgpu_desktop_jni_run

.\gradlew.bat :box3d:builder:jParser_build_windows64_ffm
.\gradlew.bat :samples:gdx:wgpu:platforms:desktop-ffm:box3d_gdx_wgpu_desktop_ffm_run
```

Desktop libFDX:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_windows64_jni
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_gl_jni_run
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_wgpu_jni_run
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_vulkan_jni_run

.\gradlew.bat :box3d:builder:jParser_build_windows64_ffm
.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_gl_ffm_run
.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_wgpu_ffm_run
.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_vulkan_ffm_run
```

Desktop C samples use the current host C native target automatically. The default tasks link Box3D statically;
the additional tasks exercise operating-system-linked and runtime-loaded shared libraries:

```powershell
.\gradlew.bat :samples:gdx:gl:platforms:desktop-c:box3d_gdx_desktop_c_build
.\gradlew.bat :samples:gdx:gl:platforms:desktop-c:box3d_gdx_desktop_c_run
.\gradlew.bat :samples:gdx:gl:platforms:desktop-c:box3d_gdx_desktop_c_shared_linked_run
.\gradlew.bat :samples:gdx:gl:platforms:desktop-c:box3d_gdx_desktop_c_runtime_loaded_run
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_gl_c_build
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_wgpu_c_build
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_vulkan_c_build
```

Web libGDX:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_web_wasm
.\gradlew.bat :samples:gdx:gl:platforms:web:gdx_teavm_web_js_run
.\gradlew.bat :samples:gdx:gl:platforms:web:gdx_teavm_web_wasm_run
.\gradlew.bat :samples:gdx:wgpu:platforms:web:gdx_teavm_web_js_run
.\gradlew.bat :samples:gdx:wgpu:platforms:web:gdx_teavm_web_wasm_run
```

Web libFDX:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_web_wasm
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgl_js_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgl_wasm_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgpu_js_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgpu_wasm_run
```

Android:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_android_jni
.\gradlew.bat :samples:gdx:gl:platforms:android:box3d_gdx_android_jni_run
.\gradlew.bat :samples:gdx:wgpu:platforms:android:box3d_gdx_wgpu_android_jni_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_gles_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_wgpu_jni_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_vulkan_run
```

Android run tasks require `adb` from `ANDROID_HOME`, `ANDROID_SDK_ROOT`, `local.properties`, or `PATH`.

## License

jBox3D is licensed under the [Apache License 2.0](LICENSE).

Upstream Box3D is developed by Erin Catto and is licensed under the MIT license.
