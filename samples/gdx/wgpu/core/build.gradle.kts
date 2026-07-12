plugins {
    id("java-library")
}

dependencies {
    api(project(":samples:gdx:shared"))
    api(project(":extensions:gdx:wgpu"))
    api("io.github.monstroussoftware.gdx-webgpu:gdx-webgpu:${LibExt.gdxWebGPUVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaGdxWebGPUTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaGdxWebGPUTarget)
}
