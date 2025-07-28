package data

import android.content.Context

object Sniffer {
    fun initiate(context: Context) {
        SnifferDB.initiate(context)
    }
}