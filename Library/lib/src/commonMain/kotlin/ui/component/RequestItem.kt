package ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import data.HttpRequestState
import io.github.stefanusayudha.spoof.lib.generated.resources.Res
import io.github.stefanusayudha.spoof.lib.generated.resources.ic_delete_24
import io.github.stefanusayudha.spoof.lib.generated.resources.ic_share_24
import org.jetbrains.compose.resources.painterResource
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
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    onDelete: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorError)) {
        StatusItem(
            modifier,
            onClick = onClick,
            onDelete = onDelete,
            onShare = onShare,
            contentPadding = contentPadding,
            executionTime = item.executionTime,
            status = item.status,
            url = item.url,
            method = item.method
        )
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
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    onDelete: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    CompositionLocalProvider(LocalContentColor provides Color.White) {
        StatusItem(
            modifier,
            onClick = onClick,
            onDelete = onDelete,
            onShare = onShare,
            contentPadding = contentPadding,
            executionTime = item.executionTime,
            status = item.status,
            url = item.url,
            method = item.method
        )
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
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    onDelete: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorSpoofed)) {
        StatusItem(
            modifier,
            onClick = onClick,
            onDelete = onDelete,
            onShare = onShare,
            contentPadding = contentPadding,
            executionTime = item.executionTime,
            status = item.status,
            url = item.url,
            method = item.method
        )
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
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    onDelete: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    CompositionLocalProvider(LocalContentColor provides Color(colorSuccess)) {
        StatusItem(
            modifier,
            onClick = onClick,
            onDelete = onDelete,
            onShare = onShare,
            contentPadding = contentPadding,
            executionTime = item.executionTime,
            status = item.status,
            url = item.url,
            method = item.method
        )
    }
}

@Composable
fun StatusItem(
    modifier: Modifier = Modifier,
    executionTime: String,
    status: String,
    url: String,
    method: String,
    onClick: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onShare: (() -> Unit)? = null,
    contentPadding: PaddingValues,
) {
    Column(
        modifier = modifier
            .then(
                other = if (onClick != null)
                    Modifier.clickable {
                        onClick.invoke()
                    }
                else Modifier
            ),
    ) {
        Spacer(modifier.height(contentPadding.calculateTopPadding()))
        Row(
            modifier = Modifier.padding(
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = contentPadding.calculateEndPadding(LayoutDirection.Rtl)
            )
        ) {
            Text(text = method.uppercase(), color = Color.White)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = url,
                overflow = TextOverflow.StartEllipsis
            )
        }
        Row(
            modifier = Modifier.padding(
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = contentPadding.calculateEndPadding(LayoutDirection.Rtl)
            )
        ) {
            Text(text = "Status:", color = Color.White)
            Spacer(modifier = Modifier.width(4.dp))
            Text(status)
        }
        Text(
            modifier = Modifier.padding(
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = contentPadding.calculateEndPadding(LayoutDirection.Rtl)
            ),
            text = executionTime,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
        if (listOf(onShare, onDelete).any { it != null })
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                if (onDelete != null) {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                        ),
                        onClick = onDelete
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(Res.drawable.ic_delete_24),
                            contentDescription = null
                        )
                    }
                }
                if (onShare != null)
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        onClick = onShare
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(Res.drawable.ic_share_24),
                            contentDescription = null
                        )
                    }
            }
        Spacer(modifier.height(contentPadding.calculateBottomPadding()))
    }
}