plugins {
    id("java-library")
}

val moduleName = "fdx"

base {
    archivesName.set(moduleName)
}

dependencies {
    compileOnly(project(":box3d:core"))
    api("io.github.libfdx:graphics:${LibExt.fdxVersion}")
    api("io.github.libfdx:g3d:${LibExt.fdxVersion}")

    testImplementation(project(":box3d:core"))
    testImplementation("junit:junit:${LibExt.jUnitVersion}")
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
