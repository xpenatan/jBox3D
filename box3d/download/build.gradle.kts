import org.gradle.api.file.RelativePath
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    id("java")
}

val box3dVersion = libs.versions.box3dSource.get()

val buildDirFile = layout.buildDirectory.get().asFile
val box3dSourceRoot = buildDirFile.resolve("box3d-source")
val box3dIncludeDir = box3dSourceRoot.resolve("include")
val box3dPrivateSourceDir = box3dSourceRoot.resolve("src")
val box3dArchiveFile = buildDirFile.resolve("tmp/box3d-source.zip")

tasks.register("box3d_download_source") {
    group = "box3d"
    description = "Download Box3D ${box3dVersion} source into the build directory."
    inputs.property("box3dVersion", box3dVersion)
    outputs.dir(box3dSourceRoot)
    onlyIf {
        !box3dIncludeDir.isDirectory || !box3dPrivateSourceDir.isDirectory
    }

    doLast {
        val url = "https://github.com/erincatto/box3d/archive/refs/tags/v${box3dVersion}.zip"
        println("URL: $url")
        delete(box3dSourceRoot)
        box3dArchiveFile.parentFile.mkdirs()
        URL(url).openStream().use { input ->
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
        delete(box3dArchiveFile)
    }
}
