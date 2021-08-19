package me.hatcloud.sms2mail.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import me.hatcloud.sms2mail.core.SmsListenerMgr
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.utils.SmsListener
import me.hatcloud.sms2mail.utils.checkPermission
import me.hatcloud.sms2mail.utils.requestPermission

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val smsListener:SmsListener =  object : SmsListener{
        override fun onSmsReceived(sms: Sms) {
            mainViewModel.onSmsReceived(sms)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppScreen(mainViewModel = mainViewModel)
        }
        if (!checkPermission(this, Manifest.permission.READ_SMS)) {
            requestPermission(this, Manifest.permission.READ_SMS)
        }
        mainViewModel.onRefreshSms()
        SmsListenerMgr.register(smsListener)
    }

    override fun onDestroy() {
        SmsListenerMgr.unregister(smsListener)
        super.onDestroy()
    }
}
