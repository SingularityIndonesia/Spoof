package ui.pane

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import data.HttpRequestState
import data.SnifferDB
import io.github.stefanusayudha.spoof.lib.generated.resources.Res
import io.github.stefanusayudha.spoof.lib.generated.resources.ic_delete_24
import io.github.stefanusayudha.spoof.lib.generated.resources.ic_share_24
import org.jetbrains.compose.resources.painterResource
import ui.component.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestPane(
    modifier: Modifier = Modifier,
    id: String
) {
    val record by SnifferDB.httpRequests.collectAsStateWithLifecycle(emptyList())
    val transaction by rememberUpdatedState(record.firstOrNull { it.id == id } as? HttpRequestState ?: return)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.statusBarsPadding()
            ) {
                when (val tr = transaction) {
                    is HttpRequestState.Error -> {
                        ErrorItem(
                            modifier = Modifier.fillMaxWidth(),
                            item = ErrorItemDisplay.from(tr)
                        ) {

                        }
                    }

                    is HttpRequestState.Executing -> {
                        ExecutingItem(
                            modifier = Modifier.fillMaxWidth(),
                            item = ExecutingItemDisplay.from(tr)
                        ) {

                        }
                    }

                    is HttpRequestState.Spoofed -> {
                        SpoofedItem(
                            modifier = Modifier.fillMaxWidth(),
                            item = SpoofedItemDisplay.from(tr)
                        ) {

                        }
                    }

                    is HttpRequestState.Success -> {
                        SuccessItem(
                            modifier = Modifier.fillMaxWidth(),
                            item = SuccessItemDisplay.from(tr)
                        ) {

                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                    onClick = {

                    }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(Res.drawable.ic_delete_24),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {

                    }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(Res.drawable.ic_share_24),
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Request",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
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
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Response",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
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