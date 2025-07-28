package ui

import data.Sniffer

actual fun openDebugger() {
    Sniffer.launchDebuggerActivity.invoke()
}

