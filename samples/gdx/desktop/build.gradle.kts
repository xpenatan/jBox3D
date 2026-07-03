plugins {
    id("java")
}

val box3dJniRuntimeClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    implementation(project(":samples:gdx:core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${LibExt.gdxVersion}")
    implementation("com.badlogicgames.gdx:gdx-platform:${LibExt.gdxVersion}:natives-desktop")

    box3dJniRuntimeClasspath(project(":box3d:desktop:jni"))
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.gdx.desktop.Box3DGdxDesktopLauncher"

fun Task.configureRuntimeInputs() {
    dependsOn(":box3d:desktop:jni:jar")
    inputs.files(box3dJniRuntimeClasspath)
}

tasks.register("box3d_gdx_desktop_jni_build") {
    group = "samples"
    description = "Builds the jBox3D libGDX desktop sample with Box3D JNI."
    dependsOn("classes")
    configureRuntimeInputs()
}

tasks.register<JavaExec>("box3d_gdx_desktop_jni_run") {
    group = "samples"
    description = "Runs the jBox3D libGDX desktop sample with Box3D JNI."
    dependsOn("box3d_gdx_desktop_jni_build")
    mainClass.set(sampleMainClass)
    classpath = box3dJniRuntimeClasspath + sourceSets["main"].runtimeClasspath
    System.getProperty("jbox3d.sample.exitAfterFrames")?.takeIf { it.isNotBlank() }?.let {
        systemProperty("jbox3d.sample.exitAfterFrames", it)
    }
    listOf(
        "jbox3d.sample.sample",
        "jbox3d.sample.sampleIndex",
        "jbox3d.sample.validateAll",
        "jbox3d.sample.autoThrowAfterFrames",
        "jbox3d.sample.screenshot",
        "jbox3d.sample.screenshotAfterFrames"
    ).forEach { property ->
        System.getProperty(property)?.takeIf { it.isNotBlank() }?.let {
            systemProperty(property, it)
        }
    }
}
