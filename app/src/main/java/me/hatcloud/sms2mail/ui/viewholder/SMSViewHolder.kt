package me.hatcloud.sms2mail.ui.viewholder

import android.view.ViewGroup
import android.widget.TextView
import me.hatcloud.sms2mail.R
import me.hatcloud.sms2mail.data.Sms
import me.hatcloud.sms2mail.utils.formatDate

class SMSViewHolder(parent: ViewGroup) : BaseViewHolder<Sms>(parent, R.layout.list_item_sms) {
    private val sender: TextView = itemView.findViewById(R.id.sender)
    private val date: TextView  = itemView.findViewById(R.id.date)
    private val content: TextView  = itemView.findViewById(R.id.content)
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