import androidx.compose.runtime.Composable

@Composable
fun Sniffer(content: @Composable () -> Unit) {
    content.invoke()
}