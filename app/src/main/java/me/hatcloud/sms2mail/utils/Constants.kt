package me.hatcloud.sms2mail.utils

import android.net.Uri
import me.hatcloud.sms2mail.data.Sms

val SMS_INBOX_URI = Uri.parse("content://sms")!!
val SMS_PROJECTION = arrayOf("_id", "address", "person", "body", "date", "thread_id", "read"
        , "protocol", "status", "type")

interface ACTION {
    companion object {
        const val MAIN_ACTION = "me.hatcloud.sms2mail.action.main"
        const val START_SMS_2_MAIL_SERVICE_ACTION = "me.hatcloud.sms2mail.action.start_sms_2_mail_service"
        const val STOP_SMS_2_MAIL_SERVICE_ACTION = "me.hatcloud.sms2mail.action.stop_sms_2_mail_service"
    }
}

interface NOTIFICATION_ID {
    companion object {
        const val SMS_2_MAIL = 12146
    }
}

interface SmsListener {
    fun onSmsReceived(sms: Sms)
}