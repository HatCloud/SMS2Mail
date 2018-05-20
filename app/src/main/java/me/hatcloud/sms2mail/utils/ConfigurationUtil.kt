package me.hatcloud.sms2mail.utils

import android.content.Context
import me.hatcloud.sms2mail.Sms2MailApp
import me.hatcloud.sms2mail.data.Configuration
import me.hatcloud.sms2mail.data.toSecurityType

object ConfigurationUtil {

    private const val PREFERENCES_NAME = "me_hatcloud_sms_2_mail_configuration"

    private const val KEY_ACCOUNT_EMAIL = "account_email"
    private const val KEY_ACCOUNT_PASSWORD = "account_password"
    private const val KEY_SMTP_SERVER_HOST = "smtp_server_host "
    private const val KEY_SMTP_SERVER_PORT = "smtp_server_port"
    private const val KEY_SMTP_SECURITY = "smtp_security"
    private const val KEY_EMAIL_TO_FORWARD = "email_to_forward "

    private val sharePreference by lazy {
        Sms2MailApp.getInstance().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private var innerConfiguration: Configuration? = null

    var configuration: Configuration
        get() {
            return innerConfiguration ?: readConfiguration()
        }
        set(value) {
            writeConfiguration(value)
            innerConfiguration = value
        }


    private fun writeConfiguration(configuration: Configuration) {
        val edit = sharePreference.edit()
        edit.putString(KEY_ACCOUNT_EMAIL, configuration.email)
        edit.putString(KEY_ACCOUNT_PASSWORD, configuration.password)
        edit.putString(KEY_SMTP_SERVER_HOST, configuration.smtpHost)
        edit.putString(KEY_SMTP_SERVER_PORT, configuration.smtpPort)
        edit.putInt(KEY_SMTP_SECURITY, configuration.securityType.value)
        edit.putString(KEY_EMAIL_TO_FORWARD, configuration.emailToForward)
        edit.apply()
    }

    private fun readConfiguration(): Configuration {
        return Configuration(sharePreference.getString(KEY_ACCOUNT_EMAIL, ""),
                sharePreference.getString(KEY_ACCOUNT_PASSWORD, ""),
                sharePreference.getString(KEY_SMTP_SERVER_HOST, ""),
                sharePreference.getString(KEY_SMTP_SERVER_PORT, ""),
                sharePreference.getInt(KEY_SMTP_SECURITY, 0).toSecurityType(),
                sharePreference.getString(KEY_EMAIL_TO_FORWARD, "")
        )
    }
}