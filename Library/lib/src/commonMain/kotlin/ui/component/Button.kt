package ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.stefanusayudha.spoof.lib.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Composable
fun Error(
    modifier: Modifier = Modifier.size(24.dp),
) {
    Icon(
        painter = painterResource(Res.drawable.ic_error_24),
        tint = Color(0xfff44336),
        contentDescription = null
    )
}

@Composable
fun Success(
    modifier: Modifier = Modifier.size(24.dp),
) {
    Icon(
        painter = painterResource(Res.drawable.ic_check_circle_24),
        tint = Color(0xff00e500),
        contentDescription = null
    )
}

@Composable
fun Pending(
    modifier: Modifier = Modifier.size(24.dp),
) {
    Icon(
        painter = painterResource(Res.drawable.ic_pending_24),
        tint = Color(0xffabafb1),
        contentDescription = null
    )
}

@Composable
fun Spoof(
    modifier: Modifier = Modifier.size(24.dp),
) {
    Icon(
        painter = painterResource(Res.drawable.ic_alt_route_24),
        tint = Color(0xffffbb00),
        contentDescription = null
    )
}