plugins {
    id("java")
}

val box3dRuntimeName = "jni"
val box3dRuntimeProject = ":box3d:desktop:jni"
val box3dRuntimeClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    implementation(project(":samples:gdx:gl:core"))
    implementation(libs.gdxBackendLwjgl3)
    implementation(variantOf(libs.gdxPlatform) { classifier("natives-desktop") })

    box3dRuntimeClasspath(project(box3dRuntimeProject))
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.gdx.desktop.Box3DGdxDesktopLauncher"

fun Task.configureRuntimeInputs() {
    dependsOn("$box3dRuntimeProject:jar")
    inputs.files(box3dRuntimeClasspath)
}

tasks.register("box3d_gdx_desktop_${box3dRuntimeName}_build") {
    group = "samples"
    description = "Builds the jBox3D libGDX desktop sample with Box3D ${box3dRuntimeName.uppercase()}."
    dependsOn("classes")
    configureRuntimeInputs()
}

tasks.register<JavaExec>("box3d_gdx_desktop_${box3dRuntimeName}_run") {
    group = "samples"
    description = "Runs the jBox3D libGDX desktop sample with Box3D ${box3dRuntimeName.uppercase()}."
    dependsOn("box3d_gdx_desktop_${box3dRuntimeName}_build")
    mainClass.set(sampleMainClass)
    classpath = box3dRuntimeClasspath + sourceSets["main"].runtimeClasspath
    System.getProperty("jbox3d.sample.exitAfterFrames")?.takeIf { it.isNotBlank() }?.let {
        systemProperty("jbox3d.sample.exitAfterFrames", it)
    }
    listOf(
        "jbox3d.sample.sample",
        "jbox3d.sample.sampleIndex",
        "jbox3d.sample.validateAll",
        "jbox3d.sample.autoThrowAfterFrames",
        "jbox3d.sample.screenshot",
        "jbox3d.sample.screenshotAfterFrames",
        "jbox3d.sample.openSamplesMenu"
    ).forEach { property ->
        System.getProperty(property)?.takeIf { it.isNotBlank() }?.let {
            systemProperty(property, it)
        }
    }
}
