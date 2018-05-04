package me.hatcloud.sms2mail.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.content.PermissionChecker.checkSelfPermission
import me.hatcloud.sms2mail.data.SMS
import me.hatcloud.sms2mail.service.SMS2MailService


private const val REQUEST_CODE_ASK_PERMISSIONS = 124


fun getSmsFromPhone(activity: Activity): List<SMS> {

    val cr = activity.contentResolver ?: return ArrayList()
    val projection = arrayOf("_id", "address", "person", "body", "date", "thread_id", "read"
            , "protocol", "status", "type")
    val SMS_INBOX = Uri.parse("content://sms/")
    val cur = cr.query(SMS_INBOX, projection, null, null, "date desc") ?: return ArrayList()
    val smsList = ArrayList<SMS>()
    while (cur.moveToNext()) {
        SMS(cur.getLong(cur.getColumnIndex("_id")),
                cur.getString(cur.getColumnIndex("address")),   // 手机号
                cur.getString(cur.getColumnIndex("person")),    // 联系人姓名列表
                cur.getString(cur.getColumnIndex("body")),      // 短信正文
                cur.getLong(cur.getColumnIndex("date")),        // 日期事件戳
                cur.getLong(cur.getColumnIndex("thread_id")),   // 对话的序号，如 100，与同一个手机号互发的短信，其序号是相同的
                cur.getInt(cur.getColumnIndex("read")),         // 是否阅读 0 未读，1 已读
                cur.getInt(cur.getColumnIndex("protocol")),     // 协议 0 SMS_PROTO 短信，1 MMS_PROTO 彩信
                cur.getInt(cur.getColumnIndex("status")),       // 短信状态 - 1 接收，0complete,64pending,128failed
                cur.getInt(cur.getColumnIndex("type")))         // 短信类型 1 是接收到的，2 是已发出
                .let {
                    smsList.add(it)
                }
    }
    cur.close()
    return smsList
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

fun isSMS2MailServiceRun(context: Context?): Boolean {
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
        if (serviceList[i].service.className == SMS2MailService::class.java.name) {
            isRunning = true
            break
        }
    }
    return isRunning
}