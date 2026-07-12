plugins {
    id("com.github.xpenatan.gdx-teavm")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
}

dependencies {
    implementation(project(":samples:gdx:wgpu:core"))
    implementation(project(":box3d:web:wasm"))
    implementation("com.github.xpenatan.jParser:runtime-web:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jWebGPU:webgpu-core:${LibExt.jWebGPUVersion}")
    implementation("com.github.xpenatan.jWebGPU:webgpu-web:${LibExt.jWebGPUVersion}")
    implementation("com.github.xpenatan.jWebGPU:webgpu-web_wasm:${LibExt.jWebGPUVersion}")
    implementation("io.github.monstroussoftware.gdx-webgpu:backend-teavm:${LibExt.gdxWebGPUVersion}")
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.gdx.wgpu.web.Box3DGdxWgpuWebLauncher"

gdxTeaVM {
    assets.from(project(":samples:gdx:shared").projectDir.resolve("src/main/resources"))
    assets.from(project(":samples:shared").projectDir.resolve("src/main/resources"))

    js {
        mainClass.set(sampleMainClass)
        htmlTitle.set("jBox3D libGDX WebGPU - Web JS")
        htmlWidth.set(0)
        htmlHeight.set(0)
        serverPort.set(8083)
        processMemory.set(2048)
        obfuscated.set(false)
    }
    wasm {
        mainClass.set(sampleMainClass)
        htmlTitle.set("jBox3D libGDX WebGPU - Web Wasm")
        htmlWidth.set(0)
        htmlHeight.set(0)
        serverPort.set(8084)
        processMemory.set(2048)
        obfuscated.set(false)
        strict.set(false)
    }
}
