package com.livepokernotes.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


object DateUtil {

    @Inject
    lateinit var dateFormat: SimpleDateFormat
    //date format: "2022-04-16 HH:mm:ss"

    private fun String.toDate(): Date = dateFormat.parse(this) ?: Date()
    fun Timestamp.toFormattedStringData(): String = this.toDate().formatToString()
    fun String.toTimestamp(): Timestamp = Timestamp(this.toDate())
    fun Date.formatToString(): String = dateFormat.format(this)
    fun getCurrentDate(): String = Date().formatToString()
}