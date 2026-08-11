plugins {
    id("java-library")
}

dependencies {
    implementation(project(":samples:shared"))
    compileOnly(project(":box3d:core"))
    api(libs.fdxBox3dExt)

    api(libs.bundles.fdxCore)

    testImplementation(project(":box3d:core"))
    testImplementation(libs.junit)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.javaFFM.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.javaFFM.get())
}
