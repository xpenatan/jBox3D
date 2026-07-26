plugins {
    id("java")
    alias(libs.plugins.fdx)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaFFM.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaFFM.get())
}

dependencies {
    implementation(project(":samples:fdx:core"))
    implementation(project(":box3d:web:wasm"))
    implementation(libs.bundles.fdxWeb)
}

libfdx {
    js {
        mainClass.set("com.github.xpenatan.box3d.sample.fdx.web.Box3DFdxWebJsLauncher")
        htmlTitle.set("jBox3D libfdx - WebGL JS")
        canvasId.set("libfdx-canvas")
        htmlWidth.set(0)
        htmlHeight.set(0)
    }
    wasm {
        mainClass.set("com.github.xpenatan.box3d.sample.fdx.web.Box3DFdxWebWasmLauncher")
        htmlTitle.set("jBox3D libfdx - WebGL Wasm")
        canvasId.set("libfdx-canvas")
        htmlWidth.set(0)
        htmlHeight.set(0)
    }
}

val jsWebappDir = layout.buildDirectory.dir("dist/web-js/webapp")
val wasmWebappDir = layout.buildDirectory.dir("dist/web-wasm/webapp")

fun registerBox3DRuntimeScriptCopy(
    taskName: String,
    prepareTaskName: String,
    webappDir: Provider<Directory>
): TaskProvider<Task> {
    val runtimeClasspath = configurations.named("runtimeClasspath")
    return tasks.register(taskName) {
        dependsOn(prepareTaskName, ":box3d:web:wasm:jar")
        inputs.files(runtimeClasspath)
        outputs.file(webappDir.map { it.file("scripts/box3d.js") })
        outputs.file(webappDir.map { it.file("scripts/box3d.wasm") })
        doLast {
            val scriptsDir = webappDir.get().dir("scripts").asFile
            val scriptNames = setOf("box3d.js", "box3d.wasm")
            project.delete(scriptNames.map { File(scriptsDir, it) })
            project.copy {
                runtimeClasspath.get().files.forEach { entry ->
                    when {
                        entry.isDirectory -> from(entry) {
                            include("box3d.js", "box3d.wasm")
                        }
                        entry.isFile && entry.extension == "jar" -> from(zipTree(entry)) {
                            include("**/box3d.js", "**/box3d.wasm")
                            eachFile {
                                relativePath = RelativePath(true, name)
                            }
                            includeEmptyDirs = false
                        }
                    }
                }
                into(scriptsDir)
            }
            val missing = scriptNames.filterNot { File(scriptsDir, it).isFile }
            if(missing.isNotEmpty()) {
                throw GradleException(
                    "Missing jBox3D web runtime scripts: ${missing.joinToString()}. " +
                            "Run :box3d:builder:jParser_build_web_wasm before building the web sample."
                )
            }
        }
    }
}

val copyBox3DJsRuntimeScripts = registerBox3DRuntimeScriptCopy(
    "copyBox3DJsRuntimeScripts",
    "libfdx_web_js_prepare",
    jsWebappDir
)
val copyBox3DWasmRuntimeScripts = registerBox3DRuntimeScriptCopy(
    "copyBox3DWasmRuntimeScripts",
    "libfdx_web_wasm_prepare",
    wasmWebappDir
)

tasks.register("box3d_fdx_webgl_js_build") {
    group = "samples"
    description = "Builds the jBox3D libfdx WebGL JavaScript sample."
    dependsOn("libfdx_web_js_build", copyBox3DJsRuntimeScripts)
}

tasks.register("box3d_fdx_webgl_wasm_build") {
    group = "samples"
    description = "Builds the jBox3D libfdx WebGL Wasm sample."
    dependsOn("libfdx_web_wasm_build", copyBox3DWasmRuntimeScripts)
}

tasks.register("box3d_fdx_webgpu_js_build") {
    group = "samples"
    description = "Builds the jBox3D libfdx WebGPU JavaScript sample."
    dependsOn("libfdx_web_js_build", copyBox3DJsRuntimeScripts)
    configureWebGpuPage("dist/web-js/webapp", "jBox3D libfdx - WebGPU JS")
}

tasks.register("box3d_fdx_webgpu_wasm_build") {
    group = "samples"
    description = "Builds the jBox3D libfdx WebGPU Wasm sample."
    dependsOn("libfdx_web_wasm_build", copyBox3DWasmRuntimeScripts)
    configureWebGpuPage("dist/web-wasm/webapp", "jBox3D libfdx - WebGPU Wasm")
}

tasks.register<io.github.libfdx.gradle.LibfdxRunWebTask>("box3d_fdx_webgl_js_run") {
    group = "samples"
    description = "Builds and serves the jBox3D libfdx WebGL JavaScript sample."
    dependsOn("box3d_fdx_webgl_js_build")
    webappDir.set(jsWebappDir)
    port.set(libfdx.js.serverPort)
    defaultPath.set("/")
}

tasks.register<io.github.libfdx.gradle.LibfdxRunWebTask>("box3d_fdx_webgl_wasm_run") {
    group = "samples"
    description = "Builds and serves the jBox3D libfdx WebGL Wasm sample."
    dependsOn("box3d_fdx_webgl_wasm_build")
    webappDir.set(wasmWebappDir)
    port.set(libfdx.wasm.serverPort)
    defaultPath.set("/")
}

tasks.register<io.github.libfdx.gradle.LibfdxRunWebTask>("box3d_fdx_webgpu_js_run") {
    group = "samples"
    description = "Builds and serves the jBox3D libfdx WebGPU JavaScript sample."
    dependsOn("box3d_fdx_webgpu_js_build")
    webappDir.set(jsWebappDir)
    port.set(libfdx.js.serverPort)
    defaultPath.set("/webgpu.html")
}

tasks.register<io.github.libfdx.gradle.LibfdxRunWebTask>("box3d_fdx_webgpu_wasm_run") {
    group = "samples"
    description = "Builds and serves the jBox3D libfdx WebGPU Wasm sample."
    dependsOn("box3d_fdx_webgpu_wasm_build")
    webappDir.set(wasmWebappDir)
    port.set(libfdx.wasm.serverPort)
    defaultPath.set("/webgpu.html")
}

fun Task.configureWebGpuPage(webappPath: String, title: String) {
    val webappDir = layout.buildDirectory.dir(webappPath)
    val indexFile = webappDir.map { it.file("index.html") }
    val loaderFile = webappDir.map { it.file("scripts/fdx-loader.js") }
    val outputFile = webappDir.map { it.file("webgpu.html") }
    val outputLoaderFile = webappDir.map { it.file("scripts/fdx-webgpu-loader.js") }
    inputs.files(indexFile, loaderFile)
    outputs.files(outputFile, outputLoaderFile)
    doLast {
        writeWebGpuPage(
            indexFile.get().asFile,
            loaderFile.get().asFile,
            outputFile.get().asFile,
            outputLoaderFile.get().asFile,
            title
        )
    }
}

fun writeWebGpuPage(
    indexFile: File,
    loaderFile: File,
    outputFile: File,
    outputLoaderFile: File,
    title: String
) {
    val source = indexFile.readText()
    val withTitle = source.replace(Regex("<title>.*</title>"), "<title>$title</title>")
    val defaultLoader = "scripts/fdx-loader.js"
    val webGpuLoader = "scripts/fdx-webgpu-loader.js"
    if(!withTitle.contains(defaultLoader)) {
        throw GradleException("Could not locate the libfdx loader in ${indexFile.absolutePath}")
    }
    outputFile.writeText(withTitle.replace(defaultLoader, webGpuLoader))

    val loaderSource = loaderFile.readText()
    val defaultArgs = "mainClassArgs: [],"
    if(!loaderSource.contains(defaultArgs)) {
        throw GradleException("Could not configure WebGPU arguments in ${loaderFile.absolutePath}")
    }
    val loaderWithGraphicsArg = loaderSource.replace(
        defaultArgs,
        "mainClassArgs: [\"--graphics=webgpu\"],"
    )
    val spreadEntryCall = "return entry.apply(root, config.mainClassArgs);"
    if(!loaderWithGraphicsArg.contains(spreadEntryCall)) {
        throw GradleException("Could not configure WebGPU entry arguments in ${loaderFile.absolutePath}")
    }
    outputLoaderFile.writeText(
        loaderWithGraphicsArg.replace(
            spreadEntryCall,
            "return entry(config.mainClassArgs);"
        )
    )
}
