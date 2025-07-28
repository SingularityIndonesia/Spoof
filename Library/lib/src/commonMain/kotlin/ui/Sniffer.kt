package ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntRect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import data.HttpRequestState
import data.SnifferDB
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.component.*
import kotlin.math.absoluteValue

@Composable
fun SnifferIndicator() {
    val density = LocalDensity.current
    val rootSize = remember { mutableStateOf(IntSize.Zero) }
    val buttonSize = remember { mutableStateOf(IntSize.Zero) }
    val widgetPosition = remember { mutableStateOf(IntOffset((-8 * density.density).toInt(), 0)) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {
                rootSize.value = it
            }
    ) {
        SnifferIndicator(
            modifier = Modifier
                .systemBarsPadding()
                .align(Alignment.CenterEnd)
                .offset { widgetPosition.value }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            val rootRect = rootSize.value.toIntRect()

                            if (widgetPosition.value.x.absoluteValue < rootRect.center.x - buttonSize.value.width / 2) {
                                // snap right
                                widgetPosition.value = widgetPosition.value
                                    .copy(x = (-8 * this.density).toInt())
                            } else {
                                // snap left
                                widgetPosition.value = widgetPosition.value
                                    .copy(x = -1 * rootSize.value.width + buttonSize.value.width + (8 * this.density).toInt())
                            }
                        },
                        onDrag = { change, dragAmount ->
                            val finalPos =
                                widgetPosition.value + IntOffset(x = dragAmount.x.toInt(), y = dragAmount.y.toInt())
                            widgetPosition.value = finalPos
                        }
                    )
                }
                .onSizeChanged {
                    buttonSize.value = it
                },
        )
    }
}

@Preview
@Composable
fun SnifferIndicator(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val requests by SnifferDB.httpRequest.collectAsStateWithLifecycle(emptyList())
    val errorCount by rememberUpdatedState(requests.count { it is HttpRequestState.Error })
    val successCount by rememberUpdatedState(requests.count { it is HttpRequestState.Success })
    val spoofCount by rememberUpdatedState(requests.count { it is HttpRequestState.Spoofed })
    val pendingCount by rememberUpdatedState(requests.count { it is HttpRequestState.Executing })
    var isMinimized by remember { mutableStateOf(false) }

    val maximizeJob = remember { mutableStateOf<Job?>(null) }
    val temporaryMaximize = {
        maximizeJob.value?.cancel()
        maximizeJob.value = scope.launch {
            isMinimized = false
            delay(3000)
            ensureActive()
            isMinimized = true
        }
    }

    DisposableEffect(requests) {
        temporaryMaximize.invoke()

        onDispose {
            maximizeJob.value?.cancel()
        }
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable {
                if (isMinimized)
                    temporaryMaximize.invoke()
                else
                    onClick.invoke()
            }
            .border(BorderStroke(1f.dp, Color(0xffffffff)), RoundedCornerShape(24.dp))
            .background(
                Color(0xff2e2e2e).copy(
                    alpha = if (isMinimized) .3f else .9f
                )
            )
            .padding(horizontal = if (isMinimized) 4.dp else 8.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isMinimized)
            CircleStateIndicator(
                IndicatorState.Executing,
                modifier = Modifier.alpha(
                    if (pendingCount > 0) 1f else .1f
                ),
            )
        else
            Row(
                modifier = Modifier.alpha(
                    if (pendingCount > 0) 1f else .1f
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Executing(
                    modifier = Modifier.size(18.dp)
                )
                Text(pendingCount.toString(), color = Color.White)
            }

        if (isMinimized)
            CircleStateIndicator(
                IndicatorState.Error,
                modifier = Modifier.alpha(
                    if (errorCount > 0) 1f else .1f
                ),
            )
        else
            Row(
                modifier = Modifier.alpha(
                    if (errorCount > 0) 1f else .1f
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Error(
                    modifier = Modifier.size(18.dp)
                )
                Text(errorCount.toString(), color = Color.White)
            }

        if (isMinimized)
            CircleStateIndicator(
                IndicatorState.Success,
                modifier = Modifier.alpha(
                    if (successCount > 0) 1f else .1f
                ),
            )
        else
            Row(
                modifier = Modifier.alpha(
                    if (successCount > 0) 1f else .1f
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Success(
                    modifier = Modifier.size(18.dp)
                )
                Text(successCount.toString(), color = Color.White)
            }

        if (isMinimized)
            CircleStateIndicator(
                IndicatorState.Spoofed,
                modifier = Modifier.alpha(
                    if (spoofCount > 0) 1f else .1f
                ),
            )
        else
            Row(
                modifier = Modifier.alpha(
                    if (spoofCount > 0) 1f else .1f
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Spoofed(
                    modifier = Modifier.size(18.dp)
                )
                Text(spoofCount.toString(), color = Color.White)
            }

    }
}