package data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import okio.Path.Companion.toPath

actual object SnifferDB {
    private var datastore: DataStore<Preferences>? = null

    internal fun initiate(context: Context) {
        // already initiated
        if (datastore != null) return

        val path = context.filesDir.resolve("singularity_sniffer_datastore.preferences_pb").absolutePath.toPath()
        datastore = PreferenceDataStoreFactory.createWithPath(
            produceFile = { path }
        )
    }

    actual val httpRequestRecord: Flow<List<HttpRequestState>>
        get() = datastore?.data
            ?.map {
                it[stringSetPreferencesKey("HTTP_REQUEST_STATES")]
                    ?.let { set ->
                        set.map { e -> Json.decodeFromString(e) }
                    }
                    ?: emptyList()
            }
            ?: error(IllegalStateException("SnifferDB is Not propertly initiated"))

    actual val httpRequests: Flow<List<HttpRequestState>>
        get() = httpRequestRecord.map { reqs ->
            reqs.groupBy { it.id }
                .map {
                    it.value.maxBy { it.timeSignMillis }
                }
                .sortedByDescending { it.timeSignMillis }
        }

    actual suspend fun httpCall(httpRequestState: HttpRequestState) {
        datastore?.updateData {
            with(it.toMutablePreferences()) {
                val previousSet = it[stringSetPreferencesKey("HTTP_REQUEST_STATES")] ?: emptySet()
                val newSet = previousSet + Json.encodeToString(httpRequestState)
                set(stringSetPreferencesKey("HTTP_REQUEST_STATES"), newSet)
                this
            }
        } ?: error(IllegalStateException("SnifferDB is Not propertly initiated"))
    }
}