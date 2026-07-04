import org.gradle.api.attributes.java.TargetJvmVersion
plugins {
    id("java")
}

val box3dRuntimeName = "c"
val box3dRuntimeProject = ":box3d:desktop:c"

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaFFMTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaFFMTarget)
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
        attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, LibExt.javaFFMTarget.toInt())
    }
}

val box3dRuntimeClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    implementation(project(":samples:fdx:core"))
    implementation("${LibExt.fdxGroup}:backend_desktop:${LibExt.fdxVersion}")
    implementation("${LibExt.fdxGroup}:wgpu_core:${LibExt.fdxVersion}")

    glRuntimeClasspath("${LibExt.fdxGroup}:gl_desktop:${LibExt.fdxVersion}")
    vulkanRuntimeClasspath("${LibExt.fdxGroup}:vulkan_desktop:${LibExt.fdxVersion}")
    wgpuJniRuntimeClasspath("${LibExt.fdxGroup}:wgpu_desktop_jni:${LibExt.fdxVersion}")
    box3dRuntimeClasspath(project(box3dRuntimeProject))
}

fun Task.configureRuntimeInputs(providerClasspath: FileCollection) {
    dependsOn("$box3dRuntimeProject:jar")
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
