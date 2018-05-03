package me.hatcloud.sms2mail.ui.viewholder

import android.view.ViewGroup
import me.hatcloud.sms2mail.ui.adapter.ICommonListAdapter

object ViewHolderManager {

    fun newInstance(type: ViewHolderType, parent: ViewGroup, adapter: ICommonListAdapter<*>? = null): BaseViewHolder<*> {
        return when (type) {
            ViewHolderType.SMS -> SMSViewHolder(parent)
        }
    }

    enum class ViewHolderType {
        SMS
    }
}
