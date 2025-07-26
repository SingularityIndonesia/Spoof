package ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.client.statement.readBytes
import io.ktor.util.AttributeKey
import io.ktor.util.KtorDsl

class SnifferPlugin private constructor() {

    @KtorDsl
    companion object Plugin : HttpClientPlugin<Unit, SnifferPlugin> {
        override val key: AttributeKey<SnifferPlugin> = AttributeKey("SnifferPlugin")

        override fun prepare(block: Unit.() -> Unit): SnifferPlugin {

            return SnifferPlugin()
        }

        override fun install(plugin: SnifferPlugin, scope: HttpClient) {
            // Intercept requests
            scope.requestPipeline.intercept(HttpRequestPipeline.Phases.Before) {
                // plugin.logRequest(context)
                // println("${context.toString()}")
            }

            // Intercept responses
            scope.responsePipeline.intercept(HttpResponsePipeline.Phases.After) {
                println("${context.request.content}")
                println("${context.response.readBytes().decodeToString()}")
            }
        }
    }
}