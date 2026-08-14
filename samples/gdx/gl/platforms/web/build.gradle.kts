plugins {
    alias(libs.plugins.gdxTeaVM)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaWeb.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaWeb.get())
}

dependencies {
    implementation(project(":samples:gdx:gl:core"))
    implementation(project(":box3d:web:wasm"))
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.gdx.web.Box3DGdxWebLauncher"
val sharedSampleResourcesDir = project(":samples:shared").layout.buildDirectory.dir("resources/main")

gdxTeaVM {
    assets.from(project(":samples:gdx:core").projectDir.resolve("src/main/resources"))
    assets.from(sharedSampleResourcesDir)

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

tasks.matching { it.name == "generateJavaScript" || it.name == "generateWasmGC" }.configureEach {
    dependsOn(":samples:shared:processResources")
}

val pagesDirectory = layout.buildDirectory.dir("pages")
val fdxWebProject = project(":samples:fdx:platforms:web")

tasks.register<Sync>("box3d_web_samples_pages_build") {
    group = "samples"
    description = "Builds and stages all jBox3D browser samples for GitHub Pages."
    dependsOn(
        "gdx_teavm_web_js_build",
        "gdx_teavm_web_wasm_build",
        ":samples:fdx:platforms:web:box3d_fdx_webgl_js_build",
        ":samples:fdx:platforms:web:box3d_fdx_webgl_wasm_build",
        ":samples:fdx:platforms:web:box3d_fdx_webgpu_js_build",
        ":samples:fdx:platforms:web:box3d_fdx_webgpu_wasm_build"
    )

    into(pagesDirectory)
    from(layout.buildDirectory.dir("dist/js/webapp")) {
        into("gdx/gl/js")
        exclude("WEB-INF/**")
    }
    from(layout.buildDirectory.dir("dist/wasm/webapp")) {
        into("gdx/gl/wasm")
        exclude("WEB-INF/**")
    }
    from(fdxWebProject.layout.buildDirectory.dir("dist/web-js/webapp")) {
        into("fdx/gl/js")
        exclude("WEB-INF/**", "webgpu.html", "scripts/fdx-webgpu-loader.js")
    }
    from(fdxWebProject.layout.buildDirectory.dir("dist/web-wasm/webapp")) {
        into("fdx/gl/wasm")
        exclude("WEB-INF/**", "webgpu.html", "scripts/fdx-webgpu-loader.js")
    }
    from(fdxWebProject.layout.buildDirectory.dir("dist/web-js/webapp")) {
        into("fdx/webgpu/js")
        exclude("WEB-INF/**", "index.html", "scripts/fdx-loader.js")
        rename("webgpu\\.html", "index.html")
    }
    from(fdxWebProject.layout.buildDirectory.dir("dist/web-wasm/webapp")) {
        into("fdx/webgpu/wasm")
        exclude("WEB-INF/**", "index.html", "scripts/fdx-loader.js")
        rename("webgpu\\.html", "index.html")
    }
    from(layout.projectDirectory.dir("src/main/pages"))

    doLast {
        val requiredFiles = listOf(
            "index.html",
            "gdx/gl/js/index.html",
            "gdx/gl/js/app.js",
            "gdx/gl/js/scripts/box3d.js",
            "gdx/gl/js/scripts/box3d.wasm",
            "gdx/gl/wasm/index.html",
            "gdx/gl/wasm/app.wasm",
            "gdx/gl/wasm/app.wasm-runtime.js",
            "gdx/gl/wasm/scripts/box3d.js",
            "gdx/gl/wasm/scripts/box3d.wasm",
            "fdx/gl/js/index.html",
            "fdx/gl/js/app.js",
            "fdx/gl/js/scripts/fdx-loader.js",
            "fdx/gl/js/scripts/box3d.js",
            "fdx/gl/js/scripts/box3d.wasm",
            "fdx/gl/wasm/index.html",
            "fdx/gl/wasm/app.wasm",
            "fdx/gl/wasm/app.wasm-runtime.js",
            "fdx/gl/wasm/scripts/fdx-loader.js",
            "fdx/gl/wasm/scripts/box3d.js",
            "fdx/gl/wasm/scripts/box3d.wasm",
            "fdx/webgpu/js/index.html",
            "fdx/webgpu/js/app.js",
            "fdx/webgpu/js/scripts/fdx-webgpu-loader.js",
            "fdx/webgpu/js/scripts/box3d.js",
            "fdx/webgpu/js/scripts/box3d.wasm",
            "fdx/webgpu/wasm/index.html",
            "fdx/webgpu/wasm/app.wasm",
            "fdx/webgpu/wasm/app.wasm-runtime.js",
            "fdx/webgpu/wasm/scripts/fdx-webgpu-loader.js",
            "fdx/webgpu/wasm/scripts/box3d.js",
            "fdx/webgpu/wasm/scripts/box3d.wasm"
        )
        val pagesRoot = pagesDirectory.get()
        val missing = requiredFiles.filterNot { pagesRoot.file(it).asFile.isFile }
        if(missing.isNotEmpty()) {
            throw GradleException("Incomplete GitHub Pages site. Missing: ${missing.joinToString()}")
        }

        val forbiddenPaths = listOf(
            "fdx/js",
            "fdx/wasm",
            "fdx/gl/js/webgpu.html",
            "fdx/gl/wasm/webgpu.html",
            "fdx/webgpu/js/webgpu.html",
            "fdx/webgpu/wasm/webgpu.html"
        )
        val present = forbiddenPaths.filter { pagesRoot.file(it).asFile.exists() }
        if(present.isNotEmpty()) {
            throw GradleException("Unexpected legacy GitHub Pages paths: ${present.joinToString()}")
        }
    }
}
