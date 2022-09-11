package com.gerwalex.extensions

import java.text.SimpleDateFormat
import java.util.*

object LongExt {

    /**
     * Kovertiert ein Long in Timestamp-Format 'yyyy-MM-dd HH:mm:ss.SSS'
     */
    @JvmStatic
    fun Long.toTimeStamp(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        return formatter.format(this)
    }
}