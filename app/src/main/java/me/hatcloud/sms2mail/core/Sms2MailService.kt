package me.hatcloud.sms2mail.core

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.data.MailInfo
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.ui.MainActivity
import me.hatcloud.sms2mail.utils.*


class Sms2MailService : Service(), SmsListener{

    init {
        SmsListenerMgr.register(this)
    }

    override fun onDestroy() {
        SmsListenerMgr.unregister(this)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            ACTION.START_SMS_2_MAIL_SERVICE_ACTION -> {
                val notificationIntent = Intent(this, MainActivity::class.java)
                notificationIntent.action = ACTION.MAIN_ACTION
                val pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0)

                val channelId: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createChannel(getSystemService(NOTIFICATION_SERVICE) as NotificationManager).id
                } else {
                    ""
                }

                val notification = NotificationCompat.Builder(this, channelId)
                        .setContentTitle(getString(R.string.notification_title))
                        .setTicker(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_content))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true)
                        .build()

                startForeground(NOTIFICATION_ID.SMS_2_MAIL,
                        notification)
            }
            ACTION.STOP_SMS_2_MAIL_SERVICE_ACTION -> {
                stopForeground(true)
                stopSelf()
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onSmsReceived(sms: Sms) {
        val thread = object : Thread(){
            override fun run() {
                sendMail(MailInfo(sms))
                LogUtil.print("call send mail")
            }
        }
        thread.start()
    }

    @TargetApi(27)
    private fun createChannel(notificationManager: NotificationManager): NotificationChannel {
        val name = getString(R.string.notification_title)
        val description = getString(R.string.notification_content)
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(name, name, importance)
        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        notificationManager.createNotificationChannel(channel)
        return channel
    }
}