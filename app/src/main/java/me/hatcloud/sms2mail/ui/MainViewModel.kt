package me.hatcloud.sms2mail.ui

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.hatcloud.sms2mail.Sms2MailApp
import me.hatcloud.sms2mail.core.Sms2MailService
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.utils.ACTION
import me.hatcloud.sms2mail.utils.getAllSmsFromPhone
import me.hatcloud.sms2mail.utils.isSms2MailServiceRun

class MainViewModel : ViewModel() {

    val running: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(isSms2MailServiceRun())
    }

    val isRefreshing: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val refreshSmsFlow = flow {
        val smsList = getAllSmsFromPhone()
        emit(smsList)
    }.flowOn(Dispatchers.IO)
        .onStart {
            isRefreshing.postValue(true)
        }
        .onCompletion {
            isRefreshing.postValue(false)
        }

    val smsList: MutableLiveData<List<Sms>> by lazy {
        MutableLiveData<List<Sms>>()
    }

    fun onToggle() {
        if (running.value == true) {
            val stopIntent = Intent(Sms2MailApp.getInstance(), Sms2MailService::class.java)
            stopIntent.action = ACTION.STOP_SMS_2_MAIL_SERVICE_ACTION
            Sms2MailApp.getInstance().startService(stopIntent)
            running.postValue(false)
        } else {
            val startIntent = Intent(Sms2MailApp.getInstance(), Sms2MailService::class.java)
            startIntent.action = ACTION.START_SMS_2_MAIL_SERVICE_ACTION
            Sms2MailApp.getInstance().startService(startIntent)
            running.postValue(true)
        }
    }

    fun onRefreshSms() {
        viewModelScope.launch {
            refreshSmsFlow.collect {
                smsList.postValue(it)
            }
        }
    }

    fun onSmsReceived(sms: Sms){
        smsList.value?.toMutableList()?.let {
            it.add(0, sms)
            smsList.postValue(it)
        }

    }
}