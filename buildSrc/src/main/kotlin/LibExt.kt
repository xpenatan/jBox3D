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
    const val jParserVersion = "-SNAPSHOT"
    const val jWebGPUVersion = "-SNAPSHOT"
    const val fdxReleaseVersion = "0.0.2"
    const val fdxVersion = "-SNAPSHOT"
    const val gdxVersion = "1.14.2"
    const val gdxWebGPUReleaseVersion = "0.8.1"
    const val gdxWebGPUVersion = "-SNAPSHOT"
    const val gdxTeaVMVersion = "1.6.0"

    // Test dependencies
    const val jUnitVersion = "4.13.2"
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
