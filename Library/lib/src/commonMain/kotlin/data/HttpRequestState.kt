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
import kotlinx.serialization.serializer

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
            "requestHeader" to value.requestHeader.toString(),
            "requestBody" to value.requestBody.toString(),
            "responseHeader" to value.responseHeader.toString(),
            "responseBody" to value.responseBody.toString(),
            "status" to value.status.toString(),
            "method" to value.method.toString()
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
                    responseHeader = data["responseHeader"],
                    requestBody = data["requestBody"],
                    method = data["method"],
                )
            }

            HttpRequestState.Success::class.simpleName -> {
                HttpRequestState.Success(
                    id = data["id"] ?: throw IllegalStateException("Data corrupted"),
                    timeSignMillis = data["timeSignMillis"]?.toLong() ?: throw IllegalStateException("Data corrupted"),
                    url = data["url"],
                    requestHeader = data["requestHeader"],
                    requestBody = data["requestBody"],
                    responseHeader = data["responseHeader"],
                    responseBody = data["responseBody"],
                    status = data["status"]?.toInt(),
                    method = data["method"],
                )
            }

            HttpRequestState.Error::class.simpleName -> {
                HttpRequestState.Error(
                    id = data["id"] ?: throw IllegalStateException("Data corrupted"),
                    timeSignMillis = data["timeSignMillis"]?.toLong() ?: throw IllegalStateException("Data corrupted"),
                    url = data["url"],
                    requestHeader = data["requestHeader"],
                    requestBody = data["requestBody"],
                    responseHeader = data["responseHeader"],
                    responseBody = data["responseBody"],
                    status = data["status"]?.toInt(),
                    method = data["method"],
                )
            }

            HttpRequestState.Spoofed::class.simpleName -> {
                HttpRequestState.Spoofed(
                    id = data["id"] ?: throw IllegalStateException("Data corrupted"),
                    timeSignMillis = data["timeSignMillis"]?.toLong() ?: throw IllegalStateException("Data corrupted"),
                    url = data["url"],
                    requestHeader = data["requestHeader"],
                    requestBody = data["requestBody"],
                    responseHeader = data["responseHeader"],
                    responseBody = data["responseBody"],
                    status = data["status"]?.toInt(),
                    method = data["method"],
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
    open val requestHeader: String? = null,
    open val requestBody: String? = null,
    open val responseHeader: String? = null,
    open val responseBody: String? = null,
    open val status: Int? = null,
    open val method: String? = null
) {

    data class Executing(
        override val id: String,
        override val timeSignMillis: Long = getTimeMillis(),
        override val url: String?,
        override val responseHeader: String?,
        override val requestBody: String?,
        override val method: String?,
    ) : HttpRequestState(id, timeSignMillis, url, responseHeader, requestBody)

    data class Success(
        override val id: String,
        override val timeSignMillis: Long = getTimeMillis(),
        override val url: String?,
        override val requestHeader: String? = null,
        override val requestBody: String?,
        override val responseHeader: String?,
        override val responseBody: String?,
        override val status: Int?,
        override val method: String?,
    ) : HttpRequestState(id, timeSignMillis, url, responseHeader, requestBody, responseBody)

    data class Error(
        override val id: String,
        override val timeSignMillis: Long = getTimeMillis(),
        override val url: String?,
        override val requestHeader: String? = null,
        override val requestBody: String?,
        override val responseHeader: String?,
        override val responseBody: String?,
        override val status: Int?,
        override val method: String?,
    ) : HttpRequestState(id, timeSignMillis, url, responseHeader, requestBody, responseBody)

    data class Spoofed(
        override val id: String,
        override val timeSignMillis: Long = getTimeMillis(),
        override val url: String?,
        override val requestHeader: String? = null,
        override val requestBody: String?,
        override val responseHeader: String?,
        override val responseBody: String?,
        override val status: Int?,
        override val method: String?,
    ) : HttpRequestState(id, timeSignMillis, url, responseHeader, requestBody, responseBody)

    companion object {
        private val json = Json {
            prettyPrint = true
        }

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
                responseHeader = pipeline.context.headers.build().toString(),
                requestBody = pipeline.context.body.toString(),
                method = pipeline.context.method.value,
            )
        }

        fun finishWith(
            pipeline: PipelineContext<HttpResponseContainer, HttpClientCall>,
            data: HttpResponseContainer
        ): HttpRequestState {
            val requestId = pipeline.context.attributes[AttributeKey<Long>("RequestId")]
            val requestHeader: String = run {
                pipeline.context.request.headers.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .split(",")
                    .joinToString("\n") {
                        it.replaceFirst("^\\s+".toRegex(), "")
                    }
            }
            val requestBody: String = run {
                pipeline.context.request.content.toString()
            }
            val responseHeader: String = run {
                pipeline.context.response.headers.toString()
                    .replace("[", "")
                    .replace("]", "")
                    .split(",")
                    .joinToString("\n") {
                        it.replaceFirst("^\\s+".toRegex(), "")
                    }
            }
            val response: String = run {
                val type = data.expectedType.kotlinType ?: return@run data.response.toString()
                val serializer = Json.serializersModule.serializer(type)
                json.encodeToString(serializer, data.response)
            }
            return Success(
                id = requestId.toString(),
                url = pipeline.context.request.url.toString(),
                requestHeader = requestHeader,
                requestBody = requestBody,
                responseHeader = responseHeader,
                responseBody = response,
                status = pipeline.context.response.status.value,
                method = pipeline.context.request.method.value,
            )
        }
    }
}