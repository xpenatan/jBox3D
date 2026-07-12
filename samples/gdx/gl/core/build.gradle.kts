plugins {
    id("java-library")
}

dependencies {
    api(project(":samples:gdx:shared"))
    api(project(":extensions:gdx:gl"))
    api("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
}
