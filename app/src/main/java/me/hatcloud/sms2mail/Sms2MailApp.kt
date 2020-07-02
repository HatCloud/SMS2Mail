package me.hatcloud.sms2mail

import android.app.Application
import me.hatcloud.sms2mail.core.SmsObserver
import me.hatcloud.sms2mail.utils.registerSmsObserver
import me.hatcloud.sms2mail.utils.unregisterSmsObserver

class Sms2MailApp : Application() {

    companion object {
        private lateinit var singleton: Sms2MailApp

        fun getInstance(): Sms2MailApp {
            return singleton
        }
    }

    override fun onCreate() {
        super.onCreate()
        singleton = this
        unregisterSmsObserver(SmsObserver)
        registerSmsObserver(SmsObserver)
    }
}
