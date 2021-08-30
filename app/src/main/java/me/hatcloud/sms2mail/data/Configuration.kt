package me.hatcloud.sms2mail.data

import me.hatcloud.sms2mail.utils.Coder
import me.hatcloud.sms2mail.utils.ConfigurationUtil
import java.security.spec.InvalidKeySpecException

data class Configuration(var email: String?,
                         var smtpHost: String?,
                         var smtpPort: String?,
                         var securityType: SecurityType,
                         var emailToForward: String?,
                         var encryptedPassword: String? = "") {
    var password
        get() = Coder.decodeAES(encryptedPassword.toString(), ConfigurationUtil.passwordAesKey) ?: ""
        set(unencryptedPassword) {
            encryptedPassword = try {
                Coder.encodeAES(unencryptedPassword
                        , ConfigurationUtil.passwordAesKey) ?: ""
            } catch (e: InvalidKeySpecException) {
                ConfigurationUtil.clearPasswordKey()
                ""
            }
        }
}

enum class SecurityType(val value: Int, val desc: String) {
    NONE(0, "None"), SSL(1, "SSL"), TLS(2, "SSL")
}

fun Int.toSecurityType(): SecurityType {
    return SecurityType.values().find { it.value == this } ?: SecurityType.NONE
}