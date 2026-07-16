import org.teavm.gradle.api.OptimizationLevel

plugins {
    id("com.github.xpenatan.gdx-teavm")
}

dependencies {
    implementation("com.badlogicgames.gdx:gdx:${LibExt.gdxVersion}")
    implementation(project(":samples:gdx:gl:core"))
    implementation(project(":box3d:desktop:c"))
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.gdx.desktop.Box3DGdxDesktopLauncher"

gdxTeaVM {
    assets.from(project(":samples:gdx:shared").projectDir.resolve("src/main/resources"))
    assets.from(project(":samples:shared").projectDir.resolve("src/main/resources"))

    reflection.add("com.badlogic.gdx.math.Vector2")
    reflection.add("com.badlogic.gdx.math.Vector3")

    glfw {
        mainClass.set(sampleMainClass)
        targetFileName.set("jbox3d-gdx")
        optimization.set(OptimizationLevel.AGGRESSIVE)
        obfuscated.set(false)
        minHeapSizeMb.set(64)
        maxHeapSizeMb.set(512)
        buildType.set("Debug")
        consoleLog.set(true)
    }
}
