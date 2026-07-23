plugins {
    id("java-library")
}

dependencies {
    api(project(":samples:gdx:core"))
    api(project(":extensions:gdx:gl"))
    api(libs.gdxCore)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
}
