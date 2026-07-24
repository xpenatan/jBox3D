pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if(requested.id.id == "com.github.xpenatan.jparser") {
                useModule("com.github.xpenatan.jParser:jparser-gradle-plugin:${requested.version}")
            }
        }
    }

    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            metadataSources {
                mavenPom()
                artifact()
                ignoreGradleMetadataRedirection()
            }
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
include(":box3d:download")
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
include(":extensions:gdx:gl")
include(":extensions:fdx")

include(":samples:shared")
include(":samples:gdx:core")
include(":samples:gdx:gl:core")
include(":samples:gdx:gl:platforms:desktop-jni")
include(":samples:gdx:gl:platforms:desktop-ffm")
include(":samples:gdx:gl:platforms:desktop-c")
include(":samples:gdx:gl:platforms:web")
include(":samples:gdx:gl:platforms:android")
include(":samples:fdx:core")
include(":samples:fdx:platforms:desktop-jni")
include(":samples:fdx:platforms:desktop-ffm")
include(":samples:fdx:platforms:desktop-c")
include(":samples:fdx:platforms:web")
include(":samples:fdx:platforms:android")
