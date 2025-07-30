package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ui.pane.RequestListPane
import ui.pane.RequestPane

expect fun openDebugger()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebuggerScreen() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        MainNavigation(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier
) {
    val controller = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = controller,
        startDestination = "list"
    ) {
        composable(route = "list") {
            RequestListPane(
                onClick = {
                    controller.navigate("request/$it")
                },
            )
        }

        composable(
            route = "request/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) {
            val id = it.savedStateHandle.get<String>("id") ?: return@composable
            RequestPane(
                id = id
            )
        }
    }
}