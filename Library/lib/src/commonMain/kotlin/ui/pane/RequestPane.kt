package ui.pane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import data.HttpRequestState
import data.SnifferDB
import ui.component.ErrorItem
import ui.component.ErrorItemDisplay
import ui.component.ExecutingItem
import ui.component.ExecutingItemDisplay
import ui.component.SpoofedItem
import ui.component.SpoofedItemDisplay
import ui.component.SuccessItem
import ui.component.SuccessItemDisplay

@Composable
fun RequestPane(
    modifier: Modifier = Modifier,
    id: String
) {
    val record by SnifferDB.httpRequests.collectAsStateWithLifecycle(emptyList())
    val transaction by rememberUpdatedState(record.firstOrNull { it.id == id } ?: return)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
        Text(transaction.toString())
    }
}