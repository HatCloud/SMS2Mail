package me.hatcloud.sms2mail.core

import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.utils.SMS_INBOX_URI
import me.hatcloud.sms2mail.utils.getSmsContentObserverCursor

object SmsObserver : ContentObserver(Handler()) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange)
        val uri1: Uri? = uri ?: Uri.parse(SMS_INBOX_URI.toString())
        if (uri1.toString() == "content://sms/raw") {
            return
        }
        var cursor: Cursor? = null
        try {
            cursor = getSmsContentObserverCursor(uri1)
            if (cursor != null && cursor.moveToFirst()) {
                SmsListenerMgr.notifyAllListeners(Sms(cursor))
            }
        } finally {
            cursor?.close()

        }
    }
    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        onChange(selfChange, null)
    }
}