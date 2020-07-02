package me.hatcloud.sms2mail.ui.viewholder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_sms.view.*
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.utils.formatDate

class SMSViewHolder(parent: ViewGroup) : BaseViewHolder<Sms>(parent, R.layout.list_item_sms) {
    override fun updateViewWithNewData() {
        with(itemView) {
            if (data.person == null) {
                sender.text = data.address
            } else {
                sender.text = context.getString(R.string.sender_name_with_address
                        , data.person, data.address)
            }
            date.text = data.date.formatDate(context)
            content.text = data.body
        }
    }
}