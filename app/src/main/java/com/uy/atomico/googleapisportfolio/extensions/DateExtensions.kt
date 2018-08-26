package com.uy.atomico.googleapisportfolio.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by agustin.sivoplas@gmail.com on 8/15/18.
 * Atomico Labs
 */

fun String.stringToDate(pattern: String): Date {
    val format = SimpleDateFormat(pattern, Locale.US)
    return format.parse(this)
}

fun Date.formatToSimpleDayMonth(): String {
    val format = SimpleDateFormat("MM/dd", Locale.US)
    return format.format(this)
}

fun Date.formatToSimpleDate(useUTC: Boolean = false): String {
    val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    if(useUTC) {
        format.timeZone = TimeZone.getTimeZone("UTC")
    }
    return format.format(this)
}

fun Date.formatToYYMMDD(): String {
    val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)
    return format.format(this)
}

fun Date.formatTo24Hour(): String {
    val format = SimpleDateFormat("HH:mm", Locale.US)
    return format.format(this)
}

fun Date.formatTo12Hour(): String {
    val format = SimpleDateFormat("hh:mm a", Locale.US)
    return format.format(this)
}

fun Date.formatToHourWithSeconds(): String {
    val format = SimpleDateFormat("HH:mm:ss", Locale.US)
    return format.format(this)
}

fun Date.formatToDateTime(): String {
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
    return format.format(this)
}

fun Date.formatEEEMMMd(): String {
    val format = SimpleDateFormat("EEE, MMM d", Locale.US)
    return format.format(this)
}

fun Date.formatToISO(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US)
    return format.format(this)
}

fun Date.convertToUTC(): Date {
    return Date(this.time - Calendar.getInstance().timeZone.getOffset(this.time))
}

fun Date.isSameDate(date: Date): Boolean {
    val format = SimpleDateFormat("yyyyMMdd", Locale.US)
    return format.format(this) == format.format(date)
}

fun Date.getDateYear(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.YEAR)
}

fun Date.getDateMonth(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.MONTH)
}

fun Date.getDateDay(): Int {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.DAY_OF_MONTH)
}

fun Date.add(field: Int, amount: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(field, amount)

    this.time = cal.time.time

    cal.clear()

    return this
}

fun Date.addYears(years: Int): Date {
    return add(Calendar.YEAR, years)
}

fun Date.addMonths(months: Int): Date {
    return add(Calendar.MONTH, months)
}

fun Date.addDays(days: Int): Date {
    return add(Calendar.DAY_OF_MONTH, days)
}

fun Date.addHours(hours: Int): Date {
    return add(Calendar.HOUR_OF_DAY, hours)
}

fun Date.addMinutes(minutes: Int): Date {
    return add(Calendar.MINUTE, minutes)
}

fun Date.addSeconds(seconds: Int): Date {
    return add(Calendar.SECOND, seconds)
}