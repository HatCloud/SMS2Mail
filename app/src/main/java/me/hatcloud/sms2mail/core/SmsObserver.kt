package me.hatcloud.sms2mail.core

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.os.Handler
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.utils.getSmsContentObserverCursor

class SmsObserver(val context: Context, handler: Handler) : ContentObserver(handler) {

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        var cursor: Cursor? = null
        try {
            cursor = getSmsContentObserverCursor(context)
            if (cursor != null && cursor.moveToFirst()) {
                SmsListenerMgr.notifyAllListeners(Sms(cursor))
            }
        } finally {
            cursor?.close()

        }
    }
}