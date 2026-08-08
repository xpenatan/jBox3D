import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    id("java-library")
}

val box3dSourceRoot = project(":box3d:download").layout.buildDirectory.dir("box3d-source")

dependencies {
    compileOnly(project(":box3d:core"))
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(":box3d:download:box3d_download_source")
    from(box3dSourceRoot) {
        include("data/**")
        include("samples/sample_issues.cpp")
    }
}
