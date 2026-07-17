import java.net.HttpURLConnection
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Base64

val libraryProjects = setOf(
    project(":box3d:core"),
    project(":box3d:shared:jni"),
    project(":box3d:shared:c"),
    project(":box3d:desktop:jni"),
    project(":box3d:desktop:ffm"),
    project(":box3d:desktop:c"),
    project(":box3d:web:wasm"),
    project(":box3d:android:jni"),
    project(":box3d:android:c"),
    project(":extensions:gdx:gl"),
    project(":extensions:gdx:wgpu"),
    project(":extensions:fdx"),
)

val requestedTaskNames = gradle.startParameter.taskNames
fun isTaskRequested(taskName: String): Boolean {
    return requestedTaskNames.any { it == taskName || it.endsWith(":$taskName") }
}

val isPrepareSnapshotDeploy = isTaskRequested("prepareSnapshotDeploy")
val isSnapshotPublish = isTaskRequested("publishSnapshot")
val isDirectRepositoryPublish = requestedTaskNames.any { requestedTask ->
    requestedTask == "publish" ||
        requestedTask.endsWith(":publish") ||
        requestedTask.contains("ToCentralRepository")
}
val isReleasePublish = isTaskRequested("publishRelease")
val isPrepareReleaseDeploy = isTaskRequested("prepareReleaseDeploy")
val isZipStagingDeploy = isTaskRequested("zipStagingDeploy")
val isUploadToMavenCentral = isTaskRequested("uploadToMavenCentral")
val isTestRelease = isTaskRequested("publishTestRelease")
val releaseProperty = providers.gradleProperty("jBox3D.release")
    .map { it.toBoolean() }
    .orElse(false)
val isReleasePublishing = isReleasePublish ||
    isPrepareReleaseDeploy ||
    isZipStagingDeploy ||
    isUploadToMavenCentral ||
    isTestRelease
val isReleaseIntent = isReleasePublishing || releaseProperty.get()

if(isPrepareSnapshotDeploy && isReleaseIntent) {
    throw GradleException("prepareSnapshotDeploy cannot be combined with -PjBox3D.release=true")
}

LibExt.isRelease = isReleaseIntent

val signingKey = System.getenv("SIGNING_KEY").orEmpty()
val signingPassword = System.getenv("SIGNING_PASSWORD").orEmpty()
val centralUsername = System.getenv("CENTRAL_PORTAL_USERNAME").orEmpty()
val centralPassword = System.getenv("CENTRAL_PORTAL_PASSWORD").orEmpty()

if(signingKey.isEmpty() != signingPassword.isEmpty()) {
    throw GradleException("SIGNING_KEY and SIGNING_PASSWORD must either both be set or both be absent")
}

if(isReleasePublishing && (signingKey.isEmpty() || signingPassword.isEmpty())) {
    throw GradleException("Release publishing requires SIGNING_KEY and SIGNING_PASSWORD")
}

if((isSnapshotPublish || isDirectRepositoryPublish || isReleasePublish || isUploadToMavenCentral) &&
    (centralUsername.isEmpty() || centralPassword.isEmpty())) {
    throw GradleException(
        "Remote publishing requires CENTRAL_PORTAL_USERNAME and CENTRAL_PORTAL_PASSWORD"
    )
}

val cleanStagingDeploy = tasks.register<Delete>("cleanStagingDeploy") {
    group = "publishing"
    description = "Delete previously staged Maven release artifacts."
    delete(
        rootProject.layout.buildDirectory.dir("staging-deploy"),
        rootProject.layout.buildDirectory.file("staging-deploy.zip"),
    )
    outputs.upToDateWhen { false }
}

val cleanSnapshotDeploy = tasks.register<Delete>("cleanSnapshotDeploy") {
    group = "publishing"
    description = "Delete previously staged local Maven snapshot artifacts."
    delete(rootProject.layout.buildDirectory.dir("snapshot-deploy"))
    outputs.upToDateWhen { false }
}

val validateNativeArtifacts = tasks.register("validateNativeArtifacts") {
    group = "publishing"
    description = "Verify that every native runtime required by the Maven publications is present."

    doLast {
        val nativeRoot = rootProject.file("box3d/builder/build/c++/libs")
        val expectedFiles = mutableListOf(
            "windows/vc/jni/box3d64.dll",
            "windows/vc/ffm/box3d64.dll",
            "windows/vc/teavm_c/box3d64.dll",
            "linux/jni/libbox3d64.so",
            "linux/ffm/libbox3d64.so",
            "linux/teavm_c/libbox3d64.so",
            "mac/jni/libbox3d64.dylib",
            "mac/arm/jni/libbox3darm64.dylib",
            "mac/ffm/libbox3d64.dylib",
            "mac/arm/ffm/libbox3darm64.dylib",
            "mac/teavm_c/libbox3d64.dylib",
            "mac/arm/teavm_c/libbox3darm64.dylib",
            "emscripten/box3d.js",
            "emscripten/box3d.wasm",
        )
        listOf("x86", "x86_64", "armeabi-v7a", "arm64-v8a").forEach { abi ->
            expectedFiles += "android/$abi/libbox3d.so"
            expectedFiles += "android/$abi/teavm_c/libbox3d.so"
        }

        val missingFiles = expectedFiles.filterNot { nativeRoot.resolve(it).isFile }
        if(missingFiles.isNotEmpty()) {
            throw GradleException(
                "Cannot publish an incomplete native artifact set. Missing:\n - " +
                    missingFiles.joinToString("\n - ")
            )
        }

        logger.lifecycle("Validated ${expectedFiles.size} required native runtime files.")
    }
}

tasks.register("validateReleaseDependencies") {
    group = "publishing"
    description = "Verify that the immutable upstream versions required by a release are on Maven Central."

    doLast {
        val releaseCoordinates = listOf(
            Triple(LibExt.fdxGroup, "graphics", LibExt.fdxReleaseVersion),
            Triple(LibExt.fdxGroup, "g3d", LibExt.fdxReleaseVersion),
            Triple(
                "io.github.monstroussoftware.gdx-webgpu",
                "gdx-webgpu",
                LibExt.gdxWebGPUReleaseVersion,
            ),
        )
        val missingCoordinates = releaseCoordinates.filterNot { (groupId, artifactId, version) ->
            val groupPath = groupId.replace('.', '/')
            val pomUrl = URI(
                "https://repo1.maven.org/maven2/$groupPath/$artifactId/$version/" +
                    "$artifactId-$version.pom"
            ).toURL()
            val connection = pomUrl.openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "HEAD"
                connection.connectTimeout = 15_000
                connection.readTimeout = 15_000
                connection.responseCode == HttpURLConnection.HTTP_OK
            }
            finally {
                connection.disconnect()
            }
        }

        if(missingCoordinates.isNotEmpty()) {
            throw GradleException(
                "Publish the required upstream releases to Maven Central first:\n - " +
                    missingCoordinates.joinToString("\n - ") { (groupId, artifactId, version) ->
                        "$groupId:$artifactId:$version"
                    }
            )
        }

        logger.lifecycle("Validated ${releaseCoordinates.size} upstream release coordinates.")
    }
}

configure(libraryProjects) {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    if(LibExt.libVersion.isBlank()) {
        throw GradleException("Version cannot be empty")
    }

    extensions.configure<PublishingExtension> {
        repositories {
            maven {
                name = "central"
                val isSnapshot = LibExt.libVersion.endsWith("-SNAPSHOT")
                url = when {
                    !isSnapshot -> uri(rootProject.layout.buildDirectory.dir("staging-deploy"))
                    isPrepareSnapshotDeploy -> uri(rootProject.layout.buildDirectory.dir("snapshot-deploy"))
                    else -> uri("https://central.sonatype.com/repository/maven-snapshots/")
                }

                if(isSnapshot && !isPrepareSnapshotDeploy) {
                    credentials {
                        username = centralUsername
                        password = centralPassword
                    }
                }
            }
        }

        publications.withType<MavenPublication>().configureEach {
            pom {
                name.set(LibExt.libName)
                description.set("Box3D Java bindings")
                url.set("https://github.com/xpenatan/jBox3D")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("Xpe")
                        name.set("Natan")
                        url.set("https://github.com/xpenatan")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/xpenatan/jBox3D.git")
                    developerConnection.set("scm:git:ssh://git@github.com/xpenatan/jBox3D.git")
                    url.set("https://github.com/xpenatan/jBox3D")
                }
            }
        }
    }

    tasks.withType<Javadoc>().configureEach {
        options.encoding = "UTF-8"
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }

    tasks.withType<PublishToMavenRepository>().configureEach {
        if(isSnapshotPublish || isDirectRepositoryPublish || isReleaseIntent) {
            dependsOn(rootProject.tasks.named("validateNativeArtifacts"))
        }
        if(isReleaseIntent) {
            dependsOn(rootProject.tasks.named("validateReleaseDependencies"))
        }
        when {
            isReleaseIntent -> dependsOn(rootProject.tasks.named("cleanStagingDeploy"))
            isPrepareSnapshotDeploy -> dependsOn(rootProject.tasks.named("cleanSnapshotDeploy"))
        }
    }

    extensions.configure<SigningExtension> {
        isRequired = isReleasePublishing
        if(signingKey.isNotEmpty() && signingPassword.isNotEmpty()) {
            useInMemoryPgpKeys(signingKey, signingPassword)
        }
        sign(extensions.getByType<PublishingExtension>().publications)
    }
}

tasks.register<Zip>("zipStagingDeploy") {
    group = "publishing"
    description = "Stage all signed release artifacts in a Central Portal deployment bundle."
    dependsOn(libraryProjects.map { it.tasks.named("publish") })
    from(rootProject.layout.buildDirectory.dir("staging-deploy"))
    archiveFileName.set("staging-deploy.zip")
    destinationDirectory.set(rootProject.layout.buildDirectory)
    onlyIf { !LibExt.libVersion.endsWith("-SNAPSHOT") }
}

tasks.register("uploadToMavenCentral") {
    group = "publishing"
    description = "Upload the staged release bundle to Maven Central and publish it automatically."
    dependsOn("zipStagingDeploy")
    onlyIf { !LibExt.libVersion.endsWith("-SNAPSHOT") }

    doLast {
        val stagingDir = rootProject.layout.buildDirectory.dir("staging-deploy").get().asFile
        val zipFile = rootProject.layout.buildDirectory.file("staging-deploy.zip").get().asFile

        if(!stagingDir.isDirectory) {
            throw GradleException("Staging directory ${stagingDir.absolutePath} does not exist")
        }
        if(!zipFile.isFile || !Files.isReadable(Paths.get(zipFile.absolutePath))) {
            throw GradleException("Release bundle ${zipFile.absolutePath} does not exist or is not readable")
        }

        val bearerToken = Base64.getEncoder().encodeToString(
            "$centralUsername:$centralPassword".toByteArray(StandardCharsets.UTF_8)
        )
        val bundleName = URLEncoder.encode(
            "${LibExt.libName}-${LibExt.libVersion}",
            StandardCharsets.UTF_8,
        )

        providers.exec {
            commandLine(
                "curl",
                "--fail-with-body",
                "--silent",
                "--show-error",
                "--request", "POST",
                "--header", "Authorization: Bearer $bearerToken",
                "--form", "bundle=@${zipFile.absolutePath};type=application/octet-stream",
                "https://central.sonatype.com/api/v1/publisher/upload" +
                    "?name=$bundleName&publishingType=AUTOMATIC",
            )
        }.result.get()
    }
}

tasks.register("prepareReleaseDeploy") {
    group = "publishing"
    description = "Prepare the signed Maven Central release bundle without uploading it."
    dependsOn("zipStagingDeploy")
    onlyIf { !LibExt.libVersion.endsWith("-SNAPSHOT") }
}

tasks.register("publishRelease") {
    group = "publishing"
    description = "Stage, upload, validate, and automatically publish a release through Central Portal."
    dependsOn("uploadToMavenCentral")
}

tasks.register("publishTestRelease") {
    group = "publishing"
    description = "Build the signed release bundle locally without uploading it."
    dependsOn("prepareReleaseDeploy")
}

tasks.register("publishSnapshot") {
    group = "publishing"
    description = "Publish all snapshot artifacts to the Central Portal snapshot repository."
    dependsOn(libraryProjects.map { it.tasks.withType<PublishToMavenRepository>() })
}

tasks.register("prepareSnapshotDeploy") {
    group = "publishing"
    description = "Publish all snapshot artifacts to a local repository under build/snapshot-deploy."
    dependsOn(libraryProjects.map { it.tasks.withType<PublishToMavenRepository>() })
    onlyIf { LibExt.libVersion.endsWith("-SNAPSHOT") }
}
