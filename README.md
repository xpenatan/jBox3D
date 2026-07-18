# jBox3D

[![Snapshot build](https://github.com/xpenatan/jBox3D/actions/workflows/snapshot.yml/badge.svg)](https://github.com/xpenatan/jBox3D/actions/workflows/snapshot.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.xpenatan.jBox3D/core)](https://central.sonatype.com/namespace/com.github.xpenatan.jBox3D)

Java bindings for [Box3D](https://github.com/erincatto/box3d) across desktop, web, and Android.

jBox3D provides a platform-neutral Java API, native runtimes for JNI, Java FFM, TeaVM C, and WebAssembly, plus integrations for libGDX and libFDX. The bindings are generated from a WebIDL contract and stay close to the upstream Box3D API.

**Online samples:** [xpenatan.github.io/jBox3D](https://xpenatan.github.io/jBox3D) | **2D companion project:** [jBox2D](https://github.com/xpenatan/jBox2d)

## Use jBox3D

Artifacts use the Maven group `com.github.xpenatan.jBox3D`. A project normally depends on the core API, one platform runtime, and optionally a rendering integration.

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

val jbox3dVersion = "-SNAPSHOT"

dependencies {
    implementation("com.github.xpenatan.jBox3D:core:$jbox3dVersion")
    runtimeOnly("com.github.xpenatan.jBox3D:desktop-jni:$jbox3dVersion")
    implementation("com.github.xpenatan.jBox3D:gdx-gl:$jbox3dVersion")
}
```

For a release, use its published version and remove the snapshot repository when it is no longer needed.

### Artifacts

| Artifact | Purpose |
| --- | --- |
| `core` | Platform-neutral Box3D API. |
| `shared-jni`, `desktop-jni`, `android-jni` | JNI bindings and platform runtimes. |
| `desktop-ffm` | Java 25 FFM desktop runtime. |
| `shared-c`, `desktop-c`, `android-c` | TeaVM C bindings and platform runtimes. |
| `web-wasm` | TeaVM web bindings and Emscripten side module. |
| `gdx-gl`, `gdx-wgpu`, `fdx` | Rendering and math integrations. |

## API model

The generated API stays close to Box3D semantics while adapting native handles and structs for Java. Integration converters write into caller-owned output objects, avoiding temporary allocations in render loops:

```java
GdxBox3DConverter.toGdx(box3dVector, reusableGdxVector);
GdxBox3DConverter.toBox3D(gdxVector, reusableBox3dVector);

FdxBox3DConverter.toFdx(box3dVector, reusableFdxVector);
FdxBox3DConverter.toBox3D(fdxVector, reusableBox3dVector);
```

## Samples

The sample suite includes libGDX OpenGL and WebGPU renderers, plus libFDX OpenGL, WebGPU, and Vulkan renderers where supported.

## Documentation

- [Build from source and run the samples](docs/usage.md)
- [Binding contract](box3d/builder/src/main/cpp/box3d.idl)

## Status

Box3D and its Java API are still evolving. Binding generation, native packaging, integrations, and cross-platform samples are available, but APIs may change between releases.

## License

jBox3D is licensed under the [Apache License 2.0](LICENSE). Upstream Box3D is developed by Erin Catto and is licensed under the MIT license.
