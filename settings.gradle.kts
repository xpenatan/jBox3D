pluginManagement {
    repositories {
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        google()
        gradlePluginPortal()
        mavenCentral()
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
