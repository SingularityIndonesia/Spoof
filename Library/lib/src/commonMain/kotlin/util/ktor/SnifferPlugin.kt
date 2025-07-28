package util.ktor

import data.HttpRequestState
import data.SnifferDB
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*

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
                plugin.httpCall(
                    HttpRequestState.beginWith(this, it)
                )
            }

            // Intercept responses
            scope.responsePipeline.intercept(HttpResponsePipeline.Phases.After) {
                plugin.httpCall(
                    HttpRequestState.finishWith(this, it)
                )
            }
        }
    }

    private val db = SnifferDB

    fun httpCall(state: HttpRequestState) {
        db.httpCall(state)
    }
}