plugins {
    id("java")
    id("com.github.xpenatan.easy-publishing") version "0.1.0"
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }

    val kotlinVersion = "2.3.20"

    dependencies {
        classpath("com.android.tools.build:gradle:8.12.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
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
        resolutionStrategy.eachDependency {
            if(requested.group == "com.github.xpenatan.jParser") {
                useVersion(LibExt.jParserVersion)
            }
            else if(requested.group == "com.github.xpenatan.gdx-teavm") {
                useVersion(LibExt.gdxTeaVMVersion)
            }
            else if(requested.group == "com.github.xpenatan.jWebGPU") {
                useVersion(LibExt.jWebGPUVersion)
            }
        }
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

    groupId.set(LibExt.groupId)
    releaseVersion.set(providers.gradleProperty("version"))
    snapshotVersion.set(LibExt.snapshotVersion)

    snapshotRepositoryUrl.set("https://central.sonatype.com/repository/maven-snapshots/")
    releaseRepositoryUrl.set("https://central.sonatype.com")
    username.set(providers.environmentVariable("CENTRAL_PORTAL_USERNAME"))
    password.set(providers.environmentVariable("CENTRAL_PORTAL_PASSWORD"))
    signingKey.set(providers.environmentVariable("SIGNING_KEY"))
    signingPassword.set(providers.environmentVariable("SIGNING_PASSWORD"))
    automaticRelease.set(true)

    pomName.set(LibExt.libName)
    pomDescription.set("Box3D Java bindings")
    projectUrl.set("https://github.com/xpenatan/jBox3D")

    developerId.set("Xpe")
    developerName.set("Natan")

    scmUrl.set("https://github.com/xpenatan/jBox3D")
    scmConnection.set("scm:git:https://github.com/xpenatan/jBox3D.git")
    scmDeveloperConnection.set("scm:git:ssh://git@github.com/xpenatan/jBox3D.git")
}

val validateNativeArtifacts = tasks.register("validateNativeArtifacts") {
    group = "verification"
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

configure(publishingModules.map(::project)) {
    pluginManager.withPlugin("maven-publish") {
        extensions.configure<org.gradle.api.publish.PublishingExtension> {
            publications.withType<org.gradle.api.publish.maven.MavenPublication>().configureEach {
                setGroupId(LibExt.groupId)
            }
        }
    }

    tasks.withType<org.gradle.api.publish.maven.tasks.PublishToMavenRepository>().configureEach {
        dependsOn(validateNativeArtifacts)
    }
}
