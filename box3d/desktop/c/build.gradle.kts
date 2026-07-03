plugins {
    id("java-library")
}

val moduleName = "desktop-c"
group = "${LibExt.groupId}.desktop"

base {
    archivesName.set(moduleName)
}

val nativeRoot = file("$projectDir/../../builder/build/c++/libs")
val nativePaths = listOf(
    "$nativeRoot/windows/vc/teavm_c/box3d64.dll",
    "$nativeRoot/linux/teavm_c/libbox3d64.so",
    "$nativeRoot/mac/teavm_c/libbox3d64.dylib",
    "$nativeRoot/mac/arm/teavm_c/libbox3darm64.dylib"
)

tasks.named<Jar>("jar") {
    from(provider {
        nativePaths.map(::file).filter { it.exists() }
    })
}

dependencies {
    api(project(":box3d:shared:c"))

    implementation("com.github.xpenatan.jParser:runtime-c_windows_x64:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:runtime-c_linux_x64:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:runtime-c_mac_x64:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:runtime-c_mac_arm64:${LibExt.jParserVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaWebTarget)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            groupId = LibExt.groupId
            version = LibExt.libVersion
            from(components["java"])
        }
    }
}
