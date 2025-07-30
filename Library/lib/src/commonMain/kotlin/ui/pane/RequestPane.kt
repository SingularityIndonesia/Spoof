package ui.pane

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import data.HttpRequestState
import data.SnifferDB
import ui.component.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestPane(
    modifier: Modifier = Modifier,
    id: String
) {
    val record by SnifferDB.httpRequests.collectAsStateWithLifecycle(emptyList())
    val transaction by rememberUpdatedState(record.firstOrNull { it.id == id } as? HttpRequestState ?: return)

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.statusBarsPadding()
            ) {
                when (val tr = transaction) {
                    is HttpRequestState.Error -> {
                        ErrorItem(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp),
                            item = ErrorItemDisplay.from(tr),
                            onShare = {

                            },
                            onDelete = {

                            }
                        ) {

                        }
                    }

                    is HttpRequestState.Executing -> {
                        ExecutingItem(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp),
                            item = ExecutingItemDisplay.from(tr),
                            onShare = {

                            },
                            onDelete = {

                            }
                        ) {

                        }
                    }

                    is HttpRequestState.Spoofed -> {
                        SpoofedItem(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp),
                            item = SpoofedItemDisplay.from(tr),
                            onShare = {

                            },
                            onDelete = {

                            }
                        ) {

                        }
                    }

                    is HttpRequestState.Success -> {
                        SuccessItem(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp),
                            item = SuccessItemDisplay.from(tr),
                            onShare = {

                            },
                            onDelete = {

                            }
                        ) {

                        }
                    }
                }
            }
        },
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding())
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
            ) {
                stickyHeader {
                    Surface {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            text = "Request",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Header",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = transaction.requestHeader.orEmpty(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Content",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = transaction.requestBody.orEmpty(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                stickyHeader {
                    Surface {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            text = "Response",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Header",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = transaction.responseHeader.orEmpty(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Content",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = transaction.responseBody.orEmpty(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}