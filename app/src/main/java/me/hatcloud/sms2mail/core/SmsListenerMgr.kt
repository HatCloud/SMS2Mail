package me.hatcloud.sms2mail.core

import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.utils.SmsListener
import me.hatcloud.sms2mail.utils.no
import me.hatcloud.sms2mail.utils.yes

object SmsListenerMgr {

    private val smsListeners = ArrayList<SmsListener>()

    fun register(smsListener: SmsListener) {
        smsListeners.contains(smsListener).no { smsListeners.add(smsListener) }
    }

    fun unregister(smsListener: SmsListener) {
        smsListeners.contains(smsListener).yes { smsListeners.remove(smsListener) }
    }

    fun notifyAllListeners(sms: Sms) {
        smsListeners.forEach {
            it.onSmsReceived(sms)
        }
    }
}