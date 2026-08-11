pluginManagement {
    repositories {
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "jBox3D"

val libfdxBuild = file(providers.gradleProperty("libfdx.localPath").orElse("../libfdx").get())
    .canonicalFile
val libfdxLocalMode = providers.gradleProperty("libfdx.local")
    .orElse("auto")
    .get()
    .trim()
    .lowercase()
val useLocalLibfdx = when(libfdxLocalMode) {
    "auto" -> libfdxBuild.resolve("settings.gradle.kts").isFile
    "true" -> true
    "false" -> false
    else -> throw GradleException(
        "libfdx.local must be auto, true, or false (was '$libfdxLocalMode')."
    )
}

include(":box3d:builder")
include(":box3d:download")
include(":box3d:base")
include(":box3d:core")
include(":box3d:shared:jni")
include(":box3d:shared:c")
include(":box3d:desktop:jni")
include(":box3d:desktop:ffm")
include(":box3d:desktop:c")
include(":box3d:web:wasm")
include(":box3d:android:jni")
include(":box3d:android:c")
include(":extensions:gdx:gl")

include(":samples:shared")
include(":samples:gdx:core")
include(":samples:gdx:gl:core")
include(":samples:gdx:gl:platforms:desktop-jni")
include(":samples:gdx:gl:platforms:desktop-ffm")
include(":samples:gdx:gl:platforms:desktop-c")
include(":samples:gdx:gl:platforms:web")
include(":samples:gdx:gl:platforms:android")
include(":samples:fdx:core")
include(":samples:fdx:platforms:desktop-jni")
include(":samples:fdx:platforms:desktop-ffm")
include(":samples:fdx:platforms:desktop-c")
include(":samples:fdx:platforms:web")
include(":samples:fdx:platforms:android")

if(useLocalLibfdx) {
    require(libfdxBuild.resolve("settings.gradle.kts").isFile) {
        "The local libFDX build was not found at '${libfdxBuild.absolutePath}'. " +
                "Set -Plibfdx.localPath=<path-to-libfdx>."
    }

    System.setProperty(
        "libfdx.compositeBuildDir",
        rootDir.resolve("build/libfdx-composite").canonicalPath
    )

    // Register the main jBox3D build as a composite provider. :box3d:core
    // exposes the matching Maven group/name, so Gradle's standard composite
    // substitution is also visible inside the included libFDX build.
    includeBuild(".")

    val libfdxModules = mapOf(
        "application" to ":libfdx:framework:application",
        "backend_android" to ":libfdx:backends:android",
        "backend_desktop" to ":libfdx:backends:desktop",
        "backend_web" to ":libfdx:backends:web",
        "box3d_ext" to ":libfdx:extensions:physics:box3d:core",
        "camera" to ":libfdx:framework:camera",
        "d3d12_core" to ":libfdx:extensions:graphics:d3d12:core",
        "display" to ":libfdx:framework:display",
        "g3d" to ":libfdx:framework:g3d",
        "gl_desktop" to ":libfdx:extensions:graphics:gl:platform:desktop",
        "gl_web" to ":libfdx:extensions:graphics:gl:platform:web",
        "graphics" to ":libfdx:framework:graphics",
        "ui_kit" to ":libfdx:framework:ui-kit",
        "vulkan_android_jni" to ":libfdx:extensions:graphics:vulkan:platform:android_jni",
        "vulkan_desktop" to ":libfdx:extensions:graphics:vulkan:platform:desktop",
        "wgpu_android_jni" to ":libfdx:extensions:graphics:wgpu:platform:android_jni",
        "wgpu_core" to ":libfdx:extensions:graphics:wgpu:core",
        "wgpu_desktop_jni" to ":libfdx:extensions:graphics:wgpu:platform:desktop_jni",
        "wgpu_web" to ":libfdx:extensions:graphics:wgpu:platform:web"
    )
    includeBuild(libfdxBuild) {
        dependencySubstitution {
            libfdxModules.forEach { (artifact, projectPath) ->
                substitute(module("io.github.libfdx:$artifact")).using(project(projectPath))
            }
        }
    }
}
