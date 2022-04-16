package com.livepokernotes.business.domain.util

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateUtil
@Inject
constructor(
    private val dateFormat: SimpleDateFormat
){
    //date format: "16-04-2022 HH:mm:ss"

    fun String.toDate(): Date = dateFormat.parse(this) ?: Date()
    fun Date.formatToString(): String = dateFormat.format(this)
    fun getCurrentDate(): String = Date().formatToString()
}