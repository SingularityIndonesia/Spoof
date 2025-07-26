pluginManagement {
    repositories {
        includeBuild("Plugin")
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

rootProject.name = "Spoof"
include(":core")
include(":lib")
include(":lib-no-op")
