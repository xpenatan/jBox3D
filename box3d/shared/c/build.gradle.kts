import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    id("java-library")
}

val moduleName = "shared-c"
val generatedTeaVMCResourcesDir = layout.buildDirectory.dir("generated/jparser/resources/main")
val box3dSourceRoot = project(":box3d:download").layout.buildDirectory.dir("box3d-source")

base {
    archivesName.set(moduleName)
}

dependencies {
    api(libs.bundles.jParserSharedC)
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src/main/java"))
        java.include("gen/c/**/*.java")
        resources.setSrcDirs(listOf("src/main/resources", generatedTeaVMCResourcesDir))
    }
}

tasks.named("clean") {
    doFirst {
        project.delete(files("$projectDir/src/main/java"))
    }
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(":box3d:download:box3d_download_source")
    from(box3dSourceRoot) {
        include("samples/tiny_obj_loader.h")
        include("samples/earcut.h")
        into("external_cpp/jparser/box3d/source")
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaWeb.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaWeb.get())
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = moduleName
            from(components["java"])
        }
    }
}
