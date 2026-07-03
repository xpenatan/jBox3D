plugins {
    id("java-library")
}

val moduleName = "shared-c"
group = "${LibExt.groupId}.shared"

base {
    archivesName.set(moduleName)
}

dependencies {
    api("com.github.xpenatan.jParser:api-core:${LibExt.jParserVersion}")
    api("com.github.xpenatan.jParser:loader-core:${LibExt.jParserVersion}")
    api("com.github.xpenatan.jParser:runtime-c:${LibExt.jParserVersion}")

    api("org.teavm:teavm-core:${LibExt.teaVMVersion}")
    api("org.teavm:teavm-classlib:${LibExt.teaVMVersion}")
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src/main/java"))
        java.exclude("**/JBox3DLoader.java")
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
