plugins {
    id("java-library")
}

val moduleName = "desktop-c"
group = "${LibExt.groupId}.desktop"

base {
    archivesName.set(moduleName)
}

val nativeRoot = file("$projectDir/../../builder/build/c++/libs")
data class NativeResource(val sourcePath: String, val resourceDir: String = "")

val nativeResources = listOf(
    NativeResource("$nativeRoot/windows/vc/teavm_c/box3d64.dll"),
    NativeResource("$nativeRoot/linux/teavm_c/libbox3d64.so"),
    NativeResource("$nativeRoot/mac/teavm_c/libbox3d64.dylib"),
    NativeResource("$nativeRoot/mac/arm/teavm_c/libbox3darm64.dylib"),
    NativeResource(
        "$nativeRoot/windows/vc/teavm_c/box3d64_.lib",
        "external_cpp/jparser/box3d/native/windows_x64"
    ),
    NativeResource(
        "$nativeRoot/linux/teavm_c/libbox3d64_.a",
        "external_cpp/jparser/box3d/native/linux_x64"
    ),
    NativeResource(
        "$nativeRoot/mac/teavm_c/libbox3d64_.a",
        "external_cpp/jparser/box3d/native/mac_x64"
    ),
    NativeResource(
        "$nativeRoot/mac/arm/teavm_c/libbox3darm64_.a",
        "external_cpp/jparser/box3d/native/mac_arm64"
    )
)

tasks.named<Jar>("jar") {
    nativeResources.forEach { resource ->
        from(provider { listOf(file(resource.sourcePath)).filter { it.exists() } }) {
            if(resource.resourceDir.isNotEmpty()) {
                into(resource.resourceDir)
            }
        }
    }
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
