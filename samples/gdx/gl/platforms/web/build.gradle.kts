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
