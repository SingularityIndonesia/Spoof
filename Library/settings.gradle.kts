pluginManagement {
    repositories {
        includeBuild("../Plugin")
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Library"
include(":core")
include(":lib")
include(":lib-no-op")
