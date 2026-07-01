# jBox3D

jBox3D is the initial project shell for Java bindings to [Box3D](https://github.com/erincatto/box3d), a 3D physics engine for games.

This repository is not a usable binding yet. The first pass only establishes the Gradle project structure, publishing metadata, and module layout that future binding work will build on.

## Planned Targets

- JNI desktop runtime
- FFM desktop runtime
- TeaVM/WebAssembly web runtime
- Android JNI runtime

## Project Structure

Canonical modules are defined in `settings.gradle.kts`.

### Core library modules

- `:box3d:box3d-base` - future hand-authored Java templates and support code.
- `:box3d:box3d-build` - future generation and native build driver.
- `:box3d:box3d-core` - future generated Java API layer.
- `:box3d:box3d-jni` - future JNI runtime packaging.
- `:box3d:box3d-ffm` - future FFM runtime packaging.
- `:box3d:box3d-web` - future TeaVM/Web runtime packaging.
- `:box3d:box3d-android` - future Android runtime packaging.

## Local Checks

Use `.\gradlew.bat` on Windows and `./gradlew` on macOS/Linux.

```powershell
.\gradlew.bat projects
.\gradlew.bat :box3d:box3d-build:build :box3d:box3d-base:build :box3d:box3d-core:build
```

Android validation requires a configured Android SDK through `ANDROID_HOME`, `ANDROID_SDK_ROOT`, or a local `local.properties` file.

## Development Notes

- No upstream Box3D source is vendored in this repository yet.
- No Java bindings, generated sources, native build tasks, examples, demos, or CI workflows are included yet.
- Future Maven group ID: `com.github.xpenatan.jBox3D`.
- Planned artifact IDs: `box3d-core`, `box3d-jni`, `box3d-ffm`, `box3d-web`, and `box3d-android`.

## License

jBox3D is licensed under the [Apache License 2.0](LICENSE).

Upstream Box3D is developed by Erin Catto and is licensed under the MIT license.
