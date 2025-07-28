package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

expect fun openDebugger()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebuggerScreen() {
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
                RequestListPane(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    onClick = {

                    },
                )
            }
        }
    }
}