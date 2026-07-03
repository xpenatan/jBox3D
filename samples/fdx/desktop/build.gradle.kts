import org.gradle.api.attributes.java.TargetJvmVersion
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("java")
}

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

val box3dJniRuntimeClasspath by configurations.creating {
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
    box3dJniRuntimeClasspath(project(":box3d:desktop:jni"))
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.fdx.desktop.Box3DFdxDesktopLauncher"

fun Task.configureRuntimeInputs(
    providerClasspath: FileCollection
) {
    dependsOn(":box3d:desktop:jni:jar")
    inputs.files(providerClasspath)
    inputs.files(box3dJniRuntimeClasspath)
}

fun JavaExec.configureSampleRun(
    descriptionText: String,
    graphics: String,
    graphicsLabel: String,
    providerClasspath: FileCollection
) {
    group = "samples"
    description = descriptionText
    mainClass.set(sampleMainClass)
    classpath = box3dJniRuntimeClasspath + sourceSets["main"].runtimeClasspath + providerClasspath
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
    if(JavaVersion.current().majorVersion.toInt() >= 22) {
        jvmArgs("--enable-native-access=ALL-UNNAMED")
    }
}

fun JavaExec.useJava25Launcher() {
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(LibExt.javaFFMTarget.toInt()))
    })
    jvmArgs("--enable-native-access=ALL-UNNAMED")
}

fun registerDesktopSampleBuild(
    taskName: String,
    descriptionText: String,
    providerClasspath: FileCollection
) {
    tasks.register(taskName) {
        group = "samples"
        description = descriptionText
        dependsOn("classes")
        configureRuntimeInputs(providerClasspath)
    }
}

registerDesktopSampleBuild("box3d_fdx_desktop_gl_jni_build",
    "Builds the jBox3D libfdx desktop OpenGL sample with Box3D JNI.", glRuntimeClasspath)
registerDesktopSampleBuild("box3d_fdx_desktop_wgpu_jni_build",
    "Builds the jBox3D libfdx desktop WGPU sample with Box3D JNI.", wgpuJniRuntimeClasspath)
registerDesktopSampleBuild("box3d_fdx_desktop_vulkan_jni_build",
    "Builds the jBox3D libfdx desktop Vulkan sample with Box3D JNI.", vulkanRuntimeClasspath)

tasks.register<JavaExec>("box3d_fdx_desktop_gl_jni_run") {
    configureSampleRun("Runs the jBox3D libfdx desktop OpenGL sample with Box3D JNI.",
        "gl", "OpenGL", glRuntimeClasspath)
    dependsOn("box3d_fdx_desktop_gl_jni_build")
    useJava25Launcher()
}

tasks.register<JavaExec>("box3d_fdx_desktop_wgpu_jni_run") {
    configureSampleRun("Runs the jBox3D libfdx desktop WGPU sample with Box3D JNI.",
        "wgpu", "WGPU JNI", wgpuJniRuntimeClasspath)
    dependsOn("box3d_fdx_desktop_wgpu_jni_build")
    useJava25Launcher()
}

tasks.register<JavaExec>("box3d_fdx_desktop_vulkan_jni_run") {
    configureSampleRun("Runs the jBox3D libfdx desktop Vulkan sample with Box3D JNI.",
        "vulkan", "Vulkan", vulkanRuntimeClasspath)
    dependsOn("box3d_fdx_desktop_vulkan_jni_build")
    useJava25Launcher()
}
