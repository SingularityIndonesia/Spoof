package ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import data.HttpRequestState
import data.SnifferDB
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ui.component.colorSuccess
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

expect fun openDebuggerWindow()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebuggerWindow() {
    val requests by SnifferDB.httpRequests.collectAsStateWithLifecycle(emptyList())

    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Sniffer")
                    }
                )
            }
        ) {
            Surface {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    items(requests) {
                        when (it) {
                            is HttpRequestState.Error -> {
                                ErrorItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    item = ErrorItemDisplay.from(it)
                                )
                            }

                            is HttpRequestState.Executing -> {
                                ExecutingItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    item = ExecutingItemDisplay.from(it)
                                )
                            }

                            is HttpRequestState.Spoofed -> {
                                SpoofedItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    item = SpoofedItemDisplay.from(it)
                                )
                            }

                            is HttpRequestState.Success -> {
                                SuccessItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    item = SuccessItemDisplay.from(it)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ErrorItemDisplay(
    val executionTime: String,
    val status: String,
    val url: String,
) {
    companion object {
        fun from(data: HttpRequestState.Error): ErrorItemDisplay {
            return ErrorItemDisplay(
                status = data.status?.toString().orEmpty(),
                url = data.url.orEmpty(),
                executionTime = data.timeSignMillis.toDate()
            )
        }
    }
}

@Composable
fun ErrorItem(
    modifier: Modifier = Modifier,
    item: ErrorItemDisplay
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorSuccess)) {
        StatusItem(modifier, item.executionTime, item.status, item.url)
    }
}

data class ExecutingItemDisplay(
    val executionTime: String,
    val status: String,
    val url: String,
) {
    companion object {
        fun from(data: HttpRequestState.Executing): ExecutingItemDisplay {
            return ExecutingItemDisplay(
                status = "Executing..",
                url = data.url.orEmpty(),
                executionTime = data.timeSignMillis.toDate()
            )
        }
    }
}

@Composable
fun ExecutingItem(
    modifier: Modifier = Modifier,
    item: ExecutingItemDisplay
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorSuccess)) {
        StatusItem(modifier, item.executionTime, item.status, item.url)
    }
}

data class SpoofedItemDisplay(
    val executionTime: String,
    val status: String,
    val url: String,
) {
    companion object {
        fun from(data: HttpRequestState.Spoofed): SpoofedItemDisplay {
            return SpoofedItemDisplay(
                status = "Spoofed",
                url = data.url.orEmpty(),
                executionTime = data.timeSignMillis.toDate()
            )
        }
    }
}

@Composable
fun SpoofedItem(
    modifier: Modifier = Modifier,
    item: SpoofedItemDisplay
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorSuccess)) {
        StatusItem(modifier, item.executionTime, item.status, item.url)
    }
}

data class SuccessItemDisplay(
    val executionTime: String,
    val status: String,
    val url: String,
) {
    companion object {
        fun from(data: HttpRequestState.Success): SuccessItemDisplay {
            return SuccessItemDisplay(
                status = data.status?.toString().orEmpty(),
                url = data.url.orEmpty(),
                executionTime = data.timeSignMillis.toDate()
            )
        }
    }
}

@Composable
fun SuccessItem(
    modifier: Modifier = Modifier,
    item: SuccessItemDisplay
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorSuccess)) {
        StatusItem(modifier, item.executionTime, item.status, item.url)
    }
}

@Composable
fun StatusItem(
    modifier: Modifier = Modifier,
    executionTime: String,
    status: String,
    url: String,
) {
    Column(
        modifier = modifier
            .padding(vertical = 16.dp),
    ) {
        Text(
            text = url,
            overflow = TextOverflow.StartEllipsis
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = executionTime, color = Color.White)
            Text("status: $status")
        }
    }
}

@OptIn(ExperimentalTime::class)
fun Long.toDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dateTime: LocalDateTime = instant.toLocalDateTime(timeZone = timeZone)

    val year = dateTime.year
    val month = dateTime.monthNumber.toString().padStart(2, '0')
    val day = dateTime.dayOfMonth.toString().padStart(2, '0')
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')
    val second = dateTime.second.toString().padStart(2, '0')

    return "$month $day, $hour:$minute:$second"
}