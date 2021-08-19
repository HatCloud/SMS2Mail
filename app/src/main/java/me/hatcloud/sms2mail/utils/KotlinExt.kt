package me.hatcloud.sms2mail.utils

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import me.hatcloud.sms2mail.Sms2MailApp
import java.util.*

inline fun <T> Boolean.yes(action: () -> T): Boolean {
    if (this) action()
    return this
}

inline fun <T> Boolean.no(action: () -> T): Boolean {
    if (!this) action()
    return this
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Int.dp2px(context: Context?): Int {
    val scale = context?.resources?.displayMetrics?.density?.toInt()
    return (this * (scale ?: 2) + 0.5f).toInt()
}

fun Long.formatDate(context: Context = Sms2MailApp.getInstance()): String {
    val date = Date(this)
    val dateFormat = DateFormat.getDateFormat(context)
    return dateFormat.format(date)
}

fun Any?.toString(): String {
    if (this == null) return "null"
    return toString()
}