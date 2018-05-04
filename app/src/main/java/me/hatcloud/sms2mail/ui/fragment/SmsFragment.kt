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
import me.hatcloud.sms2mail.core.SmsListenerMgr
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.ui.adapter.CommonListAdapter
import me.hatcloud.sms2mail.ui.viewholder.ViewHolderManager
import me.hatcloud.sms2mail.utils.SmsListener
import me.hatcloud.sms2mail.utils.getAllSmsFromPhone

class SmsFragment : Fragment(), SmsListener {

    private val smsAdapter by lazy {
        CommonListAdapter<Sms>(ViewHolderManager.ViewHolderType.SMS).apply {
            activity?.let {
                initData(getAllSmsFromPhone(it))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sms_list, container, false)

        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = smsAdapter
                addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            }
        }

        SmsListenerMgr.register(this)
        return view
    }

    override fun onDestroyView() {
        SmsListenerMgr.unregister(this)
        super.onDestroyView()
    }

    override fun onSmsReceived(sms: Sms) {
        smsAdapter.addData(sms, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SmsFragment()
    }
}
