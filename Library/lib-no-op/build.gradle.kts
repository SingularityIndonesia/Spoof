import com.vanniktech.maven.publish.SonatypeHost
import plugin.convention.companion.compileAndroidLibrary
import plugin.convention.companion.compileIOSLibrary
import plugin.convention.companion.compileJvm

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    id("ConventionUtils")
}

val artifactId = "SpoofLibNoOp"
group = "io.github.stefanusayudha.spoof"
version = "1.0.0"

compileJvm()
compileAndroidLibrary(namespace = "$group.${"SpoofLib".lowercase()}")
compileIOSLibrary(namespace = "$group.${"SpoofLib".lowercase()}", baseName = artifactId)

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), artifactId, version.toString())

    pom {
        name = artifactId
        description = "A library."
        inceptionYear = "2024"
        url = "https://github.com/kotlin/multiplatform-library-template/"
        licenses {
            license {
                name = "XXX"
                url = "YYY"
                distribution = "ZZZ"
            }
        }
        developers {
            developer {
                id = "XXX"
                name = "YYY"
                url = "ZZZ"
            }
        }
        scm {
            url = "XXX"
            connection = "YYY"
            developerConnection = "ZZZ"
        }
    }
}
