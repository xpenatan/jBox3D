plugins {
    id("java-library")
}

dependencies {
    compileOnly(project(":box3d:core"))
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
}
