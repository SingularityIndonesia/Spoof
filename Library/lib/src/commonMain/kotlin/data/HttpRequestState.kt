package data

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*
import io.ktor.util.date.*
import io.ktor.util.pipeline.*

sealed class HttpRequestState {
    abstract val id: String
    val timeSignMillis: Long = getTimeMillis()
    open val url: String? = null
    open val header: String? = null
    open val request: String? = null
    open val response: String? = null

    data class Executing(
        override val id: String,
        override val url: String?,
        override val header: String?,
        override val request: String?,
    ) : HttpRequestState()

    data class Success(
        override val id: String,
        override val url: String?,
        override val header: String?,
        override val request: String?,
        override val response: String?
    ) : HttpRequestState()

    data class Error(
        override val id: String,
        override val url: String?,
        override val header: String?,
        override val request: String?,
        override val response: String?
    ) : HttpRequestState()


    data class Spoofed(
        override val id: String,
        override val url: String?,
        override val header: String?,
        override val request: String?,
        override val response: String?
    ) : HttpRequestState()

    companion object {
        fun beginWith(pipeline: PipelineContext<Any, HttpRequestBuilder>, data: Any): HttpRequestState {
            // inject RequestId to attribute
            val requestId = with(pipeline.context.attributes) {
                getOrNull(AttributeKey<Long>("RequestId"))
                    ?: run {
                        val requestID = getTimeMillis()
                        put(AttributeKey("RequestId"), requestID)
                        requestID
                    }
            }

            return Executing(
                id = requestId.toString(),
                url = pipeline.context.url.buildString(),
                header = pipeline.context.headers.build().toString(),
                request = pipeline.context.body.toString(),
            )
        }

        fun finishWith(
            pipeline: PipelineContext<HttpResponseContainer, HttpClientCall>,
            data: HttpResponseContainer
        ): HttpRequestState {
            val requestId = pipeline.context.attributes[AttributeKey<Long>("RequestId")]

            return Success(
                id = requestId.toString(),
                url = pipeline.context.request.url.toString(),
                header = pipeline.context.request.headers.toString(),
                request = pipeline.context.request.content.toString(),
                response = data.response.toString(),
            )
        }
    }
}