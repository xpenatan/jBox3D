import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("java")
}

val box3dRuntimeName = "ffm"
val box3dRuntimeProject = ":box3d:desktop:ffm"
val box3dRuntimeClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    implementation(project(":samples:gdx:wgpu:core"))
    implementation("io.github.monstroussoftware.gdx-webgpu:backend-desktop-ffm:${LibExt.gdxWebGPUVersion}")

    box3dRuntimeClasspath(project(box3dRuntimeProject))
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaFFMTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaFFMTarget)
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.gdx.wgpu.desktop.Box3DGdxWgpuDesktopLauncher"

fun Task.configureRuntimeInputs() {
    dependsOn("$box3dRuntimeProject:jar")
    inputs.files(box3dRuntimeClasspath)
}

fun JavaExec.useJava25Launcher() {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(LibExt.javaFFMTarget.toInt()))
    })
    jvmArgs("--enable-native-access=ALL-UNNAMED")
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
    useJava25Launcher()
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
