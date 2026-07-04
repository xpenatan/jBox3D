plugins {
    id("java")
    id("com.github.xpenatan.gdx-teavm")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
}

dependencies {
    implementation(project(":samples:gdx:core"))
    implementation(project(":box3d:web:wasm"))
    implementation("com.github.xpenatan.gdx-teavm:backend-web:${LibExt.gdxTeaVMVersion}")
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.gdx.web.Box3DGdxWebLauncher"

gdxTeaVM {
    assets.from(project(":samples:gdx:core").projectDir.resolve("src/main/resources"))
    assets.from(project(":samples:shared").projectDir.resolve("src/main/resources"))

    js {
        mainClass.set(sampleMainClass)
        htmlTitle.set("jBox3D libGDX - Web JS")
        htmlWidth.set(0)
        htmlHeight.set(0)
        serverPort.set(8081)
        processMemory.set(2048)
        obfuscated.set(false)
    }
    wasm {
        mainClass.set(sampleMainClass)
        htmlTitle.set("jBox3D libGDX - Web Wasm")
        htmlWidth.set(0)
        htmlHeight.set(0)
        serverPort.set(8082)
        processMemory.set(2048)
        obfuscated.set(false)
        strict.set(false)
    }
}

val jsWebappDir = layout.buildDirectory.dir("dist/web/webapp")
val wasmWebappDir = layout.buildDirectory.dir("dist/wasm/webapp")
val pagesWebappDir = layout.buildDirectory.dir("pages")

fun registerBox3DRuntimeScriptCopy(
    taskName: String,
    buildTaskName: String,
    webappDir: Provider<Directory>
): TaskProvider<Task> {
    val runtimeClasspath = configurations.named("runtimeClasspath")
    return tasks.register(taskName) {
        dependsOn(":box3d:builder:box3d_build_project_web_wasm", buildTaskName, ":box3d:web:wasm:jar")
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
                            "Run :box3d:builder:box3d_build_project_web_wasm before building the web sample."
                )
            }
        }
    }
}

val copyBox3DJsRuntimeScripts = registerBox3DRuntimeScriptCopy(
    "copyBox3DJsRuntimeScripts",
    "gdx_teavm_web_js_build",
    jsWebappDir
)
val copyBox3DWasmRuntimeScripts = registerBox3DRuntimeScriptCopy(
    "copyBox3DWasmRuntimeScripts",
    "gdx_teavm_web_wasm_build",
    wasmWebappDir
)

afterEvaluate {
    tasks.named("gdx_teavm_web_js_run") {
        dependsOn(copyBox3DJsRuntimeScripts)
    }

    tasks.named("gdx_teavm_web_wasm_run") {
        dependsOn(copyBox3DWasmRuntimeScripts)
    }
}

tasks.register("box3d_gdx_web_js_build") {
    group = "samples"
    description = "Builds the jBox3D libGDX JavaScript web sample."
    dependsOn(copyBox3DJsRuntimeScripts)
}

tasks.register("box3d_gdx_web_wasm_build") {
    group = "samples"
    description = "Builds the jBox3D libGDX Wasm web sample."
    dependsOn(copyBox3DWasmRuntimeScripts)
}

tasks.register<Sync>("box3d_gdx_web_pages_build") {
    group = "samples"
    description = "Builds the jBox3D libGDX web samples in a GitHub Pages layout."
    dependsOn(copyBox3DJsRuntimeScripts, copyBox3DWasmRuntimeScripts)
    into(pagesWebappDir)
    from(jsWebappDir) {
        into("gdx/gl/js")
        exclude("WEB-INF/**")
    }
    from(wasmWebappDir) {
        into("gdx/gl/wasm")
        exclude("WEB-INF/**")
    }
    from(layout.projectDirectory.file("src/main/pages/index.html")) {
        into("")
    }
}

tasks.register("box3d_gdx_web_js_run") {
    group = "samples"
    description = "Builds and serves the jBox3D libGDX JavaScript web sample."
    dependsOn("gdx_teavm_web_js_run")
}

tasks.register("box3d_gdx_web_wasm_run") {
    group = "samples"
    description = "Builds and serves the jBox3D libGDX Wasm web sample."
    dependsOn("gdx_teavm_web_wasm_run")
}
