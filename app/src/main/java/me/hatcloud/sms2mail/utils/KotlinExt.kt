package me.hatcloud.sms2mail.utils

import android.app.Activity
import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.text.format.DateFormat
import android.view.View
import android.widget.Toast
import java.util.*

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Int.dp2px(context: Context?): Int {
    val scale = context?.resources?.displayMetrics?.density?.toInt()
    return (this * (scale ?: 2) + 0.5f).toInt()
}
fun Long.formatDate(context: Context): String {
    val date = Date(this)
    val dateFormat = DateFormat.getDateFormat(context)
    return dateFormat.format(date)
}

fun Any?.toString(): String {
    if (this == null) return "null"
    return toString()
}

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
    return lazy { findViewById<T>(idRes) }
}

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
    return lazy { findViewById<T>(idRes) }
}


