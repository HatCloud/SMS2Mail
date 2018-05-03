package me.hatcloud.sms2mail.ui.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.hatcloud.sms2mail.R

/**
 * A simple [Fragment] subclass.
 * Use the [ToggleFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ToggleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_toggle, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() = ToggleFragment()
    }
}
