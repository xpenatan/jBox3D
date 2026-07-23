plugins {
    id("java")
    id("java-library")
}

dependencies {
    implementation(libs.bundles.jParserBase)
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src/main/java"))
    }
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaMain.get())
}
