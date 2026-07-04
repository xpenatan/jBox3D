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
    implementation(project(":samples:gdx:core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:${LibExt.gdxVersion}")
    implementation("com.badlogicgames.gdx:gdx-platform:${LibExt.gdxVersion}:natives-desktop")

    box3dRuntimeClasspath(project(box3dRuntimeProject))
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.gdx.desktop.Box3DGdxDesktopLauncher"

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
    useJava25Launcher()
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
