pluginManagement {
    val jParserPluginVersion = "-SNAPSHOT"
    val gdxTeaVMPluginVersion = "-SNAPSHOT"

    resolutionStrategy {
        eachPlugin {
            if(requested.id.id == "com.github.xpenatan.jparser") {
                useModule("com.github.xpenatan.jParser:jparser-gradle-plugin:$jParserPluginVersion")
            }
        }
    }

    plugins {
        id("com.github.xpenatan.jparser") version jParserPluginVersion
        id("com.github.xpenatan.gdx-teavm") version gdxTeaVMPluginVersion
        id("io.github.libfdx") version "-SNAPSHOT"
    }

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
        }
        gradlePluginPortal()
        maven {
            url = uri("http://teavm.org/maven/repository/")
            isAllowInsecureProtocol = true
        }
    }
}

rootProject.name = "jBox3D"

include(":box3d:builder")
include(":box3d:base")
include(":box3d:core")
include(":box3d:shared:jni")
include(":box3d:shared:c")
include(":box3d:desktop:jni")
include(":box3d:desktop:ffm")
include(":box3d:desktop:c")
include(":box3d:web:wasm")
include(":box3d:android:jni")
include(":box3d:android:c")
include(":extensions:gdx")
include(":extensions:fdx")

include(":samples:shared")
include(":samples:gdx:core")
include(":samples:gdx:desktop")
include(":samples:gdx:web")
include(":samples:fdx:core")
include(":samples:fdx:desktop")
include(":samples:fdx:web")
include(":samples:fdx:android")
