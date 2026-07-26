plugins {
    id("java")
    alias(libs.plugins.easyPublishing)
}

val jbox3dPublishedModules = linkedMapOf(
    ":box3d:core" to "core",
    ":box3d:shared:jni" to "shared-jni",
    ":box3d:shared:c" to "shared-c",
    ":box3d:desktop:jni" to "desktop-jni",
    ":box3d:desktop:ffm" to "desktop-ffm",
    ":box3d:desktop:c" to "desktop-c",
    ":box3d:web:wasm" to "web-wasm",
    ":box3d:android:jni" to "android-jni",
    ":box3d:android:c" to "android-c",
    ":extensions:gdx:gl" to "gdx-gl",
    ":extensions:fdx" to "fdx",
)

val useJBox3DMavenArtifacts = libs.versions.jbox3dUseMavenArtifacts.get().let { value ->
    value.toBooleanStrictOrNull()
        ?: throw GradleException(
            "jbox3dUseMavenArtifacts in gradle/libs.versions.toml must be either true or false."
        )
}
val jbox3dMavenGroup = libs.versions.jbox3dGroup.get()
val jbox3dMavenVersion = libs.versions.jbox3dMavenVersion.get()
    .also { version ->
        if(version.isBlank()) {
            throw GradleException("jbox3dMavenVersion in gradle/libs.versions.toml must not be blank.")
        }
    }

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven { url = uri("https://central.sonatype.com/repository/maven-snapshots/") }
        maven {
            url = uri("http://teavm.org/maven/repository/")
            isAllowInsecureProtocol = true
        }
    }

    configurations.configureEach {
        // Check for updates every sync
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}

if(useJBox3DMavenArtifacts) {
    logger.lifecycle("jBox3D samples: using Maven artifacts version $jbox3dMavenVersion")
    subprojects {
        if(path.startsWith(":samples:")) {
            configurations.configureEach {
                resolutionStrategy.dependencySubstitution {
                    jbox3dPublishedModules.forEach { (projectPath, artifactId) ->
                        substitute(project(projectPath))
                            .using(module("$jbox3dMavenGroup:$artifactId:$jbox3dMavenVersion"))
                            .because("jbox3dUseMavenArtifacts is enabled")
                    }
                }
            }
        }
    }
}

easyPublishing {
    modules(jbox3dPublishedModules.keys.toList())

    groupId.set(libs.versions.jbox3dGroup)
    releaseVersion.set(libs.versions.jbox3dRelease)
    snapshotVersion.set(libs.versions.jbox3dSnapshot)

    snapshotRepositoryUrl.set("https://central.sonatype.com/repository/maven-snapshots/")
    releaseRepositoryUrl.set("https://central.sonatype.com")
    username.set(providers.environmentVariable("CENTRAL_PORTAL_USERNAME"))
    password.set(providers.environmentVariable("CENTRAL_PORTAL_PASSWORD"))
    signingKey.set(providers.environmentVariable("SIGNING_KEY"))
    signingPassword.set(providers.environmentVariable("SIGNING_PASSWORD"))

    pomName.set(libs.versions.jbox3dName)
    pomDescription.set("Box3D Java bindings")
    projectUrl.set("https://github.com/xpenatan/jBox3D")

    developerId.set("Xpe")
    developerName.set("Natan")

    scmUrl.set("https://github.com/xpenatan/jBox3D")
    scmConnection.set("scm:git:https://github.com/xpenatan/jBox3D.git")
    scmDeveloperConnection.set("scm:git:ssh://git@github.com/xpenatan/jBox3D.git")
}
