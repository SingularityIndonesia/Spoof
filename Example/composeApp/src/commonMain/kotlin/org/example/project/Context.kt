package org.example.project

import ui.Sniffer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import util.ktor.SnifferPlugin

val LocalHttpClient = staticCompositionLocalOf<HttpClient> { error("Not provider") }

@Composable
fun ContextScope(
    content: @Composable () -> Unit
) {
    val httpClient = remember { defaultHttpClient() }

    CompositionLocalProvider(LocalHttpClient provides httpClient) {
        Sniffer {
            content.invoke()
        }
    }
}

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

        install(SnifferPlugin)
    }
}
