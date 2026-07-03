plugins {
    id("java-library")
}

dependencies {
    implementation(project(":samples:shared"))
    compileOnly(project(":box3d:core"))
    api(project(":extensions:gdx"))
    api("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
}
