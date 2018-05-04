package me.hatcloud.sms2mail.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_toggle.*
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.core.Sms2MailService
import me.hatcloud.sms2mail.utils.ACTION
import me.hatcloud.sms2mail.utils.isSms2MailServiceRun


/**
 * A simple [Fragment] subclass for toggle sms to mail service.
 * Use the [ToggleFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ToggleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_toggle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        btnToggle.setOnClickListener {
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
            layoutContainer.setBackgroundResource(R.color.primary)
            btnToggle.setText(R.string.stop)
        } else {
            layoutContainer.setBackgroundResource(R.color.background)
            btnToggle.setText(R.string.start)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ToggleFragment()
    }
}
