plugins {
    id("java-library")
}

dependencies {
    compileOnly(project(":box3d:core"))
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
}
