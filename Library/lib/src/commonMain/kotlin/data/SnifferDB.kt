package data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object SnifferDB {
    private val _httpRequestRecord = MutableStateFlow(emptyList<HttpRequestState>())
    val httpRequestRecord = _httpRequestRecord.asStateFlow()
    val httpRequest = _httpRequestRecord.map {
        val reqGroups = it.groupBy { e -> e.id }
        val lastRequest = reqGroups
            .map { e ->
                e.value.maxBy {
                    it.timeSignMillis
                }
            }

        lastRequest
    }

    fun httpCall(httpRequestState: HttpRequestState) {
        _httpRequestRecord.update { requests ->
            requests + httpRequestState
        }
    }
}