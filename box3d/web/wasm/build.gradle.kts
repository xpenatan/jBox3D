plugins {
    id("java-library")
}

val moduleName = "web-wasm"

base {
    archivesName.set(moduleName)
}

val nativePaths = listOf(
    "$projectDir/../../builder/build/c++/libs/emscripten/box3d.js",
    "$projectDir/../../builder/build/c++/libs/emscripten/box3d.wasm"
)

tasks.named<Jar>("jar") {
    from(provider {
        nativePaths.map(::file).filter { it.exists() }
    })
}

dependencies {
    api(project(":box3d:core"))
    api("com.github.xpenatan.jParser:runtime-core:${LibExt.jParserVersion}")
    api("com.github.xpenatan.jParser:runtime-web:${LibExt.jParserVersion}")
    api("com.github.xpenatan.jParser:runtime-web_wasm:${LibExt.jParserVersion}")
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src/main/java", "src/main/support/java"))
        resources.setSrcDirs(listOf("src/main/resources"))
    }
}

tasks.named("clean") {
    doFirst {
        val srcPath = "$projectDir/src/main/java"
        project.delete(files(srcPath))
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
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
