plugins {
    id("com.android.library")
}

val moduleName = "android-c"
group = "${LibExt.groupId}.android"
val cLibsDir = "$projectDir/../../builder/build/c++/libs/android"
val stagedJniLibsDir = layout.buildDirectory.dir("generated/cJniLibs")
val androidAbis = listOf("x86", "x86_64", "armeabi-v7a", "arm64-v8a")

val stageCJniLibs by tasks.registering(Copy::class) {
    androidAbis.forEach { abi ->
        from("$cLibsDir/$abi/teavm_c") {
            include("*.so")
            into(abi)
        }
    }
    into(stagedJniLibsDir)
}

android {
    namespace = "com.github.xpenatan.box3d.android.c"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }

    sourceSets {
        named("main") {
            jniLibs.srcDirs(stagedJniLibsDir)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
        targetCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    publishing {
        singleVariant("release")
    }
}

val mavenSourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
    from(rootProject.file("README.md")) {
        into("META-INF")
    }
}

val mavenJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(rootProject.file("README.md")) {
        into("META-INF")
    }
}

tasks.matching { task ->
    task.name == "mergeReleaseJniLibFolders" || task.name == "mergeDebugJniLibFolders"
}.configureEach {
    dependsOn(stageCJniLibs)
}

dependencies {
    api(project(":box3d:shared:c"))
    api("com.github.xpenatan.jParser:runtime-android-c:${LibExt.jParserVersion}")
    runtimeOnly("com.github.xpenatan.jParser:runtime-android-c_x86:${LibExt.jParserVersion}")
    runtimeOnly("com.github.xpenatan.jParser:runtime-android-c_x86_64:${LibExt.jParserVersion}")
    runtimeOnly("com.github.xpenatan.jParser:runtime-android-c_armeabi_v7a:${LibExt.jParserVersion}")
    runtimeOnly("com.github.xpenatan.jParser:runtime-android-c_arm64_v8a:${LibExt.jParserVersion}")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            groupId = LibExt.groupId
            version = LibExt.libVersion
            artifact(mavenSourcesJar)
            artifact(mavenJavadocJar)
        }
    }
}

afterEvaluate {
    publishing {
        publications.named<MavenPublication>("maven") {
            from(components["release"])
        }
    }
}
