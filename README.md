# jBox3D

jBox3D is an early Java binding project for [Box3D](https://github.com/erincatto/box3d), a 3D physics engine for games.

The project currently contains the Gradle layout, native source download/build driver, initial generated wrapper bindings, JNI runtime packaging, and small desktop samples. The Java API is still experimental and will change as the binding coverage grows.

## Planned Targets

- JNI desktop runtime
- FFM desktop runtime
- C desktop runtime
- TeaVM/WebAssembly web runtime
- Android JNI runtime
- Android C runtime

## Project Structure

Canonical modules are defined in `settings.gradle.kts`.

### Core library modules

- `:box3d:base` - hand-authored Java templates and support code.
- `:box3d:builder` - Box3D source download, binding generation, and native build driver.
- `:box3d:core` - generated Java API layer.
- `:box3d:shared:jni` - generated platform-neutral JNI Java layer.
- `:box3d:shared:c` - generated platform-neutral C Java layer.
- `:box3d:desktop:jni` - desktop JNI native packaging.
- `:box3d:desktop:ffm` - desktop FFM native packaging.
- `:box3d:desktop:c` - desktop C native packaging.
- `:box3d:web:wasm` - TeaVM/WebAssembly runtime packaging.
- `:box3d:android:jni` - Android JNI runtime packaging.
- `:box3d:android:c` - Android C runtime packaging.

### Extension modules

- `:extensions:gdx` - libGDX debug renderer support.
- `:extensions:fdx` - libfdx debug renderer support.

### Sample modules

- `:samples:gdx:core` - shared libGDX 3D sample application.
- `:samples:gdx:desktop` - libGDX LWJGL3 desktop launcher.
- `:samples:gdx:web` - libGDX TeaVM web launcher.
- `:samples:fdx:core` - shared libfdx 3D sample application.
- `:samples:fdx:desktop` - libfdx desktop launchers for OpenGL, WGPU, and Vulkan.
- `:samples:fdx:web` - libfdx TeaVM web launchers for WebGL and WebGPU.

## Local Checks

Use `.\gradlew.bat` on Windows and `./gradlew` on macOS/Linux.

```powershell
.\gradlew.bat projects
.\gradlew.bat :box3d:builder:build :box3d:base:build :box3d:core:build
```

The desktop samples do not build native libraries. Build native outputs explicitly through the jParser-backed build module when needed:

```powershell
.\gradlew.bat :box3d:builder:box3d_build_project_windows64_jni
```

Use the matching `box3d_build_project_*_jni` task for other desktop platforms.

Then build the sample launchers:

```powershell
.\gradlew.bat :samples:gdx:desktop:box3d_gdx_desktop_jni_build
.\gradlew.bat :samples:fdx:desktop:box3d_fdx_desktop_gl_jni_build
.\gradlew.bat :samples:fdx:desktop:box3d_fdx_desktop_wgpu_jni_build
.\gradlew.bat :samples:fdx:desktop:box3d_fdx_desktop_vulkan_jni_build
```

Run the desktop samples with:

```powershell
.\gradlew.bat :samples:gdx:desktop:box3d_gdx_desktop_jni_run
.\gradlew.bat :samples:fdx:desktop:box3d_fdx_desktop_gl_jni_run
.\gradlew.bat :samples:fdx:desktop:box3d_fdx_desktop_wgpu_jni_run
.\gradlew.bat :samples:fdx:desktop:box3d_fdx_desktop_vulkan_jni_run
```

Web samples also consume prebuilt jParser WebAssembly outputs from `:box3d:web:wasm`; they do not compile Box3D natives themselves. After building the web runtime through `:box3d:builder`, use:

```powershell
.\gradlew.bat :samples:gdx:web:box3d_gdx_web_js_build
.\gradlew.bat :samples:gdx:web:box3d_gdx_web_wasm_build
.\gradlew.bat :samples:gdx:web:box3d_gdx_web_js_run
.\gradlew.bat :samples:gdx:web:box3d_gdx_web_wasm_run
```

Android validation requires a configured Android SDK through `ANDROID_HOME`, `ANDROID_SDK_ROOT`, or a local `local.properties` file.

## Development Notes

- Upstream Box3D source is downloaded into the build directory and is not vendored.
- Box3D debug draw callbacks are exposed through `B3DebugDrawEm`; the libGDX and libfdx samples render them through the extension modules.
- No CI workflows are included yet.
- Future Maven group ID: `com.github.xpenatan.jBox3D`.
- Planned artifact IDs: `core`, `shared-jni`, `shared-c`, `desktop-jni`, `desktop-ffm`, `desktop-c`, `web-wasm`, `android-jni`, `android-c`, `gdx`, and `fdx`.

## License

jBox3D is licensed under the [Apache License 2.0](LICENSE).

Upstream Box3D is developed by Erin Catto and is licensed under the MIT license.
