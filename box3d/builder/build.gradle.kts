import com.github.xpenatan.jParser.builder.targets.AndroidTarget
import com.github.xpenatan.jParser.builder.targets.SourceLanguage
import com.github.xpenatan.jParser.gradle.JParserTargets
import java.io.File

plugins {
    id("java-library")
    id("com.github.xpenatan.jparser")
}

fun File.normalizedPath(): String {
    return absolutePath.replace('\\', '/')
}

val downloadBuildDir = file("../download/build")
val box3dSourceRoot = File(downloadBuildDir, "box3d-source")
val box3dIncludeDir = File(box3dSourceRoot, "include")
val box3dPrivateSourceDir = File(box3dSourceRoot, "src")
val box3dCustomSourceDir = file("src/main/cpp/custom")
val box3dSourcePattern = "${box3dPrivateSourceDir.normalizedPath()}/*.c"
val box3dTimerSource = File(box3dPrivateSourceDir, "timer.c")
val box3dWebTimerSource = File(box3dCustomSourceDir, "box3d_web_timer.c")

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
}

val jParserTargetNames = listOf(
    JParserTargets.WEB_WASM,
    JParserTargets.WINDOWS64_JNI,
    JParserTargets.LINUX64_JNI,
    JParserTargets.MAC64_JNI,
    JParserTargets.MAC_ARM_JNI,
    JParserTargets.ANDROID_JNI,
    JParserTargets.IOS_JNI,
    JParserTargets.WINDOWS64_FFM,
    JParserTargets.LINUX64_FFM,
    JParserTargets.MAC64_FFM,
    JParserTargets.MAC_ARM_FFM,
    JParserTargets.WINDOWS64_TEAVM_C,
    JParserTargets.LINUX64_TEAVM_C,
    JParserTargets.MAC64_TEAVM_C,
    JParserTargets.MAC_ARM_TEAVM_C,
    JParserTargets.ANDROID_TEAVM_C
)
val windowsTargetNames = setOf(
    JParserTargets.WINDOWS64_JNI,
    JParserTargets.WINDOWS64_FFM,
    JParserTargets.WINDOWS64_TEAVM_C
)
val linuxTargetNames = setOf(
    JParserTargets.LINUX64_JNI,
    JParserTargets.LINUX64_FFM,
    JParserTargets.LINUX64_TEAVM_C
)
val androidArmV7Target = AndroidTarget.Target.armeabi_v7a

jParser {
    libName.set("box3d")
    modulePrefix.set("")
    modulePath(file(".."))
    moduleBuildSuffix.set("builder")
    moduleBaseSuffix.set("base")
    moduleCoreSuffix.set("core")
    moduleJNISuffix.set("shared/jni")
    moduleFFMSuffix.set("desktop/ffm")
    moduleWebSuffix.set("web/wasm")
    moduleCSuffix.set("shared/c")
    packageName.set("com.github.xpenatan.box3d")
    cppSourcePath(box3dSourceRoot)
    sourceLanguage.set(SourceLanguage.C)
    cStandard.set("c17")

    native {
        dependsOn(":box3d:download:box3d_download_source")
        headerDir(box3dIncludeDir)
        headerDir(box3dPrivateSourceDir)
        headerDir(box3dCustomSourceDir)
        cppInclude(box3dSourcePattern)
        includeDefaultSources.set(false)
        includeCustomSources.set(false)

        jParserTargetNames.forEach { targetName ->
            target(targetName) {
                includeDefaultSources.set(false)
                includeCustomSources.set(false)
                if(targetName in windowsTargetNames) {
                    compileFlag("/MP2")
                    compileFlag("/Zm200")
                }
                else {
                    compileFlag("-ffp-contract=off")
                }
                if(targetName in linuxTargetNames) {
                    linkerFlag("-lm")
                }
                if(targetName == JParserTargets.ANDROID_JNI || targetName == JParserTargets.ANDROID_TEAVM_C) {
                    androidTarget(androidArmV7Target) {
                        compileFlag("-DBOX3D_DISABLE_SIMD")
                    }
                }
                if(targetName == JParserTargets.WEB_WASM) {
                    cppExclude(box3dTimerSource)
                    cppInclude(box3dWebTimerSource)
                    compileFlag("-msimd128")
                    compileFlag("-msse2")
                    linkerFlag("-msimd128")
                    linkerFlag("-msse2")
                }
            }
        }
    }
}
