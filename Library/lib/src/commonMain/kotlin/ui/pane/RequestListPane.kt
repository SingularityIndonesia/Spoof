package ui.pane

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import data.HttpRequestState
import data.SnifferDB
import ui.component.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestListPane(
    onClick: (id: String) -> Unit,
) {
    val requests by SnifferDB.httpRequests.collectAsStateWithLifecycle(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Transaction List")
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            LazyColumn {
                items(requests) {
                    when (it) {
                        is HttpRequestState.Error -> {
                            ErrorItem(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                                item = ErrorItemDisplay.from(it),
                                onClick = { onClick.invoke(it.id) },
                            )
                        }

                        is HttpRequestState.Executing -> {
                            ExecutingItem(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                                item = ExecutingItemDisplay.from(it),
                                onClick = { onClick.invoke(it.id) },
                            )
                        }

                        is HttpRequestState.Spoofed -> {
                            SpoofedItem(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                                item = SpoofedItemDisplay.from(it),
                                onClick = { onClick.invoke(it.id) },
                            )
                        }

                        is HttpRequestState.Success -> {
                            SuccessItem(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                                item = SuccessItemDisplay.from(it),
                                onClick = { onClick.invoke(it.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}