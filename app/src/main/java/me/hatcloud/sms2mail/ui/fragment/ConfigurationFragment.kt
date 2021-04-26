package me.hatcloud.sms2mail.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.data.Configuration
import me.hatcloud.sms2mail.data.SecurityType
import me.hatcloud.sms2mail.databinding.FragmentConfigurationBinding
import me.hatcloud.sms2mail.utils.ConfigurationUtil

class ConfigurationFragment : Fragment() {

    private var _binding: FragmentConfigurationBinding? = null
    private val binding get() = _binding!!

    private val allEditableView by lazy {
        arrayOf(binding.inputEmail,
                binding.inputPassword,
                binding.inputSmtpServerHost,
                binding.inputSmtpServerPort,
                binding.radioSecurity,
                binding.radioBtnSecurityNone,
                binding.radioBtnSecuritySSL,
                binding.radioBtnSecurityTLS,
                binding.inputEmailToForward)
    }

    private var isEditMode = false
        set(value) {
            if (field == value) {
                return
            }
            allEditableView.forEach {
                it.isEnabled = value
            }
            binding.btnEditAndApply.setText(if (value) R.string.apply else R.string.edit)
            if (!value) {
                ConfigurationUtil.configuration = Configuration(binding.inputEmail.text.toString(),
                        binding.inputSmtpServerHost.text.toString(),
                        binding.inputSmtpServerPort.text.toString(),
                        when (binding.radioSecurity.checkedRadioButtonId) {
                            R.id.radioBtnSecurityNone -> SecurityType.NONE
                            R.id.radioBtnSecuritySSL -> SecurityType.SSL
                            R.id.radioBtnSecurityTLS -> SecurityType.TLS
                            else -> SecurityType.NONE
                        },
                        binding.inputEmailToForward.text.toString())
                        .apply {
                            password = binding.inputPassword.text.toString()
                        }
            }
            field = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
       _binding = FragmentConfigurationBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val configuration: Configuration? = ConfigurationUtil.configuration
        if (configuration != null) {
            binding.inputEmail.setText(configuration.email)
            binding.inputPassword.setText(configuration.password)
            binding.inputSmtpServerHost.setText(configuration.smtpHost)
            binding.inputSmtpServerPort.setText(configuration.smtpPort)
            when (configuration.securityType) {
                SecurityType.NONE -> binding.radioSecurity.check(R.id.radioBtnSecurityNone)
                SecurityType.SSL -> binding.radioSecurity.check(R.id.radioBtnSecuritySSL)
                SecurityType.TLS -> binding.radioSecurity.check(R.id.radioBtnSecurityTLS)
            }
            binding.inputEmailToForward.setText(configuration.emailToForward)
        }

        binding.btnEditAndApply.setText(R.string.edit)
        binding.btnEditAndApply.setOnClickListener {
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
