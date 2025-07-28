package ui

import data.Sniffer

actual fun openDebuggerWindow() {
    Sniffer.launchDebuggerActivity.invoke()
}

