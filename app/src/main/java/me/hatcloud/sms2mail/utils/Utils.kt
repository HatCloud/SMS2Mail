package me.hatcloud.sms2mail.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.content.PermissionChecker.checkSelfPermission
import me.hatcloud.sms2mail.Sms2MailApp
import me.hatcloud.sms2mail.core.MailPasswordAuthenticator
import me.hatcloud.sms2mail.core.Sms2MailService
import me.hatcloud.sms2mail.core.SmsObserver
import me.hatcloud.sms2mail.data.MailInfo
import me.hatcloud.sms2mail.data.Sms
import android.net.Uri
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


private const val REQUEST_CODE_ASK_PERMISSIONS = 124


fun getAllSmsFromPhone(): List<Sms> {
    val cur = getSmsContentObserverCursor() ?: return ArrayList()
    val smsList = ArrayList<Sms>()
    while (cur.moveToNext()) {
        Sms(cur).let { smsList.add(it) }
    }
    cur.close()
    return smsList
}

fun getSmsContentObserverCursor(): Cursor? {
    return Sms2MailApp.getInstance().contentResolver?.query(SMS_INBOX_URI, SMS_PROJECTION, null, null
            , "date desc")
}
fun getSmsContentObserverCursor(uri: Uri?): Cursor? {
    return uri?.let {
        Sms2MailApp.getInstance().contentResolver?.query(it, SMS_PROJECTION, null, null
                , "date desc")
    }
}

fun registerSmsObserver(smsObserver : SmsObserver) {
    Sms2MailApp.getInstance().contentResolver.registerContentObserver(SMS_INBOX_URI, true, smsObserver);
}

fun unregisterSmsObserver(smsObserver : SmsObserver) {
    Sms2MailApp.getInstance().contentResolver.unregisterContentObserver(smsObserver);
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

fun sendMail(mailInfo: MailInfo): Boolean {
    LogUtil.d("send mail: $mailInfo")
    val properties = mailInfo.getProperties()

    // 根据邮件会话属性和密码验证器构造一个发送邮件的session
    val sendMailSession = if (mailInfo.needValid) {
        Session.getDefaultInstance(properties, MailPasswordAuthenticator(mailInfo.userName, mailInfo.password))
    } else {
        Session.getDefaultInstance(properties)
    }

    try {
        // 根据session创建一个邮件消息
        val mailMessage = MimeMessage(sendMailSession)
        mailMessage.addHeader("Content-type", "text/HTML; charset=UTF-8")
        mailMessage.addHeader("format", "flowed")
        mailMessage.addHeader("Content-Transfer-Encoding", "8bit")
        // 创建邮件发送者地址
        val from = InternetAddress(mailInfo.fromAddress)
        // 设置邮件消息的发送者
        mailMessage.setFrom(from)
        // 创建邮件的接收者地址，并设置到邮件消息中
        val to = InternetAddress(mailInfo.toAddress)
        mailMessage.setRecipient(Message.RecipientType.TO, to)
        // 设置邮件消息的主题
        mailMessage.setSubject(mailInfo.subject, "UTF-8")
        // 设置邮件消息发送的时间
        mailMessage.sentDate = Date()

        // 设置邮件消息的主要内容
        mailMessage.setText(mailInfo.content, "UTF-8")
        // 发送邮件
        Transport.send(mailMessage)
        return true
    } catch (ex: MessagingException) {
        ex.printStackTrace()
    }

    return false
}