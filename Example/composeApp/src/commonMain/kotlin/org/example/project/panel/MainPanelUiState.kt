package org.example.project.panel

import org.example.project.model.Post

data class MainPanelUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)