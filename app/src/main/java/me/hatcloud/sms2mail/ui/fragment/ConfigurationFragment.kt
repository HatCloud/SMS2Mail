package me.hatcloud.sms2mail.ui.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_configuration.*

import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.data.Configuration
import me.hatcloud.sms2mail.data.SecurityType
import me.hatcloud.sms2mail.utils.ConfigurationUtil

class ConfigurationFragment : Fragment() {

    private val allEditableView by lazy {
        arrayOf(inputEmail,
                inputPassword,
                inputSmtpServerHost,
                inputSmtpServerPort,
                radioSecurity,
                radioBtnSecurityNone,
                radioBtnSecuritySSL,
                radioBtnSecurityTLS,
                inputEmailToForward)
    }

    private var isEditMode = false
        set(value) {
            if (field == value) {
                return
            }
            allEditableView.forEach {
                it.isEnabled = value
            }
            btnEditAndApply.setText(if (value) R.string.apply else R.string.edit)
            if (!value) {
                ConfigurationUtil.configuration = Configuration(inputEmail.text.toString(),
                        inputSmtpServerHost.text.toString(),
                        inputSmtpServerPort.text.toString(),
                        when (radioSecurity.checkedRadioButtonId) {
                            R.id.radioBtnSecurityNone -> SecurityType.NONE
                            R.id.radioBtnSecuritySSL -> SecurityType.SSL
                            R.id.radioBtnSecurityTLS -> SecurityType.TLS
                            else -> SecurityType.NONE
                        },
                        inputEmailToForward.text.toString())
                        .apply {
                            password = inputPassword.text.toString()
                        }
            }
            field = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val configuration: Configuration = ConfigurationUtil.configuration
        inputEmail.setText(configuration.email)
        inputPassword.setText(configuration.password)
        inputSmtpServerHost.setText(configuration.smtpHost)
        inputSmtpServerPort.setText(configuration.smtpPort)
        when (configuration.securityType) {
            SecurityType.NONE -> radioSecurity.check(R.id.radioBtnSecurityNone)
            SecurityType.SSL -> radioSecurity.check(R.id.radioBtnSecuritySSL)
            SecurityType.TLS -> radioSecurity.check(R.id.radioBtnSecurityTLS)
        }
        inputEmailToForward.setText(configuration.emailToForward)
        btnEditAndApply.setText(R.string.edit)
        btnEditAndApply.setOnClickListener {
            isEditMode = !isEditMode
        }
        allEditableView.forEach {
            it.isEnabled = false
            it.clearFocus()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConfigurationFragment()
    }
}
