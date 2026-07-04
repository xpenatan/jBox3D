plugins {
    id("java-library")
}

val moduleName = "shared-c"
group = "${LibExt.groupId}.shared"
val generatedTeaVMCResourcesDir = layout.buildDirectory.dir("generated/jparser/resources/main")

base {
    archivesName.set(moduleName)
}

dependencies {
    api("com.github.xpenatan.jParser:api-core:${LibExt.jParserVersion}")
    api("com.github.xpenatan.jParser:loader-core:${LibExt.jParserVersion}")
    api("com.github.xpenatan.jParser:runtime-core:${LibExt.jParserVersion}")
    api("com.github.xpenatan.jParser:runtime-c:${LibExt.jParserVersion}")

    api("org.teavm:teavm-core:${LibExt.teaVMVersion}")
    api("org.teavm:teavm-classlib:${LibExt.teaVMVersion}")
}

sourceSets {
    main {
        val generatedJavaRoot = file("src/main/java").toPath().toAbsolutePath().normalize()
        java.setSrcDirs(listOf("src/main/java", "src/manual/java"))
        java.exclude { element ->
            element.file.name == "JBox3DLoader.java" &&
                    element.file.toPath().toAbsolutePath().normalize().startsWith(generatedJavaRoot)
        }
        resources.setSrcDirs(listOf(generatedTeaVMCResourcesDir))
    }
}

tasks.named("clean") {
    doFirst {
        project.delete(files("$projectDir/src/main/java"))
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
