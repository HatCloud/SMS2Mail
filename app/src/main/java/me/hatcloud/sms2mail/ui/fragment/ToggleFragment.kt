package me.hatcloud.sms2mail.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.core.Sms2MailService
import me.hatcloud.sms2mail.databinding.FragmentToggleBinding
import me.hatcloud.sms2mail.utils.ACTION
import me.hatcloud.sms2mail.utils.isSms2MailServiceRun


/**
 * A simple [Fragment] subclass for toggle sms to mail service.
 * Use the [ToggleFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ToggleFragment : Fragment() {


    private var _binding: FragmentToggleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentToggleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        binding.btnToggle.setOnClickListener {
            if (isSms2MailServiceRun(activity)) {
                val stopIntent = Intent(activity, Sms2MailService::class.java)
                stopIntent.action = ACTION.STOP_SMS_2_MAIL_SERVICE_ACTION
                activity?.let {
                    it.startService(stopIntent)
                    updateUI(false)
                }

            } else {
                val startIntent = Intent(activity, Sms2MailService::class.java)
                startIntent.action = ACTION.START_SMS_2_MAIL_SERVICE_ACTION
                activity?.let {
                    it.startService(startIntent)
                    updateUI(true)
                }
            }
        }
    }

    private fun updateUI() {
        updateUI(isSms2MailServiceRun(activity))
    }

    private fun updateUI(isSMS2MailServiceRun: Boolean) {
        if (isSMS2MailServiceRun) {
            binding.layoutContainer.setBackgroundResource(R.color.primary)
            binding.btnToggle.setText(R.string.stop)
        } else {
            binding.layoutContainer.setBackgroundResource(R.color.background)
            binding.btnToggle.setText(R.string.start)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ToggleFragment()
    }
}
