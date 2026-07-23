plugins {
    id("java")
    alias(libs.plugins.easyPublishing)
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

val publishingModules = listOf(
    ":box3d:core",
    ":box3d:shared:jni",
    ":box3d:shared:c",
    ":box3d:desktop:jni",
    ":box3d:desktop:ffm",
    ":box3d:desktop:c",
    ":box3d:web:wasm",
    ":box3d:android:jni",
    ":box3d:android:c",
    ":extensions:gdx:gl",
    ":extensions:gdx:wgpu",
    ":extensions:fdx",
)

easyPublishing {
    modules(publishingModules)

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
