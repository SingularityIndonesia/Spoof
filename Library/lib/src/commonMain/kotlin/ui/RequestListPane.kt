package ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import data.HttpRequestState
import data.SnifferDB
import ui.component.*

@Composable
fun RequestListPane(
    modifier: Modifier,
    onClick: (id: String) -> Unit,
) {
    val requests by SnifferDB.httpRequests.collectAsStateWithLifecycle(emptyList())

    LazyColumn(
        modifier = modifier,
    ) {
        items(requests) {
            when (it) {
                is HttpRequestState.Error -> {
                    ErrorItem(
                        modifier = Modifier.fillMaxWidth(),
                        item = ErrorItemDisplay.from(it),
                        onClick = { onClick.invoke(it.id) },
                    )
                }

                is HttpRequestState.Executing -> {
                    ExecutingItem(
                        modifier = Modifier.fillMaxWidth(),
                        item = ExecutingItemDisplay.from(it),
                        onClick = { onClick.invoke(it.id) },
                    )
                }

                is HttpRequestState.Spoofed -> {
                    SpoofedItem(
                        modifier = Modifier.fillMaxWidth(),
                        item = SpoofedItemDisplay.from(it),
                        onClick = { onClick.invoke(it.id) },
                    )
                }

                is HttpRequestState.Success -> {
                    SuccessItem(
                        modifier = Modifier.fillMaxWidth(),
                        item = SuccessItemDisplay.from(it),
                        onClick = { onClick.invoke(it.id) },
                    )
                }
            }
        }
    }
}