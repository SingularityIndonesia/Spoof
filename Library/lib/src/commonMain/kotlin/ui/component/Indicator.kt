package ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.stefanusayudha.spoof.lib.generated.resources.*
import org.jetbrains.compose.resources.painterResource

const val colorError = 0xfff44336
const val colorSuccess = 0xff00e500
const val colorExecuting = 0xffabafb1
const val colorSpoofed = 0xffffbb00

sealed class IndicatorState {
    object Error : IndicatorState()
    object Success : IndicatorState()
    object Executing : IndicatorState()
    object Spoofed : IndicatorState()
}

@Composable
fun CircleStateIndicator(state: IndicatorState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(
                Color(
                    when (state) {
                        is IndicatorState.Error -> colorError
                        is IndicatorState.Success -> colorSuccess
                        is IndicatorState.Executing -> colorExecuting
                        is IndicatorState.Spoofed -> colorSpoofed
                    }
                )
            )
    )
}

@Composable
fun Error(
    modifier: Modifier = Modifier.size(24.dp),
) {
    Icon(
        modifier = modifier,
        painter = painterResource(Res.drawable.ic_error_24),
        tint = Color(colorError),
        contentDescription = null
    )
}

@Composable
fun Success(
    modifier: Modifier = Modifier.size(24.dp),
) {
    Icon(
        modifier = modifier,
        painter = painterResource(Res.drawable.ic_check_circle_24),
        tint = Color(colorSuccess),
        contentDescription = null
    )
}

@Composable
fun Executing(
    modifier: Modifier = Modifier.size(24.dp),
) {
    Icon(
        modifier = modifier,
        painter = painterResource(Res.drawable.ic_pending_24),
        tint = Color(colorExecuting),
        contentDescription = null
    )
}

@Composable
fun Spoofed(
    modifier: Modifier = Modifier.size(24.dp),
) {
    Icon(
        modifier = modifier,
        painter = painterResource(Res.drawable.ic_alt_route_24),
        tint = Color(colorSpoofed),
        contentDescription = null
    )
}