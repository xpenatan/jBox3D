import org.gradle.api.attributes.java.TargetJvmVersion
plugins {
    id("java")
}

val box3dRuntimeName = "c"
val box3dRuntimeProject = ":box3d:desktop:c"

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

fun Task.configureRuntimeInputs(providerClasspath: FileCollection) {
    if(!libs.versions.jbox3dUseMavenArtifacts.get().toBoolean()) {
        dependsOn("$box3dRuntimeProject:jar")
    }
    inputs.files(providerClasspath)
    inputs.files(box3dRuntimeClasspath)
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
    "Builds the jBox3D libfdx desktop OpenGL sample compile path with Box3D TeaVM C.", glRuntimeClasspath)
registerDesktopSampleBuild("box3d_fdx_desktop_wgpu_${box3dRuntimeName}_build",
    "Builds the jBox3D libfdx desktop WGPU sample compile path with Box3D TeaVM C.", wgpuJniRuntimeClasspath)
registerDesktopSampleBuild("box3d_fdx_desktop_vulkan_${box3dRuntimeName}_build",
    "Builds the jBox3D libfdx desktop Vulkan sample compile path with Box3D TeaVM C.", vulkanRuntimeClasspath)
registerDesktopSampleBuild("box3d_fdx_desktop_d3d12_${box3dRuntimeName}_build",
    "Builds the jBox3D libfdx desktop Direct3D 12 sample compile path with Box3D TeaVM C.", d3d12RuntimeClasspath)
