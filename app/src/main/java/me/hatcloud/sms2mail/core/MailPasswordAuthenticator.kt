package me.hatcloud.sms2mail.core

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication


class MailPasswordAuthenticator(private val username: String, private val password: String) : Authenticator() {
    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(username, password)
    }
}