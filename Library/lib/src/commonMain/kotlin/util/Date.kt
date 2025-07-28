package util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Long.toDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val dateTime: LocalDateTime = instant.toLocalDateTime(timeZone = timeZone)

    val month = dateTime.monthNumber.toString().padStart(2, '0')
    val day = dateTime.dayOfMonth.toString().padStart(2, '0')
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')
    val second = dateTime.second.toString().padStart(2, '0')

    return "$month $day, $hour:$minute:$second"
}