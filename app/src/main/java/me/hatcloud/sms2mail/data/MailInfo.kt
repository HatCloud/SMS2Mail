package me.hatcloud.sms2mail.data

import me.hatcloud.sms2mail.utils.ConfigurationUtil
import java.util.*

data class MailInfo(private val mailServerHost: String,                     // 发送邮件的服务器的IP
                    private val mailServerPort: String,                     // 发送邮件的服务器的端口
                    val fromAddress: String,                        // 邮件发送者的地址
                    val toAddress: String,                          // 邮件接收者的地址
                    val userName: String,                           // 登陆邮件发送服务器的用户名
                    val password: String,                           // 登陆邮件发送服务器的密码
                    val subject: String,                            // 邮件主题
                    val content: String,                            // 邮件的文本内容
                    val needValid: Boolean = true,                  // 是否需要身份验证
                    val attachFileNames: List<String>? = null) {    // 邮件附件的文件名

    constructor(configuration: Configuration, subject: String, content: String)
            : this(configuration.smtpHost,
            configuration.smtpPort,
            configuration.email,
            configuration.emailToForward,
            configuration.email,
            configuration.password,
            subject,
            content)

    constructor(sms: Sms) : this(ConfigurationUtil.configuration, "Sms from ${sms.address}",
            sms.body)

    /**
     * 获得邮件会话属性
     */
    fun getProperties(): Properties {
        return when (ConfigurationUtil.configuration.securityType) {
            SecurityType.NONE -> getNormalProperties()
            SecurityType.SSL -> getSSLProperties()
            SecurityType.TLS -> getTLSProperties()
        }
    }

    private fun getNormalProperties(): Properties {
        val p = Properties()
        p["mail.smtp.host"] = this.mailServerHost
        p["mail.smtp.port"] = this.mailServerPort
        p["mail.smtp.enable"] = true
        p["mail.smtp.auth"] = if (needValid) "true" else "false"
        return p
    }

    private fun getTLSProperties(): Properties {
        val p = Properties()
        p["mail.smtp.host"] = this.mailServerHost
        p["mail.smtp.port"] = this.mailServerPort
        p["mail.smtp.enable"] = true
        p["mail.smtp.starttls.enable"] = true
        p["mail.smtp.auth"] = if (needValid) "true" else "false"
        return p
    }

    private fun getSSLProperties(): Properties {
        val p = Properties()
        p["mail.smtp.host"] = this.mailServerHost
        p["mail.smtp.port"] = this.mailServerPort
        p["mail.smtp.socketFactory.port"] = this.mailServerPort
        p["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory" //SSL Factory Class
        p["mail.smtp.auth"] = if (needValid) "true" else "false"
        return p
    }
}