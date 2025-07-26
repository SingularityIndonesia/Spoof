import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.stefanusayudha.spoof.lib.generated.resources.Res
import io.github.stefanusayudha.spoof.lib.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
fun Sniffer(content: @Composable () -> Unit) {
    val rootSize = remember { mutableStateOf(IntSize.Zero) }
    val widgetPosition = remember { mutableStateOf(IntOffset.Zero) }

    Box(
        Modifier
            .onSizeChanged {
                rootSize.value = it
            }
    ) {
        content.invoke()
        SnifferFloatingWidget(
            modifier = Modifier
                .padding(8.dp)
                .systemBarsPadding()
                .align(Alignment.TopEnd)
                .offset { widgetPosition.value }
                .draggable(
                    state = rememberDraggableState {
                        widgetPosition.value += IntOffset(x = 0, y = it.toInt())
                    },
                    orientation = Orientation.Vertical
                )
                .onGloballyPositioned {
                    val rect = it.positionInRoot()
                    val rootHeight = rootSize.value.height
                    val topDelta = rect.y - widgetPosition.value.y

                    if (widgetPosition.value.y < 0)
                        widgetPosition.value = IntOffset(x = 0, y = 0)

                    if (rect.y + topDelta > rootHeight)
                        widgetPosition.value = IntOffset(x = 0, y = (widgetPosition.value.y - 100))
                },
            onClick = {

            }
        )
    }
}

@Composable
fun SnifferFloatingWidget(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        shape = CircleShape,
        onClick = onClick
    ) {
        Image(
            modifier = Modifier.size(36.dp),
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = null
        )
    }
}