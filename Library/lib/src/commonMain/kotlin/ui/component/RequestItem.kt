package ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import data.HttpRequestState
import util.toDate

data class ErrorItemDisplay(
    val executionTime: String,
    val status: String,
    val url: String,
    val method: String,
) {
    companion object {
        fun from(data: HttpRequestState.Error): ErrorItemDisplay {
            return ErrorItemDisplay(
                status = data.status?.toString().orEmpty(),
                url = data.url.orEmpty(),
                executionTime = data.timeSignMillis.toDate(),
                method = data.method.toString()
            )
        }
    }
}

@Composable
fun ErrorItem(
    modifier: Modifier = Modifier,
    item: ErrorItemDisplay,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorSuccess)) {
        StatusItem(modifier, onClick, item.executionTime, item.status, item.url, item.method)
    }
}

data class ExecutingItemDisplay(
    val executionTime: String,
    val status: String,
    val url: String,
    val method: String,
) {
    companion object {
        fun from(data: HttpRequestState.Executing): ExecutingItemDisplay {
            return ExecutingItemDisplay(
                status = "Executing..",
                url = data.url.orEmpty(),
                executionTime = data.timeSignMillis.toDate(),
                method = data.method.toString()
            )
        }
    }
}

@Composable
fun ExecutingItem(
    modifier: Modifier = Modifier,
    item: ExecutingItemDisplay,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorSuccess)) {
        StatusItem(modifier, onClick, item.executionTime, item.status, item.url, item.method)
    }
}

data class SpoofedItemDisplay(
    val executionTime: String,
    val status: String,
    val url: String,
    val method: String,
) {
    companion object {
        fun from(data: HttpRequestState.Spoofed): SpoofedItemDisplay {
            return SpoofedItemDisplay(
                status = "Spoofed",
                url = data.url.orEmpty(),
                executionTime = data.timeSignMillis.toDate(),
                method = data.method.toString()
            )
        }
    }
}

@Composable
fun SpoofedItem(
    modifier: Modifier = Modifier,
    item: SpoofedItemDisplay,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorSuccess)) {
        StatusItem(modifier, onClick, item.executionTime, item.status, item.url, item.method)
    }
}

data class SuccessItemDisplay(
    val executionTime: String,
    val status: String,
    val url: String,
    val method: String,
) {
    companion object {
        fun from(data: HttpRequestState.Success): SuccessItemDisplay {
            return SuccessItemDisplay(
                status = data.status?.toString().orEmpty(),
                url = data.url.orEmpty(),
                executionTime = data.timeSignMillis.toDate(),
                method = data.method.toString()
            )
        }
    }
}

@Composable
fun SuccessItem(
    modifier: Modifier = Modifier,
    item: SuccessItemDisplay,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorSuccess)) {
        StatusItem(modifier, onClick, item.executionTime, item.status, item.url, item.method)
    }
}

@Composable
fun StatusItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    executionTime: String,
    status: String,
    url: String,
    method: String,
) {
    Column(
        modifier = modifier
            .clickable {
                onClick.invoke()
            }
            .padding(vertical = 16.dp, horizontal = 16.dp),
    ) {
        Row {
            Text(text = method.uppercase(), color = Color.White)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = url,
                overflow = TextOverflow.StartEllipsis
            )
        }
        Row {
            Text(text = executionTime, color = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Status:", color = Color.White)
            Spacer(modifier = Modifier.width(4.dp))
            Text(status)
        }
    }
}