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

val glRuntimeClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val vulkanRuntimeClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

val wgpuJniRuntimeClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes {
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, libs.versions.javaFFM.get().toInt())
    }
}

val box3dRuntimeClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    implementation(project(":samples:fdx:core"))
    implementation(libs.bundles.fdxDesktopCore)

    glRuntimeClasspath(libs.fdxGlDesktop)
    vulkanRuntimeClasspath(libs.fdxVulkanDesktop)
    wgpuJniRuntimeClasspath(libs.fdxWGPUDesktopJni)
    box3dRuntimeClasspath(project(box3dRuntimeProject))
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.fdx.desktop.Box3DFdxDesktopLauncher"

fun Task.configureRuntimeInputs(providerClasspath: FileCollection) {
    dependsOn("$box3dRuntimeProject:jar")
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
        "jbox3d.sample.autoThrowAfterFrames"
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
