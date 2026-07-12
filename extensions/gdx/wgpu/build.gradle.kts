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
    api("io.github.monstroussoftware.gdx-webgpu:gdx-webgpu:${LibExt.gdxWebGPUVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaGdxWebGPUTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaGdxWebGPUTarget)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            groupId = LibExt.groupId
            version = LibExt.libVersion
            from(components["java"])
        }
    }
}
