plugins {
    alias(libs.plugins.androidLibrary)
}

val moduleName = "android-jni"

android {
    enableKotlin = false
    namespace = "com.github.xpenatan.box3d.android.jni"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }

    sourceSets {
        named("main") {
            jniLibs.directories.add(file("$projectDir/../../builder/build/c++/libs/android").absolutePath)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
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

val mavenSourcesJar = tasks.register<Jar>("mavenSourcesJar") {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
    from(rootProject.file("README.md")) {
        into("META-INF")
    }
}

val mavenJavadocJar = tasks.register<Jar>("mavenJavadocJar") {
    archiveClassifier.set("javadoc")
    from(rootProject.file("README.md")) {
        into("META-INF")
    }
}

dependencies {
    api(project(":box3d:shared:jni"))
    api(libs.bundles.jParserAndroidJni)
    runtimeOnly(libs.jParserRuntimeAndroidX86)
    runtimeOnly(libs.jParserRuntimeAndroidX8664)
    runtimeOnly(libs.jParserRuntimeAndroidArmeabiV7a)
    runtimeOnly(libs.jParserRuntimeAndroidArm64V8a)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
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
