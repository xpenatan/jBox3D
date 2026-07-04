plugins {
    id("java")
}

val box3dRuntimeName = "c"
val box3dRuntimeProject = ":box3d:desktop:c"
val box3dSharedCProject = ":box3d:shared:c"
val teaVMBuilderMainClass = "com.github.xpenatan.box3d.sample.gdx.desktop.Box3DGdxTeaVMBuilder"
val glfwBuildRoot = layout.buildDirectory.dir("dist/glfw")

val box3dRuntimeClasspath by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

fun currentHostBox3DCBuildTask(): String? {
    val osName = System.getProperty("os.name").lowercase()
    val osArch = System.getProperty("os.arch").lowercase()
    return when {
        osName.contains("windows") -> ":box3d:builder:box3d_build_project_windows64_c"
        osName.contains("linux") -> ":box3d:builder:box3d_build_project_linux64_c"
        osName.contains("mac") && (osArch.contains("aarch64") || osArch.contains("arm64")) ->
            ":box3d:builder:box3d_build_project_macArm_c"
        osName.contains("mac") -> ":box3d:builder:box3d_build_project_mac64_c"
        else -> null
    }
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    implementation(project(":samples:gdx:gl:core"))
    implementation(project(box3dRuntimeProject))
    implementation("com.github.xpenatan.gdx-teavm:backend-glfw:${LibExt.gdxTeaVMVersion}")

    box3dRuntimeClasspath(project(box3dRuntimeProject))
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
}

val prepareGdxTeaVMGlfwBuildRoot = tasks.register("prepareGdxTeaVMGlfwBuildRoot") {
    group = "samples"
    description = "Creates the gdx-teavm GLFW build root before the native build task validates inputs."
    inputs.property("box3dCMakeLayoutVersion", 7)
    outputs.dir(glfwBuildRoot)
    outputs.upToDateWhen { false }

    doLast {
        val buildRoot = glfwBuildRoot.get().asFile
        delete(buildRoot.resolve("c"))
        delete(buildRoot.resolve("build/cmake"))
        buildRoot.mkdirs()
    }
}

fun Task.configureRuntimeInputs() {
    dependsOn("classes")
    dependsOn(prepareGdxTeaVMGlfwBuildRoot)
    val nativeBuildTask = currentHostBox3DCBuildTask()
    if(nativeBuildTask != null) {
        dependsOn(nativeBuildTask)
        project(box3dSharedCProject).tasks.named("processResources") {
            mustRunAfter(nativeBuildTask)
        }
        project(box3dSharedCProject).tasks.named("jar") {
            mustRunAfter(nativeBuildTask)
        }
        project(box3dRuntimeProject).tasks.named("jar") {
            mustRunAfter(nativeBuildTask)
        }
    }
    dependsOn("$box3dRuntimeProject:jar")
    inputs.files(box3dRuntimeClasspath)
}

fun JavaExec.configureTeaVM(action: String, buildType: String = "Debug", console: Boolean = false) {
    mainClass.set(teaVMBuilderMainClass)
    classpath = sourceSets["main"].runtimeClasspath
    workingDir = projectDir
    maxHeapSize = "2048m"
    configureRuntimeInputs()
    args(buildType, action)
    if(console) {
        args("console")
    }
}

tasks.register<JavaExec>("box3d_gdx_desktop_${box3dRuntimeName}_generate") {
    group = "samples"
    description = "Generates the jBox3D libGDX desktop sample through gdx-teavm GLFW C."
    configureTeaVM("generate")
}

tasks.register<JavaExec>("box3d_gdx_desktop_${box3dRuntimeName}_build") {
    group = "samples"
    description = "Builds the jBox3D libGDX desktop sample through gdx-teavm GLFW C."
    configureTeaVM("build")
}

tasks.register<JavaExec>("box3d_gdx_desktop_${box3dRuntimeName}_run") {
    group = "samples"
    description = "Runs the jBox3D libGDX desktop sample through gdx-teavm GLFW C."
    configureTeaVM("run", console = true)
}
