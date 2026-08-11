import org.gradle.api.file.RelativePath
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    id("java")
}

val box3dSourceRef = providers.gradleProperty("box3dSource")
    .orElse(libs.versions.box3dSource)
    .get()
val box3dCommitHashPattern = Regex("[0-9a-fA-F]{7,40}")
val box3dArchiveRef = if(box3dCommitHashPattern.matches(box3dSourceRef)) {
    box3dSourceRef
}
else {
    "refs/tags/v$box3dSourceRef"
}

val buildDirFile = layout.buildDirectory.get().asFile
val box3dSourceRoot = buildDirFile.resolve("box3d-source")
val box3dIncludeDir = box3dSourceRoot.resolve("include")
val box3dPrivateSourceDir = box3dSourceRoot.resolve("src")
val box3dArchiveFile = buildDirFile.resolve("tmp/box3d-source.zip")
val box3dSourceRefFile = box3dSourceRoot.resolve(".box3d-source-ref")
val box3dOriginalSampleBuildDir = buildDirFile.resolve("box3d-original-sample")
val box3dOriginalSampleBuildType = providers.gradleProperty("box3dSampleBuildType").orElse("Release")
val box3dCmakeExecutable = providers.gradleProperty("box3dCmakeExecutable").orElse("cmake")

fun originalSampleExecutable(buildType: String): File {
    val executableName = if(System.getProperty("os.name").lowercase().contains("win")) {
        "samples.exe"
    }
    else {
        "samples"
    }
    val candidates = listOf(
        box3dOriginalSampleBuildDir.resolve("bin/$buildType/$executableName"),
        box3dOriginalSampleBuildDir.resolve("bin/$executableName")
    )
    return candidates.firstOrNull { it.isFile }
        ?: throw GradleException(
            "The original Box3D sample executable was not found. Checked: " +
                    candidates.joinToString { it.absolutePath }
        )
}

val downloadBox3DSource = tasks.register("box3d_download_source") {
    group = "box3d"
    description = "Download Box3D $box3dSourceRef source into the build directory."
    inputs.property("box3dSourceRef", box3dSourceRef)
    outputs.dir(box3dSourceRoot)

    doLast {
        val url = "https://github.com/erincatto/box3d/archive/$box3dArchiveRef.zip"
        println("URL: $url")
        delete(box3dSourceRoot)
        box3dArchiveFile.parentFile.mkdirs()
        URI(url).toURL().openStream().use { input ->
            Files.copy(input, box3dArchiveFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        copy {
            from(zipTree(box3dArchiveFile)) {
                eachFile {
                    val strippedSegments = relativePath.segments.drop(1)
                    if(strippedSegments.isEmpty()) {
                        exclude()
                    }
                    else {
                        relativePath = RelativePath(!isDirectory, *strippedSegments.toTypedArray())
                    }
                }
                includeEmptyDirs = false
            }
            into(box3dSourceRoot)
        }
        check(box3dIncludeDir.isDirectory && box3dPrivateSourceDir.isDirectory) {
            "Downloaded Box3D archive does not contain the expected include and src directories."
        }
        box3dSourceRefFile.writeText("$box3dSourceRef\n")
        delete(box3dArchiveFile)
    }
}

val configureOriginalBox3DSample = tasks.register<Exec>("box3d_c_sample_configure") {
    group = "box3d"
    description = "Configure the original Box3D native sample application for $box3dSourceRef."
    dependsOn(downloadBox3DSource)

    inputs.property("box3dSourceRef", box3dSourceRef)
    inputs.property("box3dSampleBuildType", box3dOriginalSampleBuildType)
    inputs.property("box3dCmakeExecutable", box3dCmakeExecutable)
    inputs.file(box3dSourceRoot.resolve("CMakeLists.txt"))
    inputs.file(box3dSourceRoot.resolve("samples/CMakeLists.txt"))
    outputs.file(box3dOriginalSampleBuildDir.resolve("CMakeCache.txt"))

    doFirst {
        box3dOriginalSampleBuildDir.mkdirs()
        commandLine(
            box3dCmakeExecutable.get(),
            "-S", box3dSourceRoot.absolutePath,
            "-B", box3dOriginalSampleBuildDir.absolutePath,
            "-DBOX3D_SAMPLES=ON",
            "-DBOX3D_UNIT_TESTS=OFF",
            "-DBOX3D_BENCHMARKS=OFF",
            "-DBOX3D_DOCS=OFF",
            "-DBOX3D_BUILD_SHADERS=OFF",
            "-DCMAKE_BUILD_TYPE=${box3dOriginalSampleBuildType.get()}"
        )
    }
}

val buildOriginalBox3DSample = tasks.register<Exec>("box3d_c_sample_build") {
    group = "box3d"
    description = "Build the original Box3D native sample application for $box3dSourceRef."
    dependsOn(configureOriginalBox3DSample)

    inputs.property("box3dSampleBuildType", box3dOriginalSampleBuildType)
    inputs.property("box3dCmakeExecutable", box3dCmakeExecutable)

    doFirst {
        commandLine(
            box3dCmakeExecutable.get(),
            "--build", box3dOriginalSampleBuildDir.absolutePath,
            "--target", "samples",
            "--config", box3dOriginalSampleBuildType.get(),
            "--parallel"
        )
    }
    doLast {
        logger.lifecycle("Original Box3D sample: ${originalSampleExecutable(box3dOriginalSampleBuildType.get())}")
    }
}

tasks.register<Exec>("box3d_c_sample_run") {
    group = "box3d"
    description = "Build and run the original Box3D native sample application for $box3dSourceRef."
    dependsOn(buildOriginalBox3DSample)

    doFirst {
        val arguments = mutableListOf<String>()
        providers.gradleProperty("box3dSampleIndex").orNull?.let {
            arguments.addAll(listOf("--sample", it))
        }
        providers.gradleProperty("box3dSampleFrames").orNull?.let {
            arguments.addAll(listOf("--frames", it))
        }
        workingDir(box3dSourceRoot)
        commandLine(originalSampleExecutable(box3dOriginalSampleBuildType.get()).absolutePath, *arguments.toTypedArray())
    }
}
