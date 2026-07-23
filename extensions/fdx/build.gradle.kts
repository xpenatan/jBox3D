plugins {
    id("java-library")
}

val moduleName = "fdx"

base {
    archivesName.set(moduleName)
}

dependencies {
    compileOnly(project(":box3d:core"))
    api(libs.bundles.fdxExtension)

    testImplementation(project(":box3d:core"))
    testImplementation(libs.junit)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaFFM.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaFFM.get())
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
