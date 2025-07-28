package data

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*
import io.ktor.util.date.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json

object HttpRequestStateSerializer : KSerializer<HttpRequestState> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("InstantAsString", PrimitiveKind.STRING)

    @Throws(IllegalStateException::class)
    override fun serialize(encoder: kotlinx.serialization.encoding.Encoder, value: HttpRequestState) {
        val typeToken = value::class.simpleName ?: throw IllegalStateException("Class didn't have name")
        val data = mapOf(
            "typeToken" to typeToken,
            "id" to value.id.toString(),
            "timeSignMillis" to value.timeSignMillis.toString(),
            "url" to value.url.toString(),
            "header" to value.header.toString(),
            "request" to value.request.toString(),
            "response" to value.response.toString(),
            "status" to value.status.toString()
        )
        encoder.encodeString(Json.encodeToString(data))
    }

    @Throws(IllegalStateException::class)
    override fun deserialize(decoder: Decoder): HttpRequestState {
        val data = Json.decodeFromString<Map<String, String>>(decoder.decodeString())
        return when (data["typeToken"]) {
            HttpRequestState.Executing::class.simpleName -> {
                HttpRequestState.Executing(
                    id = data["id"] ?: throw IllegalStateException("Data corrupted"),
                    timeSignMillis = data["timeSignMillis"]?.toLong() ?: throw IllegalStateException("Data corrupted"),
                    url = data["url"],
                    header = data["header"],
                    request = data["request"],
                )
            }

            HttpRequestState.Success::class.simpleName -> {
                HttpRequestState.Success(
                    id = data["id"] ?: throw IllegalStateException("Data corrupted"),
                    timeSignMillis = data["timeSignMillis"]?.toLong() ?: throw IllegalStateException("Data corrupted"),
                    url = data["url"],
                    header = data["header"],
                    request = data["request"],
                    response = data["response"],
                    status = data["status"]?.toInt(),
                )
            }

            HttpRequestState.Error::class.simpleName -> {
                HttpRequestState.Error(
                    id = data["id"] ?: throw IllegalStateException("Data corrupted"),
                    timeSignMillis = data["timeSignMillis"]?.toLong() ?: throw IllegalStateException("Data corrupted"),
                    url = data["url"],
                    header = data["header"],
                    request = data["request"],
                    response = data["response"],
                    status = data["status"]?.toInt(),
                )
            }

            HttpRequestState.Spoofed::class.simpleName -> {
                HttpRequestState.Spoofed(
                    id = data["id"] ?: throw IllegalStateException("Data corrupted"),
                    timeSignMillis = data["timeSignMillis"]?.toLong() ?: throw IllegalStateException("Data corrupted"),
                    url = data["url"],
                    header = data["header"],
                    request = data["request"],
                    response = data["response"],
                    status = data["status"]?.toInt(),
                )
            }

            else -> throw IllegalStateException("No type token found")
        }
    }
}

@Serializable(HttpRequestStateSerializer::class)
sealed class HttpRequestState(
    open val id: String,
    open val timeSignMillis: Long,
    open val url: String? = null,
    open val header: String? = null,
    open val request: String? = null,
    open val response: String? = null,
    open val status: Int? = null
) {

    data class Executing(
        override val id: String,
        override val timeSignMillis: Long = getTimeMillis(),
        override val url: String?,
        override val header: String?,
        override val request: String?,
    ) : HttpRequestState(id, timeSignMillis, url, header, request)

    data class Success(
        override val id: String,
        override val timeSignMillis: Long = getTimeMillis(),
        override val url: String?,
        override val header: String?,
        override val request: String?,
        override val response: String?,
        override val status: Int?
    ) : HttpRequestState(id, timeSignMillis, url, header, request, response)

    data class Error(
        override val id: String,
        override val timeSignMillis: Long = getTimeMillis(),
        override val url: String?,
        override val header: String?,
        override val request: String?,
        override val response: String?,
        override val status: Int?
    ) : HttpRequestState(id, timeSignMillis, url, header, request, response)

    data class Spoofed(
        override val id: String,
        override val timeSignMillis: Long = getTimeMillis(),
        override val url: String?,
        override val header: String?,
        override val request: String?,
        override val response: String?,
        override val status: Int?
    ) : HttpRequestState(id, timeSignMillis, url, header, request, response)

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
                status = pipeline.context.response.status.value
            )
        }
    }
}