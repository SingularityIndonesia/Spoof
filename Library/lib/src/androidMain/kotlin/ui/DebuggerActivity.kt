package ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import data.Sniffer

@SuppressLint("RestrictedApi")
class DebuggerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        Sniffer.initiate(this)

        setContent {
            DebuggerScreen()
        }
    }
}