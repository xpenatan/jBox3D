import java.io.File
import java.util.Properties

object LibExt {
    const val groupId = "com.github.xpenatan.jBox3D"
    const val libName = "jBox3D"
    var isRelease = false
    val libVersion: String
        get() = getVersion()

    const val javaMainTarget = "1.8"
    const val javaGdxWebGPUTarget = "11"
    const val javaWebTarget = "17"
    const val javaFFMTarget = "25"

    // Library dependencies
    const val box3dVersion = "0.1.0"
    const val jParserVersion = "1.2.4"
    const val jWebGPUReleaseVersion = "0.3.2"
    val jWebGPUVersion: String
        get() = releaseDependencyVersion(jWebGPUReleaseVersion)
    const val fdxGroup = "io.github.libfdx"
    const val fdxReleaseVersion = "0.0.2"
    val fdxVersion: String
        get() = releaseDependencyVersion(fdxReleaseVersion)
    const val gdxVersion = "1.14.2"
    const val gdxWebGPUReleaseVersion = "0.8.1"
    val gdxWebGPUVersion: String
        get() = releaseDependencyVersion(gdxWebGPUReleaseVersion)
    const val gdxTeaVMVersion = "1.6.0"

    // Test dependencies
    const val jUnitVersion = "4.13.2"
}

private fun releaseDependencyVersion(releaseVersion: String): String {
    return if(LibExt.isRelease) releaseVersion else "-SNAPSHOT"
}

private fun getVersion(): String {
    if(!LibExt.isRelease) {
        return "-SNAPSHOT"
    }

    val file = File("gradle.properties")
    if(!file.exists()) {
        throw RuntimeException("gradle.properties must exist for release builds")
    }

    val properties = Properties()
    file.inputStream().use(properties::load)
    return properties.getProperty("version")?.trim()?.takeIf(String::isNotEmpty)
        ?: throw RuntimeException("version is missing from gradle.properties")
}
