package me.hatcloud.sms2mail.data

data class Configuration(var email: String,
                         var password: String,
                         var smtpHost: String,
                         var smtpPort: String,
                         var securityType: SecurityType,
                         var emailToForward: String)

enum class SecurityType(val value: Int) {
    NONE(0), SSL(1), TLS(2)
}

fun Int.toSecurityType(): SecurityType {
    return SecurityType.values().find { it.value == this } ?: SecurityType.NONE
}