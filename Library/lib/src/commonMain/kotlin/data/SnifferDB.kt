package data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object SnifferDB {
    private val _httpRequest = MutableStateFlow(emptyList<HttpRequestState>())
    val httpRequest = _httpRequest.asStateFlow()

    fun httpCall(httpRequestState: HttpRequestState) {
        _httpRequest.update {
            it.dropWhile { it.id == httpRequestState.id } + httpRequestState
        }
    }
}