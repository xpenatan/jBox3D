import com.github.xpenatan.jParser.builder.targets.SourceLanguage
import com.github.xpenatan.jParser.gradle.JParserBuildTask
import com.github.xpenatan.jParser.gradle.JParserTargets
import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.file.RelativePath
import java.io.File

plugins {
    id("java")
    id("java-library")
    id("de.undercouch.download") version "5.4.0"
    id("com.github.xpenatan.jparser")
}

fun File.normalizedPath(): String {
    return absolutePath.replace('\\', '/')
}

val buildDirFile = layout.buildDirectory.get().asFile
val box3dArchiveFile = File(buildDirFile, "box3d-source.zip")
val box3dSourceRoot = File(buildDirFile, "box3d-source")
val box3dIncludeDir = File(box3dSourceRoot, "include")
val box3dPrivateSourceDir = File(box3dSourceRoot, "src")
val box3dCustomSourceDir = file("src/main/cpp/custom")
val box3dSourcePattern = "${box3dPrivateSourceDir.normalizedPath()}/*.c"
val box3dTimerSource = "${box3dPrivateSourceDir.normalizedPath()}/timer.c"
val box3dWebTimerSource = File(box3dCustomSourceDir, "box3d_web_timer.c")
val generatedJavaDirs = listOf(
    file("../base/src/main/java"),
    file("../core/src/main/java"),
    file("../shared/jni/src/main/java"),
    file("../shared/c/src/main/java"),
    file("../desktop/ffm/src/main/java"),
    file("../web/wasm/src/main/java")
)

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
}

tasks.register<Download>("box3d_download_source") {
    group = "box3d"
    description = "Download Box3D ${LibExt.box3dVersion} source into the build directory."
    src("https://github.com/erincatto/box3d/archive/refs/tags/v${LibExt.box3dVersion}.zip")
    dest(box3dArchiveFile)
    onlyIf {
        !box3dIncludeDir.isDirectory || !box3dPrivateSourceDir.isDirectory
    }
    outputs.dir(box3dSourceRoot)

    doFirst {
        delete(box3dSourceRoot)
    }

    doLast {
        copy {
            from(zipTree(box3dArchiveFile)) {
                eachFile {
                    val strippedSegments = relativePath.segments.drop(1)
                    if(strippedSegments.isEmpty()) {
                        exclude()
                    }
                    else {
                        relativePath = RelativePath(!isDirectory, *strippedSegments.toTypedArray())
                    }
                }
                includeEmptyDirs = false
            }
            into(box3dSourceRoot)
        }
        delete(box3dArchiveFile)
    }
}

val jParserGenerateArgs = listOf("gen_jni", "gen_ffm", "gen_web", "gen_teavm_c")
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
val androidArmV7TargetName = "armeabi_v7a"

jParser {
    libName.set("box3d")
    modulePrefix.set("")
    modulePath.set(file("..").absolutePath)
    moduleBuildSuffix.set("builder")
    moduleBaseSuffix.set("base")
    moduleCoreSuffix.set("core")
    moduleJNISuffix.set("shared/jni")
    moduleFFMSuffix.set("desktop/ffm")
    moduleWebSuffix.set("web/wasm")
    moduleCSuffix.set("shared/c")
    packageName.set("com.github.xpenatan.box3d")
    cppSourcePath.set(box3dSourceRoot.normalizedPath())
    sourceLanguage.set(SourceLanguage.C)
    cStandard.set("c17")

    native {
        dependsOn("box3d_download_source")
        headerDir(box3dIncludeDir.normalizedPath())
        headerDir(box3dPrivateSourceDir.normalizedPath())
        headerDir(box3dCustomSourceDir.normalizedPath())
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
                    androidTarget(androidArmV7TargetName) {
                        compileFlag("-DBOX3D_DISABLE_SIMD")
                    }
                }
                if(targetName == JParserTargets.WEB_WASM) {
                    cppExclude(box3dTimerSource)
                    cppInclude(box3dWebTimerSource.normalizedPath())
                    compileFlag("-msimd128")
                    compileFlag("-msse2")
                    linkerFlag("-msimd128")
                    linkerFlag("-msse2")
                }
            }
        }
    }
}

val prepareGeneratedDirs = tasks.register("box3d_prepare_generated_dirs") {
    group = "box3d"
    description = "Create generated Java source directories expected by jParser."

    doLast {
        generatedJavaDirs.forEach { dir ->
            dir.mkdirs()
        }
    }
}

tasks.named<JParserBuildTask>("jParser_generate") {
    dependsOn(prepareGeneratedDirs)
    buildArgs.set(jParserGenerateArgs)
}

fun registerBox3DAlias(aliasName: String, targetTaskName: String) {
    tasks.register(aliasName) {
        group = "box3d"
        description = "Alias for $targetTaskName."
        dependsOn(targetTaskName)
    }
}

registerBox3DAlias("box3d_build_project", "jParser_generate")
jParserTargetNames.forEach { targetName ->
    val aliasTargetName = targetName.replace("_teavm_c", "_c")
    registerBox3DAlias("box3d_build_project_$aliasTargetName", "jParser_build_$targetName")
}
