plugins {
    id("java-library")
}

dependencies {
    implementation(project(":samples:shared"))
    compileOnlyApi(project(":box3d:core"))
    api(libs.gdxCore)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
}
