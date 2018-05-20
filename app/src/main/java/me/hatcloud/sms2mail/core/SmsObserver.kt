package me.hatcloud.sms2mail.core

import android.database.ContentObserver
import android.database.Cursor
import android.os.Handler
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.utils.getSmsContentObserverCursor

object SmsObserver : ContentObserver(Handler()) {

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        var cursor: Cursor? = null
        try {
            cursor = getSmsContentObserverCursor()
            if (cursor != null && cursor.moveToFirst()) {
                SmsListenerMgr.notifyAllListeners(Sms(cursor))
            }
        } finally {
            cursor?.close()

        }
    }
}