package data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update

expect object SnifferDB {
    val httpRequestRecord: Flow<List<HttpRequestState>>
    val httpRequest: Flow<List<HttpRequestState>>

    suspend fun httpCall(httpRequestState: HttpRequestState)
}