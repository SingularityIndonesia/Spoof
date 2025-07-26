import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntRect
import io.github.stefanusayudha.spoof.lib.generated.resources.Res
import io.github.stefanusayudha.spoof.lib.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
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