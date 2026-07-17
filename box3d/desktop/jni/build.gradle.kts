plugins {
    id("java-library")
}

val moduleName = "desktop-jni"
group = "${LibExt.groupId}.desktop"

base {
    archivesName.set(moduleName)
}

val nativeRoot = file("$projectDir/../../builder/build/c++/libs")
val nativePaths = listOf(
    "$nativeRoot/windows/vc/jni/box3d64.dll",
    "$nativeRoot/linux/jni/libbox3d64.so",
    "$nativeRoot/mac/jni/libbox3d64.dylib",
    "$nativeRoot/mac/arm/jni/libbox3darm64.dylib"
)

tasks.named<Jar>("jar") {
    from(provider {
        nativePaths.map(::file).filter { it.exists() }
    })
}

tasks.named<Copy>("processTestResources") {
    from(provider {
        nativePaths.map(::file).filter { it.exists() }
    })
}

val currentPlatformJniBuildTask = when {
    System.getProperty("os.name").lowercase().contains("windows") && System.getProperty("os.arch").contains("64") -> ":box3d:builder:jParser_build_windows64_jni"
    System.getProperty("os.name").lowercase().contains("linux") && System.getProperty("os.arch").contains("64") -> ":box3d:builder:jParser_build_linux64_jni"
    System.getProperty("os.name").lowercase().contains("mac") && System.getProperty("os.arch").lowercase().contains("aarch64") -> ":box3d:builder:jParser_build_macArm_jni"
    System.getProperty("os.name").lowercase().contains("mac") && System.getProperty("os.arch").lowercase().contains("arm64") -> ":box3d:builder:jParser_build_macArm_jni"
    System.getProperty("os.name").lowercase().contains("mac") && System.getProperty("os.arch").contains("64") -> ":box3d:builder:jParser_build_mac64_jni"
    else -> null
}

tasks.named<Copy>("processTestResources") {
    currentPlatformJniBuildTask?.let { dependsOn(it) }
}

tasks.named<Test>("test") {
    currentPlatformJniBuildTask?.let { dependsOn(it) }
}

dependencies {
    api(project(":box3d:shared:jni"))

    implementation("com.github.xpenatan.jParser:runtime-desktop-jni_windows_x64:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:runtime-desktop-jni_linux_x64:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:runtime-desktop-jni_mac_x64:${LibExt.jParserVersion}")
    implementation("com.github.xpenatan.jParser:runtime-desktop-jni_mac_arm64:${LibExt.jParserVersion}")

    testImplementation("junit:junit:${LibExt.jUnitVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaMainTarget)
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
