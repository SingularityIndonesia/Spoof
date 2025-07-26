package org.example.project.panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.model.Post

class MainPanelViewModel(
    private val client: HttpClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainPanelUiState())
    val uiState: StateFlow<MainPanelUiState> = _uiState.asStateFlow()

    private var job: Job? = null
    fun fetchPosts() {
        job?.cancel()

        job = viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            runCatching {
                client.get("https://jsonplaceholder.typicode.com/posts")
                    .body<List<Post>>()
            }.onSuccess { posts ->
                ensureActive()
                _uiState.update {
                    it.copy(
                        posts = posts,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }.onFailure { e ->
                ensureActive()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}