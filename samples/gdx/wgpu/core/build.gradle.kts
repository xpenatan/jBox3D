plugins {
    id("java-library")
}

dependencies {
    api(project(":samples:gdx:core"))
    api(project(":extensions:gdx:wgpu"))
    api(libs.gdxWebGPUCore)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaGdxWebGPU.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaGdxWebGPU.get())
}
