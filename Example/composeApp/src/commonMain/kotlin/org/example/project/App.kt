package org.example.project

import androidx.compose.runtime.Composable
import org.example.project.panel.MainPanel
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.SnifferIndicator

@Composable
@Preview
fun App() {
    ContextScope {
        MainPanel()
    }

    SnifferIndicator()
}