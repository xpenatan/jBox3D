plugins {
    id("java-library")
}

val moduleName = "gdx-gl"

base {
    archivesName.set(moduleName)
}

dependencies {
    compileOnly(project(":box3d:core"))
    api("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")

    testImplementation(project(":box3d:core"))
    testImplementation("junit:junit:${LibExt.jUnitVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
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
