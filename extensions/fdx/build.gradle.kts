plugins {
    id("java-library")
}

val moduleName = "fdx"

base {
    archivesName.set(moduleName)
}

dependencies {
    compileOnly(project(":box3d:core"))
    api("${LibExt.fdxGroup}:graphics:${LibExt.fdxVersion}")
    api("${LibExt.fdxGroup}:g3d:${LibExt.fdxVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaFFMTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaFFMTarget)
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
