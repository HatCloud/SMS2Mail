package me.hatcloud.sms2mail.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.ui.adapter.CommonListAdapter
import me.hatcloud.sms2mail.ui.viewholder.ViewHolderManager
import me.hatcloud.sms2mail.utils.getSmsFromPhone

class SmsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sms_list, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = CommonListAdapter<Sms>(ViewHolderManager.ViewHolderType.SMS).apply {
                    activity?.let {
                        initData(getSmsFromPhone(it))
                    }
                }
                addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            }
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = SmsFragment()
    }
}
