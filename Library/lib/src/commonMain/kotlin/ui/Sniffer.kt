package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import ui.component.Error
import ui.component.Pending
import ui.component.Spoof
import ui.component.Success
import data.HttpRequestState
import data.SnifferDB
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.absoluteValue

@Composable
fun Sniffer(content: @Composable () -> Unit) {
    val density = LocalDensity.current
    val rootSize = remember { mutableStateOf(IntSize.Zero) }
    val buttonSize = remember { mutableStateOf(IntSize.Zero) }
    val widgetPosition = remember { mutableStateOf(IntOffset((-8 * density.density).toInt(), 0)) }

    Box(
        modifier = Modifier.onSizeChanged {
            rootSize.value = it
        }
    ) {
        content.invoke()

        SnifferFloatingWidget(
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
fun SnifferFloatingWidget(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val requests by SnifferDB.httpRequest.collectAsStateWithLifecycle()
    val errorCount by rememberUpdatedState(requests.count { it is HttpRequestState.Error })
    val successCount by rememberUpdatedState(requests.count { it is HttpRequestState.Success })
    val spoofCount by rememberUpdatedState(requests.count { it is HttpRequestState.Spoofed })
    val pendingCount by rememberUpdatedState(requests.count { it is HttpRequestState.Executing })

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick.invoke()
            }
            .border(BorderStroke(1f.dp, Color(0xffffffff)), RoundedCornerShape(8.dp))
            .background(Color(0xff2e2e2e))
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnimatedVisibility(
            visible = errorCount > 0,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Error()
                Text(errorCount.toString(), color = Color.White)
            }
        }
        AnimatedVisibility(
            visible = successCount > 0,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Success()
                Text(successCount.toString(), color = Color.White)
            }
        }
        AnimatedVisibility(
            visible = spoofCount > 0,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Spoof()
                Text(spoofCount.toString(), color = Color.White)
            }
        }
        AnimatedVisibility(
            visible = pendingCount > 0,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Pending()
                Text(pendingCount.toString(), color = Color.White)
            }
        }
    }
}