package me.hatcloud.sms2mail.ui.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.hatcloud.sms2mail.R

/**
 * A simple [Fragment] subclass.
 *
 */
class ConfigurationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() = ConfigurationFragment()
    }
}
