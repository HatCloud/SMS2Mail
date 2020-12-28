package me.hatcloud.sms2mail.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.core.SmsListenerMgr
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.ui.adapter.CommonListAdapter
import me.hatcloud.sms2mail.ui.viewholder.ViewHolderManager
import me.hatcloud.sms2mail.utils.SmsListener
import me.hatcloud.sms2mail.utils.getAllSmsFromPhone

class SmsFragment : Fragment(), SmsListener {


    private var rvSms: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private val smsAdapter by lazy {
        CommonListAdapter<Sms>(ViewHolderManager.ViewHolderType.SMS).apply {
            activity?.let {
                initData(getAllSmsFromPhone())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sms_list, container, false)
        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).apply {
            setOnRefreshListener {
                smsAdapter.initData(getAllSmsFromPhone())
                this.isRefreshing = false
            }
        }
        rvSms = view.findViewById<RecyclerView>(R.id.rvSms).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = smsAdapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SmsListenerMgr.register(this)
    }

    override fun onDestroyView() {
        SmsListenerMgr.unregister(this)
        super.onDestroyView()
    }

    override fun onSmsReceived(sms: Sms) {
        smsAdapter.addData(sms, 0)
        rvSms?.smoothScrollToPosition(0)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SmsFragment()
    }
}
