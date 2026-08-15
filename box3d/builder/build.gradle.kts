import com.github.xpenatan.jParser.builder.targets.AndroidTarget
import com.github.xpenatan.jParser.builder.targets.SourceLanguage
import com.github.xpenatan.jParser.gradle.JParserTargets
import java.io.File

plugins {
    id("java-library")
    alias(libs.plugins.jParser)
}

fun File.normalizedPath(): String {
    return absolutePath.replace('\\', '/')
}

val downloadBuildDir = file("../download/build")
val box3dSourceRoot = File(downloadBuildDir, "box3d-source")
val box3dIncludeDir = File(box3dSourceRoot, "include")
val box3dPrivateSourceDir = File(box3dSourceRoot, "src")
val box3dCustomSourceDir = file("src/main/cpp/custom")
val box3dSourcePattern = "${box3dPrivateSourceDir.normalizedPath()}/*.c"
val box3dTimerSource = File(box3dPrivateSourceDir, "timer.c")
val box3dWebTimerSource = File(box3dCustomSourceDir, "box3d_web_timer.c")
val box3dDumpSource = File(box3dCustomSourceDir, "box3d_dump.c")
val box3dWrapperSource = File(box3dCustomSourceDir, "jBox3D.cpp")
val box3dIdlSource = file("src/main/cpp/box3d.idl")

val verifyBox3dApiCoverage = tasks.register("verifyBox3dApiCoverage") {
    group = "verification"
    description = "Fails when a public Box3D C API has no WebIDL-reachable native wrapper."
    dependsOn(":box3d:download:box3d_download_source")
    inputs.dir(box3dIncludeDir)
    inputs.files(box3dWrapperSource, box3dDumpSource, box3dIdlSource)

    doLast {
        val apiPattern = Regex("""(?m)^\s*B3_API\s+[^;]*?\b(b3[A-Za-z0-9_]+)\s*\(""")
        val publicApis = File(box3dIncludeDir, "box3d")
            .walkTopDown()
            .filter { it.isFile && it.extension == "h" }
            .flatMap { header -> apiPattern.findAll(header.readText()).map { it.groupValues[1] } }
            .toSortedSet()
        check(publicApis.isNotEmpty()) {
            "No B3_API declarations were found under ${box3dIncludeDir.normalizedPath()}"
        }

        val wrapperImplementation = box3dWrapperSource.readText()
        val nativeImplementation = listOf(box3dWrapperSource, box3dDumpSource)
            .joinToString("\n") { source -> source.readText() }
        val missing = publicApis.filter { api ->
            !Regex("""\b${Regex.escape(api)}\s*\(""").containsMatchIn(nativeImplementation)
        }
        check(missing.isEmpty()) {
            "${missing.size} public Box3D C APIs have no native wrapper call site:\n" +
                missing.joinToString("\n")
        }

        val idlText = box3dIdlSource.readText()
        val idlInterfaces = Regex("""(?s)interface\s+([A-Za-z_]\w*)\s*(?:\:\s*[A-Za-z_]\w*)?\s*\{(.*?)\};""")
            .findAll(idlText)
            .associate { match -> match.groupValues[1] to match.groupValues[2] }
        val wrapperMethods = Regex(
            """(?m)^\s*(?:[A-Za-z_][\w:<>,*&]*\s+)*([A-Za-z_]\w*)::(~?[A-Za-z_]\w*)\s*\("""
        ).findAll(wrapperImplementation).toList()

        // These APIs live in private bridge helpers, but each helper is reached by
        // the listed WebIDL entry point. Everything else must be called directly
        // from the WebIDL-exposed wrapper method containing its first call site.
        val indirectIdlAdapters = mapOf(
            "b3CreateCompound" to ("B3Compound" to "CreateFromDef"),
            "b3CreateMesh" to ("B3Mesh" to "CreateFromDef"),
            "b3DefaultDebugDraw" to ("B3DebugDrawImplCustom" to "B3DebugDrawImplCustom"),
            "b3GetCompoundChild" to ("B3DebugDrawEm" to "DrawWorld"),
            "b3QueryCompound" to ("B3DebugDrawEm" to "DrawWorld"),
            "b3QueryHeightField" to ("B3DebugDrawEm" to "DrawWorld")
        )

        fun isIdlExposed(className: String, methodName: String): Boolean {
            val interfaceBody = idlInterfaces[className] ?: return false
            if(methodName.startsWith("~")) {
                // Disposal of any bound interface invokes its native destructor.
                return true
            }
            return Regex("""\b${Regex.escape(methodName)}\s*\(""").containsMatchIn(interfaceBody)
        }

        val notIdlReachable = publicApis.mapNotNull { api ->
            val explicitAdapter = indirectIdlAdapters[api]
            if(explicitAdapter != null) {
                return@mapNotNull if(isIdlExposed(explicitAdapter.first, explicitAdapter.second)) {
                    null
                }
                else {
                    "$api -> ${explicitAdapter.first}::${explicitAdapter.second}"
                }
            }

            val call = Regex("""\b${Regex.escape(api)}\s*\(""").find(wrapperImplementation)
                ?: return@mapNotNull "$api -> no call in jBox3D.cpp"
            val wrapperMethod = wrapperMethods.lastOrNull { method ->
                method.range.first <= call.range.first
            } ?: return@mapNotNull "$api -> no enclosing wrapper method"
            val className = wrapperMethod.groupValues[1]
            val methodName = wrapperMethod.groupValues[2]
            if(isIdlExposed(className, methodName)) {
                null
            }
            else {
                "$api -> $className::$methodName"
            }
        }
        check(notIdlReachable.isEmpty()) {
            "${notIdlReachable.size} public Box3D C APIs are not reachable through WebIDL:\n" +
                notIdlReachable.joinToString("\n")
        }

        logger.lifecycle(
            "Verified ${publicApis.size}/${publicApis.size} public Box3D C API entry points through WebIDL."
        )
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
}

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
val androidArmV7Target = AndroidTarget.Target.armeabi_v7a

jParser {
    libName.set("box3d")
    modulePrefix.set("")
    modulePath(file(".."))
    moduleBuildSuffix.set("builder")
    moduleBaseSuffix.set("base")
    moduleCoreSuffix.set("core")
    moduleJNISuffix.set("shared/jni")
    moduleFFMSuffix.set("desktop/ffm")
    moduleWebSuffix.set("web/wasm")
    moduleCSuffix.set("shared/c")
    packageName.set("com.github.xpenatan.box3d")
    cppSourcePath(box3dSourceRoot)
    sourceLanguage.set(SourceLanguage.C)
    cStandard.set("c17")

    native {
        dependsOn(":box3d:download:box3d_download_source")
        headerDir(box3dIncludeDir)
        headerDir(box3dPrivateSourceDir)
        headerDir(box3dCustomSourceDir)
        cppInclude(box3dSourcePattern)
        includeDefaultSources.set(false)
        includeCustomSources.set(false)

        jParserTargetNames.forEach { targetName ->
            target(targetName) {
                includeDefaultSources.set(false)
                includeCustomSources.set(false)
                cppInclude(box3dDumpSource)
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
                    androidTarget(androidArmV7Target) {
                        compileFlag("-DBOX3D_DISABLE_SIMD")
                    }
                }
                if(targetName == JParserTargets.WEB_WASM) {
                    cppExclude(box3dTimerSource)
                    cppInclude(box3dWebTimerSource)
                    // Emscripten 6.0.3's LLVM WebAssembly selector crashes on an
                    // SLP-generated min/max pattern in shape.c. Keep -O3 and explicit
                    // SIMD, but disable automatic SLP until the LLVM fix is released.
                    compileFlag("-fno-slp-vectorize")
                    compileFlag("-msimd128")
                    compileFlag("-msse2")
                    linkerFlag("-msimd128")
                    linkerFlag("-msse2")
                }
            }
        }
    }
}

tasks.matching { it.name == "jParser_build_web_wasm" }.configureEach {
    doLast {
        val generatedWebDir = project.file(
            "../web/wasm/src/main/java/gen/web/com/github/xpenatan/box3d"
        )
        val jsBigInt = "org.teavm.jso.core.JSBigInt"

        fun patchCallback(fileName: String, replacements: List<Pair<String, String>>) {
            val callbackFile = File(generatedWebDir, fileName)
            var generated = callbackFile.readText()
            replacements.forEach { (before, after) ->
                check(generated.contains(before)) {
                    "The generated web callback signature changed in $fileName: $before"
                }
                generated = generated.replace(before, after)
            }
            callbackFile.writeText(generated)
        }

        patchCallback(
            "B3CustomFilterEm.java",
            listOf(
                "public boolean Filter(int shapeIdA, int shapeIdB) {" to
                    "public boolean Filter($jsBigInt shapeIdA, $jsBigInt shapeIdB) {",
                "return internal_Filter(shapeIdA, shapeIdB);" to
                    "return internal_Filter(shapeIdA.longValue(), shapeIdB.longValue());",
                "boolean Filter(int shapeIdA, int shapeIdB);" to
                    "boolean Filter($jsBigInt shapeIdA, $jsBigInt shapeIdB);"
            )
        )
        patchCallback(
            "B3TreeQueryCallbackEm.java",
            listOf(
                "public boolean Query(int proxyId, int userData) {" to
                    "public boolean Query(int proxyId, $jsBigInt userData) {",
                "return internal_Query(proxyId, userData);" to
                    "return internal_Query(proxyId, userData.longValue());",
                "boolean Query(int proxyId, int userData);" to
                    "boolean Query(int proxyId, $jsBigInt userData);"
            )
        )
        patchCallback(
            "B3TreeClosestCallbackEm.java",
            listOf(
                "public float QueryClosest(float distanceSquaredMin, int proxyId, int userData) {" to
                    "public float QueryClosest(float distanceSquaredMin, int proxyId, $jsBigInt userData) {",
                "return internal_QueryClosest(distanceSquaredMin, proxyId, userData);" to
                    "return internal_QueryClosest(distanceSquaredMin, proxyId, userData.longValue());",
                "float QueryClosest(float distanceSquaredMin, int proxyId, int userData);" to
                    "float QueryClosest(float distanceSquaredMin, int proxyId, $jsBigInt userData);"
            )
        )
        patchCallback(
            "B3TreeRayCastCallbackEm.java",
            listOf(
                "public float RayCast(int input_addr, int proxyId, int userData) {" to
                    "public float RayCast(int input_addr, int proxyId, $jsBigInt userData) {",
                "return internal_RayCast(input_addr, proxyId, userData);" to
                    "return internal_RayCast(input_addr, proxyId, userData.longValue());",
                "float RayCast(int input_addr, int proxyId, int userData);" to
                    "float RayCast(int input_addr, int proxyId, $jsBigInt userData);"
            )
        )
        patchCallback(
            "B3TreeBoxCastCallbackEm.java",
            listOf(
                "public float BoxCast(int input_addr, int proxyId, int userData) {" to
                    "public float BoxCast(int input_addr, int proxyId, $jsBigInt userData) {",
                "return internal_BoxCast(input_addr, proxyId, userData);" to
                    "return internal_BoxCast(input_addr, proxyId, userData.longValue());",
                "float BoxCast(int input_addr, int proxyId, int userData);" to
                    "float BoxCast(int input_addr, int proxyId, $jsBigInt userData);"
            )
        )
        patchCallback(
            "B3PreSolveCallbackEm.java",
            listOf(
                "public boolean PreSolve(int shapeIdA, int shapeIdB, int point_addr, int normal_addr) {" to
                    "public boolean PreSolve($jsBigInt shapeIdA, $jsBigInt shapeIdB, int point_addr, int normal_addr) {",
                "return internal_PreSolve(shapeIdA, shapeIdB, point_addr, normal_addr);" to
                    "return internal_PreSolve(shapeIdA.longValue(), shapeIdB.longValue(), point_addr, normal_addr);",
                "boolean PreSolve(int shapeIdA, int shapeIdB, int point_addr, int normal_addr);" to
                    "boolean PreSolve($jsBigInt shapeIdA, $jsBigInt shapeIdB, int point_addr, int normal_addr);"
            )
        )
        patchCallback(
            "B3FrictionCallbackEm.java",
            listOf(
                "public float MixFriction(float frictionA, int userMaterialIdA, float frictionB, int userMaterialIdB) {" to
                    "public float MixFriction(float frictionA, $jsBigInt userMaterialIdA, float frictionB, $jsBigInt userMaterialIdB) {",
                "return internal_MixFriction(frictionA, userMaterialIdA, frictionB, userMaterialIdB);" to
                    "return internal_MixFriction(frictionA, userMaterialIdA.longValue(), frictionB, userMaterialIdB.longValue());",
                "float MixFriction(float frictionA, int userMaterialIdA, float frictionB, int userMaterialIdB);" to
                    "float MixFriction(float frictionA, $jsBigInt userMaterialIdA, float frictionB, $jsBigInt userMaterialIdB);"
            )
        )
        patchCallback(
            "B3RestitutionCallbackEm.java",
            listOf(
                "public float MixRestitution(float restitutionA, int userMaterialIdA, float restitutionB, int userMaterialIdB) {" to
                    "public float MixRestitution(float restitutionA, $jsBigInt userMaterialIdA, float restitutionB, $jsBigInt userMaterialIdB) {",
                "return internal_MixRestitution(restitutionA, userMaterialIdA, restitutionB, userMaterialIdB);" to
                    "return internal_MixRestitution(restitutionA, userMaterialIdA.longValue(), restitutionB, userMaterialIdB.longValue());",
                "float MixRestitution(float restitutionA, int userMaterialIdA, float restitutionB, int userMaterialIdB);" to
                    "float MixRestitution(float restitutionA, $jsBigInt userMaterialIdA, float restitutionB, $jsBigInt userMaterialIdB);"
            )
        )
        patchCallback(
            "B3AllocatorEm.java",
            listOf(
                "public long Allocate(int size, int alignment) {" to
                    "public int Allocate(int size, int alignment) {",
                "return internal_Allocate(size, alignment);" to
                    "return (int)internal_Allocate(size, alignment);",
                "public void Free(int address) {" to
                    "public void Free($jsBigInt address) {",
                "internal_Free(address);" to
                    "internal_Free(address.longValue());",
                "long Allocate(int size, int alignment);" to
                    "int Allocate(int size, int alignment);",
                "void Free(int address);" to
                    "void Free($jsBigInt address);"
            )
        )
    }
}

tasks.matching { it.name.startsWith("jParser_build_") }.configureEach {
    dependsOn(verifyBox3dApiCoverage)
}

tasks.named("check") {
    dependsOn(verifyBox3dApiCoverage)
}
