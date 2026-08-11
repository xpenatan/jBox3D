import org.gradle.api.attributes.java.TargetJvmVersion
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("java")
}

val box3dRuntimeName = "ffm"
val box3dRuntimeProject = ":box3d:desktop:ffm"

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaFFM.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaFFM.get())
}

val glRuntimeClasspath = configurations.create("glRuntimeClasspath") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val vulkanRuntimeClasspath = configurations.create("vulkanRuntimeClasspath") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val wgpuJniRuntimeClasspath = configurations.create("wgpuJniRuntimeClasspath") {
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes {
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, libs.versions.javaFFM.get().toInt())
    }
}

val box3dRuntimeClasspath = configurations.create("box3dRuntimeClasspath") {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val d3d12RuntimeClasspath = files()

dependencies {
    implementation(project(":samples:fdx:core"))
    implementation(libs.bundles.fdxDesktopCore)
    implementation(libs.fdxD3D12Core)

    glRuntimeClasspath(libs.fdxGlDesktop)
    vulkanRuntimeClasspath(libs.fdxVulkanDesktop)
    wgpuJniRuntimeClasspath(libs.fdxWGPUDesktopJni)
    box3dRuntimeClasspath(project(box3dRuntimeProject))
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.fdx.desktop.Box3DFdxDesktopLauncher"

fun Task.configureRuntimeInputs(providerClasspath: FileCollection) {
    if(!libs.versions.jbox3dUseMavenArtifacts.get().toBoolean()) {
        dependsOn("$box3dRuntimeProject:jar")
    }
    inputs.files(providerClasspath)
    inputs.files(box3dRuntimeClasspath)
}

fun JavaExec.configureSampleRun(descriptionText: String, graphics: String, graphicsLabel: String, providerClasspath: FileCollection) {
    group = "samples"
    description = descriptionText
    mainClass.set(sampleMainClass)
    classpath = box3dRuntimeClasspath + sourceSets["main"].runtimeClasspath + providerClasspath
    systemProperty("jbox3d.sample.graphics", graphics)
    systemProperty("jbox3d.sample.graphicsLabel", graphicsLabel)
    System.getProperty("jbox3d.sample.exitAfterFrames")?.takeIf { it.isNotBlank() }?.let {
        systemProperty("jbox3d.sample.exitAfterFrames", it)
    }
    System.getProperty("jbox3d.sample.visible")?.takeIf { it.isNotBlank() }?.let {
        systemProperty("jbox3d.sample.visible", it)
    }
    listOf(
        "jbox3d.sample.sample",
        "jbox3d.sample.sampleIndex",
        "jbox3d.sample.validateAll",
        "jbox3d.sample.autoThrowAfterFrames",
        "jbox3d.sample.screenshot",
        "jbox3d.sample.screenshotAfterFrames",
        "jbox3d.sample.debugView"
    ).forEach { property ->
        System.getProperty(property)?.takeIf { it.isNotBlank() }?.let {
            systemProperty(property, it)
        }
    }
}

fun JavaExec.useJava25Launcher() {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.javaFFM.get().toInt()))
    })
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

fun registerDesktopSampleBuild(taskName: String, descriptionText: String, providerClasspath: FileCollection) {
    tasks.register(taskName) {
        group = "samples"
        description = descriptionText
        dependsOn("classes")
        configureRuntimeInputs(providerClasspath)
    }
}

registerDesktopSampleBuild("box3d_fdx_desktop_gl_${box3dRuntimeName}_build",
    "Builds the jBox3D libfdx desktop OpenGL sample with Box3D ${box3dRuntimeName.uppercase()}.", glRuntimeClasspath)
registerDesktopSampleBuild("box3d_fdx_desktop_wgpu_${box3dRuntimeName}_build",
    "Builds the jBox3D libfdx desktop WGPU sample with Box3D ${box3dRuntimeName.uppercase()}.", wgpuJniRuntimeClasspath)
registerDesktopSampleBuild("box3d_fdx_desktop_vulkan_${box3dRuntimeName}_build",
    "Builds the jBox3D libfdx desktop Vulkan sample with Box3D ${box3dRuntimeName.uppercase()}.", vulkanRuntimeClasspath)
registerDesktopSampleBuild("box3d_fdx_desktop_d3d12_${box3dRuntimeName}_build",
    "Builds the jBox3D libfdx desktop Direct3D 12 sample with Box3D ${box3dRuntimeName.uppercase()}.", d3d12RuntimeClasspath)

tasks.register<JavaExec>("box3d_fdx_desktop_gl_${box3dRuntimeName}_run") {
    configureSampleRun("Runs the jBox3D libfdx desktop OpenGL sample with Box3D ${box3dRuntimeName.uppercase()}.",
        "gl", "OpenGL", glRuntimeClasspath)
    dependsOn("box3d_fdx_desktop_gl_${box3dRuntimeName}_build")
    useJava25Launcher()
}

tasks.register<JavaExec>("box3d_fdx_desktop_wgpu_${box3dRuntimeName}_run") {
    configureSampleRun("Runs the jBox3D libfdx desktop WGPU sample with Box3D ${box3dRuntimeName.uppercase()}.",
        "wgpu", "WGPU JNI", wgpuJniRuntimeClasspath)
    dependsOn("box3d_fdx_desktop_wgpu_${box3dRuntimeName}_build")
    useJava25Launcher()
}

tasks.register<JavaExec>("box3d_fdx_desktop_vulkan_${box3dRuntimeName}_run") {
    configureSampleRun("Runs the jBox3D libfdx desktop Vulkan sample with Box3D ${box3dRuntimeName.uppercase()}.",
        "vulkan", "Vulkan", vulkanRuntimeClasspath)
    dependsOn("box3d_fdx_desktop_vulkan_${box3dRuntimeName}_build")
    useJava25Launcher()
}

tasks.register<JavaExec>("box3d_fdx_desktop_d3d12_${box3dRuntimeName}_run") {
    configureSampleRun("Runs the jBox3D libfdx desktop Direct3D 12 sample with Box3D ${box3dRuntimeName.uppercase()}.",
        "d3d12", "Direct3D 12", d3d12RuntimeClasspath)
    dependsOn("box3d_fdx_desktop_d3d12_${box3dRuntimeName}_build")
    useJava25Launcher()
}
