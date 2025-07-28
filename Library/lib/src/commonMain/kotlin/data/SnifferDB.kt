package data

import kotlinx.coroutines.flow.Flow

expect object SnifferDB {
    val httpRequestRecord: Flow<List<HttpRequestState>>
    val httpRequests: Flow<List<HttpRequestState>>

    suspend fun httpCall(httpRequestState: HttpRequestState)
}