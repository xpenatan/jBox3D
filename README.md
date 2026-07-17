# jBox3D

Java bindings for [Box3D](https://github.com/erincatto/box3d) across desktop, web, and Android.

The project uses [jParser](https://github.com/xpenatan/jParser) to generate bindings from a WebIDL contract and a small native facade. It downloads the pinned upstream Box3D source into the build directory, generates Java and native glue, and packages the platform runtime modules.

The generated API stays close to upstream Box3D semantics while adapting native handles and structs for Java. The repository also includes cross-platform sample applications with solver controls, debug rendering, body interaction, and camera controls.

**Live samples:** [xpenatan.github.io/jBox3D](https://xpenatan.github.io/jBox3D) | **2D companion project:** [jBox2D](https://github.com/xpenatan/jBox2d)

## Modules

| Area | Modules | Purpose |
| --- | --- | --- |
| Source and bindings | `:box3d:download`, `:box3d:builder` | Download the pinned Box3D source, own the binding contract and facade, generate Java/native glue, and build native targets. |
| Core Java API | `:box3d:base`, `:box3d:core` | Runtime-loader support and the generated platform-neutral API. |
| Desktop JNI | `:box3d:shared:jni`, `:box3d:desktop:jni` | Generated JNI bindings and native libraries for Windows, Linux, and macOS. |
| Desktop FFM | `:box3d:desktop:ffm` | Java 25 Foreign Function & Memory bindings and desktop native libraries. |
| TeaVM C | `:box3d:shared:c`, `:box3d:desktop:c` | Generated C-runtime bindings and desktop native packaging. |
| WebAssembly | `:box3d:web:wasm` | TeaVM web API plus the Emscripten side module. |
| Android | `:box3d:android:jni`, `:box3d:android:c` | Android JNI and TeaVM C runtime packaging. |
| Integrations | `:extensions:gdx:gl`, `:extensions:gdx:wgpu`, `:extensions:fdx` | libGDX OpenGL/WebGPU and libFDX math/debug-rendering support. |
| Samples | `:samples:shared`, `:samples:gdx:*`, `:samples:fdx:*` | Shared Box3D scenarios plus libGDX and libFDX platform launchers. |

The binding contract is [`box3d.idl`](box3d/builder/src/main/cpp/box3d.idl). The [`custom`](box3d/builder/src/main/cpp/custom) facade adapts the native API for code generation and shared Java use.

Box3D and its Java API are still evolving, but binding generation, native builds, runtime packaging, integration modules, cross-platform samples, and GitHub Pages deployment are in place.

## Maven artifacts

Release artifacts are published to Maven Central under `com.github.xpenatan.jBox3D`. Snapshot artifacts use the `-SNAPSHOT` version and are published through the Central Portal snapshot repository.

| Artifact | Purpose |
| --- | --- |
| `core` | Platform-neutral Box3D API. |
| `shared-jni`, `desktop-jni`, `android-jni` | JNI bindings and platform runtimes. |
| `desktop-ffm` | Java 25 FFM desktop runtime. |
| `shared-c`, `desktop-c`, `android-c` | TeaVM C bindings and platform runtimes. |
| `web-wasm` | TeaVM web bindings and Emscripten side module. |
| `gdx-gl`, `gdx-wgpu`, `fdx` | Rendering and math integrations. |

A consumer normally selects `core`, one runtime, and an optional rendering integration:

```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://central.sonatype.com/repository/maven-snapshots/")
        content {
            includeGroup("com.github.xpenatan.jBox3D")
        }
    }
}

dependencies {
    implementation("com.github.xpenatan.jBox3D:core:-SNAPSHOT")
    runtimeOnly("com.github.xpenatan.jBox3D:desktop-jni:-SNAPSHOT")
    implementation("com.github.xpenatan.jBox3D:gdx-gl:-SNAPSHOT")
}
```

For a release, replace `-SNAPSHOT` with the version from `gradle.properties` and the snapshot repository can be omitted.

The integration converters write into caller-owned output objects, avoiding temporary allocations in render loops:

```java
GdxBox3DConverter.toGdx(box3dVector, reusableGdxVector);
GdxBox3DConverter.toBox3D(gdxVector, reusableBox3dVector);

FdxBox3DConverter.toFdx(box3dVector, reusableFdxVector);
FdxBox3DConverter.toBox3D(fdxVector, reusableBox3dVector);
```

## Samples

The Box3D scenario implementations and registry live in `:samples:shared`. Both front ends reuse that catalog: libGDX provides OpenGL and WebGPU renderers, while libFDX provides OpenGL, WebGPU, and Vulkan paths where the platform supports them.

| Front end | Desktop | Web | Android |
| --- | --- | --- | --- |
| libGDX / OpenGL | JNI, FFM, and TeaVM C | TeaVM JavaScript and WasmGC | JNI |
| libGDX / WebGPU | JNI and FFM | TeaVM JavaScript and WasmGC | JNI |
| libFDX / OpenGL, WebGPU, Vulkan | JNI and FFM; C compile paths | OpenGL and WebGPU with TeaVM JavaScript or WasmGC | OpenGL ES, WebGPU, and Vulkan with JNI |

The shared Java scenarios are under [`samples/shared`](samples/shared/src/main/java/com/github/xpenatan/box3d/sample/shared/samples). Renderer-specific applications and platform launchers remain separate, so the physics samples do not need to be duplicated.

## Requirements

- JDK 25 for the full build, FFM runtime, and libFDX modules.
- A platform C/C++ toolchain: MSVC on Windows, clang or GCC on Linux, or Xcode command-line tools on macOS.
- Emscripten for the WebAssembly target.
- Android SDK and NDK for Android native targets (compile SDK 36; OpenGL modules require API 21, while WebGPU and libFDX samples require API 29).
- Access to Maven Central and the configured Central Portal snapshot repository for Gradle dependencies.

The Gradle wrapper is pinned to 9.4.1 so the full build can run on JDK 25. On macOS or Linux, use `./gradlew` in place of `\.\gradlew.bat` in the commands below.

## Generate the bindings

```powershell
.\gradlew.bat :box3d:download:box3d_download_source
.\gradlew.bat :box3d:builder:jParser_generate
```

The download task writes the upstream source to `box3d/download/build/box3d-source`; Box3D source is not vendored in this repository. Generated source directories are removed by their module `clean` tasks, so a from-scratch build must regenerate them:

```powershell
.\gradlew.bat clean
.\gradlew.bat :box3d:builder:jParser_generate
.\gradlew.bat build
```

## Build native runtimes

Windows x64 examples:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_windows64_jni
.\gradlew.bat :box3d:builder:jParser_build_windows64_ffm
.\gradlew.bat :box3d:builder:jParser_build_windows64_teavm_c
.\gradlew.bat :box3d:builder:jParser_build_web_wasm
```

Replace `windows64` with `linux64`, `mac64`, or `macArm` for the corresponding desktop target. Android native tasks are:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_android_jni
.\gradlew.bat :box3d:builder:jParser_build_android_teavm_c
```

The builder also exposes `:box3d:builder:jParser_build_ios_jni`, but this repository does not yet include an iOS runtime-packaging or sample module.

## Run the samples

### Desktop

Build the native JNI, FFM, or C target for the current host before launching those samples. The launcher tasks package native libraries already produced by the builder; they do not invoke the native build themselves.

libGDX / OpenGL:

```powershell
.\gradlew.bat :samples:gdx:gl:platforms:desktop-jni:box3d_gdx_desktop_jni_run
.\gradlew.bat :samples:gdx:gl:platforms:desktop-ffm:box3d_gdx_desktop_ffm_run
.\gradlew.bat :samples:gdx:gl:platforms:desktop-c:gdx_teavm_glfw_run
```

The gdx-teavm plugin provides the standard `gdx_teavm_glfw_generate`, `gdx_teavm_glfw_build`, and `gdx_teavm_glfw_run` tasks.

libGDX / WebGPU:

```powershell
.\gradlew.bat :samples:gdx:wgpu:platforms:desktop-jni:box3d_gdx_wgpu_desktop_jni_run
.\gradlew.bat :samples:gdx:wgpu:platforms:desktop-ffm:box3d_gdx_wgpu_desktop_ffm_run
```

libFDX / OpenGL, WebGPU, and Vulkan:

```powershell
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_gl_jni_run
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_wgpu_jni_run
.\gradlew.bat :samples:fdx:platforms:desktop-jni:box3d_fdx_desktop_vulkan_jni_run

.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_gl_ffm_run
.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_wgpu_ffm_run
.\gradlew.bat :samples:fdx:platforms:desktop-ffm:box3d_fdx_desktop_vulkan_ffm_run
```

The libFDX desktop C module currently exposes compile-path validation rather than runnable C launchers:

```powershell
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_gl_c_build
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_wgpu_c_build
.\gradlew.bat :samples:fdx:platforms:desktop-c:box3d_fdx_desktop_vulkan_c_build
```

### Web

Build the Emscripten Box3D side module before launching the browser samples:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_web_wasm
```

libGDX / OpenGL and WebGPU:

```powershell
.\gradlew.bat :samples:gdx:gl:platforms:web:gdx_teavm_web_js_run
.\gradlew.bat :samples:gdx:gl:platforms:web:gdx_teavm_web_wasm_run
.\gradlew.bat :samples:gdx:wgpu:platforms:web:gdx_teavm_web_js_run
.\gradlew.bat :samples:gdx:wgpu:platforms:web:gdx_teavm_web_wasm_run
```

libFDX / OpenGL and WebGPU:

```powershell
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgl_js_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgl_wasm_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgpu_js_run
.\gradlew.bat :samples:fdx:platforms:web:box3d_fdx_webgpu_wasm_run
```

### Android

Build the JNI runtime, then choose a sample application to install and launch through `adb`:

```powershell
.\gradlew.bat :box3d:builder:jParser_build_android_jni
.\gradlew.bat :samples:gdx:gl:platforms:android:box3d_gdx_android_jni_run
.\gradlew.bat :samples:gdx:wgpu:platforms:android:box3d_gdx_wgpu_android_jni_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_gles_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_wgpu_jni_run
.\gradlew.bat :samples:fdx:platforms:android:box3d_fdx_android_vulkan_run
```

`adb` is resolved from `local.properties`, `ANDROID_HOME`, `ANDROID_SDK_ROOT`, or `PATH`.

### Controls and sample selection

Use the sample menu and settings panel to choose a scenario, renderer options, solver settings, and debug views. In the libGDX front end, use the right mouse button to look around; use `WASD` or the arrow keys to move, `Q`/`E` to move vertically, `R` to restart the scenario, `C` to reset the camera, and `T` to throw the selected shape. Click to throw toward the pointer, or hold `Ctrl` and drag with the primary button to pick a body.

Launchers accept a category/name through `jbox3d.sample.sample` or a registry index through `jbox3d.sample.sampleIndex`. For example:

```powershell
$env:GRADLE_OPTS = '-Djbox3d.sample.sample=Stacking/Single Box'
.\gradlew.bat :samples:gdx:gl:platforms:desktop-jni:box3d_gdx_desktop_jni_run
```

## GitHub Pages

The [live site](https://xpenatan.github.io/jBox3D) packages four libGDX distributions: OpenGL and WebGPU, each compiled with TeaVM JavaScript and WasmGC. The libFDX web launchers are available locally but are not currently included in the Pages bundle.

The manually dispatched [GitHub Pages workflow](.github/workflows/gh-pages.yml) writes the static site to `samples/gdx/gl/platforms/web/build/pages` and deploys it when run from `master`; the repository's Pages source must be set to **GitHub Actions**.

## Publishing

The reusable Maven workflow builds Windows, Linux, macOS Intel/Apple Silicon, WebAssembly, and Android native runtimes before publishing one complete artifact set. Pushes to `master` run the snapshot workflow; releases are started manually with the `Build Release` workflow.

Configure these GitHub Actions secrets before enabling uploads:

- `CENTRAL_PORTAL_USERNAME` and `CENTRAL_PORTAL_PASSWORD`: a Maven Central Portal user token.
- `PGP_SECRET` and `PGP_PASSPHRASE`: the ASCII-armored private signing key and its passphrase.

The Central Portal namespace must also have snapshot publishing enabled. Local preparation commands are:

```powershell
# Unsigned local snapshot repository: build/snapshot-deploy
.\gradlew.bat prepareSnapshotDeploy

# Signed release bundle without uploading: build/staging-deploy.zip
.\gradlew.bat validateReleaseDependencies
.\gradlew.bat publishTestRelease

# Remote publishing; credentials and signing variables are required
.\gradlew.bat publishSnapshot
.\gradlew.bat publishRelease
```

`publishRelease` requests automatic publishing after Central validation. Maven Central releases are immutable, so build and inspect the signed bundle with `publishTestRelease` before dispatching a release.

The release workflow verifies that all required non-snapshot dependency versions are available from Maven Central before starting the native build matrix.

## License

jBox3D is licensed under the [Apache License 2.0](LICENSE). Upstream Box3D is developed by Erin Catto and is licensed under the MIT license.
