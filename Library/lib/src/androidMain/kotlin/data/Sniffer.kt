package data

import android.content.Context
import android.content.Intent
import ui.DebuggerActivity

object Sniffer {
    private var isInitiated: Boolean = false
    lateinit var launchDebuggerActivity: () -> Unit
        private set

    fun initiate(context: Context) {
        // already initiated
        if (isInitiated) return

        SnifferDB.initiate(context)
        launchDebuggerActivity = {
            val intent = Intent(
                context,
                DebuggerActivity::class.java
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        }
    }
}