import java.io.File
import java.util.Properties

object LibExt {
    const val groupId = "com.github.xpenatan.jBox3D"
    const val libName = "jBox3D"
    var isRelease = false
    var libVersion: String = ""
        get() {
            return getVersion()
        }

    const val javaMainTarget = "1.8"
    const val javaGdxWebGPUTarget = "11"
    const val javaWebTarget = "17"
    const val javaFFMTarget = "25"

    // Library dependencies
    const val box3dVersion = "0.1.0"
    const val jParserVersion = "-SNAPSHOT"
    const val jWebGPUVersion = "-SNAPSHOT"
    const val fdxGroup = "io.github.libfdx"
    const val fdxVersion = "-SNAPSHOT"
    const val gdxVersion = "1.14.2"
    const val gdxWebGPUVersion = "-SNAPSHOT"
    const val gdxTeaVMVersion = "1.6.0"

    // Sample dependencies
    const val sampleVersion = "-SNAPSHOT"
    const val useRepoLibs = false

    // Test dependencies
    const val jUnitVersion = "4.12"
}

private fun getVersion(): String {
    var libVersion = "-SNAPSHOT"
    val file = File("gradle.properties")
    if(file.exists()) {
        val properties = Properties()
        properties.load(file.inputStream())
        val version = properties.getProperty("version")
        if(LibExt.isRelease) {
            libVersion = version
        }
    }
    else {
        if(LibExt.isRelease) {
            throw RuntimeException("properties should exist")
        }
    }
    return libVersion
}
