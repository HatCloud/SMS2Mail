package me.hatcloud.sms2mail.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.content.PermissionChecker.checkSelfPermission
import me.hatcloud.sms2mail.core.Sms2MailService
import me.hatcloud.sms2mail.data.Sms


private const val REQUEST_CODE_ASK_PERMISSIONS = 124


fun getAllSmsFromPhone(context: Context): List<Sms> {
    val cur = getSmsContentObserverCursor(context) ?: return ArrayList()
    val smsList = ArrayList<Sms>()
    while (cur.moveToNext()) {
        Sms(cur).let { smsList.add(it) }
    }
    cur.close()
    return smsList
}

fun getSmsContentObserverCursor(context: Context): Cursor? {
    return context.contentResolver?.query(SMS_INBOX_URI, SMS_PROJECTION, null, null
            , "date desc")
}

fun checkPermission(activity: Activity, permission: String): Boolean =
        checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

fun requestPermission(activity: Activity, permission: String) {
    requestPermissions(activity, arrayOf(permission),
            REQUEST_CODE_ASK_PERMISSIONS);
}

fun addPermission(activity: Activity, permissionsList: MutableList<String>, permission: String): Boolean {
    if (!checkPermission(activity, permission)) {
        permissionsList.add(permission)
        // Check for Rationale Option
        if (!shouldShowRequestPermissionRationale(activity, permission))
            return false
    }
    return true
}

fun isSms2MailServiceRun(context: Context?): Boolean {
    if (context == null) {
        return false
    }
    var isRunning = false
    val activityManager = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val serviceList = activityManager
            .getRunningServices(30)

    if (serviceList.size <= 0) {
        return false
    }

    for (i in serviceList.indices) {
        if (serviceList[i].service.className == Sms2MailService::class.java.name) {
            isRunning = true
            break
        }
    }
    return isRunning
}