package ui.pane

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import data.HttpRequestState
import data.SnifferDB
import ui.component.*

@Composable
fun RequestPane(
    modifier: Modifier = Modifier,
    id: String
) {
    val record by SnifferDB.httpRequests.collectAsStateWithLifecycle(emptyList())
    val transaction by rememberUpdatedState(record.firstOrNull { it.id == id } as? HttpRequestState ?: return)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp),
    ) {
        when (val tr = transaction) {
            is HttpRequestState.Error -> {
                ErrorItem(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    item = ErrorItemDisplay.from(tr)
                ) {

                }
            }

            is HttpRequestState.Executing -> {
                ExecutingItem(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    item = ExecutingItemDisplay.from(tr)
                ) {

                }
            }

            is HttpRequestState.Spoofed -> {
                SpoofedItem(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    item = SpoofedItemDisplay.from(tr)
                ) {

                }
            }

            is HttpRequestState.Success -> {
                SuccessItem(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    item = SuccessItemDisplay.from(tr)
                ) {

                }
            }
        }
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
        Spacer(modifier = Modifier.height(4.dp))
    }
}