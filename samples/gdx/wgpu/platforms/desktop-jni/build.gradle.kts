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
    implementation(project(":samples:gdx:wgpu:core"))
    implementation("io.github.monstroussoftware.gdx-webgpu:backend-desktop-jni:${LibExt.gdxWebGPUVersion}")

    box3dRuntimeClasspath(project(box3dRuntimeProject))
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaGdxWebGPUTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaGdxWebGPUTarget)
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.gdx.wgpu.desktop.Box3DGdxWgpuDesktopLauncher"

fun Task.configureRuntimeInputs() {
    dependsOn("$box3dRuntimeProject:jar")
    inputs.files(box3dRuntimeClasspath)
}

tasks.register("box3d_gdx_wgpu_desktop_${box3dRuntimeName}_build") {
    group = "samples"
    description = "Builds the jBox3D libGDX WebGPU desktop sample with Box3D ${box3dRuntimeName.uppercase()}."
    dependsOn("classes")
    configureRuntimeInputs()
}

tasks.register<JavaExec>("box3d_gdx_wgpu_desktop_${box3dRuntimeName}_run") {
    group = "samples"
    description = "Runs the jBox3D libGDX WebGPU desktop sample with Box3D ${box3dRuntimeName.uppercase()}."
    dependsOn("box3d_gdx_wgpu_desktop_${box3dRuntimeName}_build")
    mainClass.set(sampleMainClass)
    classpath = box3dRuntimeClasspath + sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    listOf(
        "jbox3d.sample.exitAfterFrames",
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
