package org.example.project

import androidx.compose.runtime.staticCompositionLocalOf
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import util.ktor.SnifferPluginKtor

val LocalHttpClient = staticCompositionLocalOf { defaultHttpClient() }

fun defaultHttpClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }

        install(SnifferPluginKtor)
    }
}
