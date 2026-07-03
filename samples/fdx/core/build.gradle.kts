plugins {
    id("java-library")
}

dependencies {
    implementation(project(":samples:shared"))
    compileOnly(project(":box3d:core"))
    api(project(":extensions:fdx"))

    api("${LibExt.fdxGroup}:application:${LibExt.fdxVersion}")
    api("${LibExt.fdxGroup}:camera:${LibExt.fdxVersion}")
    api("${LibExt.fdxGroup}:display:${LibExt.fdxVersion}")
    api("${LibExt.fdxGroup}:graphics:${LibExt.fdxVersion}")
    api("${LibExt.fdxGroup}:ui_kit:${LibExt.fdxVersion}")
}

java {
    sourceCompatibility = JavaVersion.toVersion(LibExt.javaFFMTarget)
    targetCompatibility = JavaVersion.toVersion(LibExt.javaFFMTarget)
}
