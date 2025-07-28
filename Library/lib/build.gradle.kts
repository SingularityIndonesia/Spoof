import com.vanniktech.maven.publish.SonatypeHost
import plugin.convention.companion.compileAndroidLibrary
import plugin.convention.companion.compileIOSLibrary
import plugin.convention.companion.compileJvm
import plugin.convention.companion.dependency
import plugin.convention.companion.withKotlinMultiplatformExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinxSerialization)
    id("ConventionUtils")
}

val artifactId = "SpoofLib"
group = "io.github.stefanusayudha.spoof"
version = "1.0.0"

compileJvm()
compileAndroidLibrary(namespace = "$group.${artifactId.lowercase()}")
compileIOSLibrary(namespace = "$group.${artifactId.lowercase()}", baseName = artifactId)

dependency {
    common {
        withKotlinMultiplatformExtension {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }

        implementation(libs.androidx.lifecycle.viewmodel)
        implementation(libs.androidx.lifecycle.runtimeCompose)
        implementation(libs.androidx.lifecycle.viewmodel.compose)
        
        // Navigation Compose
        implementation(libs.androidx.navigation.compose)

        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)

        // DataStore library
        implementation(libs.datastore)
        implementation(libs.datastore.preferences)

        // Date Time
        implementation(libs.kotlinx.datetime)

        implementation(project(":core"))
    }

    android {
        implementation(libs.androidx.activity.compose)
        implementation(libs.ktor.client.okhttp)
    }

    ios {
        implementation(libs.ktor.client.darwin)
    }

    desktop {
        implementation(libs.ktor.client.cio)
    }
}

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
