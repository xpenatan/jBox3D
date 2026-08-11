import org.teavm.gradle.api.OptimizationLevel

plugins {
    alias(libs.plugins.gdxTeaVM)
}

dependencies {
    implementation(libs.gdxCore)
    implementation(project(":samples:gdx:gl:core"))
    implementation(project(":box3d:desktop:c"))
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaWeb.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaWeb.get())
}

val sampleMainClass = "com.github.xpenatan.box3d.sample.gdx.desktop.Box3DGdxDesktopLauncher"
val sharedSampleResourcesDir = project(":samples:shared").layout.buildDirectory.dir("resources/main")

gdxTeaVM {
    assets.from(project(":samples:gdx:core").projectDir.resolve("src/main/resources"))
    assets.from(sharedSampleResourcesDir)

    reflection.add("com.badlogic.gdx.math.Vector2")
    reflection.add("com.badlogic.gdx.math.Vector3")

    glfw {
        mainClass.set(sampleMainClass)
        targetFileName.set("jbox3d-gdx")
        optimization.set(OptimizationLevel.AGGRESSIVE)
        obfuscated.set(false)
        minHeapSizeMb.set(128)
        maxHeapSizeMb.set(1024)
        buildType.set("Debug")
        consoleLog.set(true)
    }
}

tasks.named("generateC") {
    dependsOn(":samples:shared:processResources")
}
