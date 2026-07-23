plugins {
    id("java-library")
}

val moduleName = "gdx-wgpu"

base {
    archivesName.set(moduleName)
}

dependencies {
    compileOnly(project(":box3d:core"))
    api(project(":extensions:gdx:gl"))
    api(libs.gdxWebGPUCore)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaGdxWebGPU.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaGdxWebGPU.get())
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            from(components["java"])
        }
    }
}
