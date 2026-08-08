import org.gradle.api.file.RelativePath
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

tasks.register("box3d_download_source") {
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
