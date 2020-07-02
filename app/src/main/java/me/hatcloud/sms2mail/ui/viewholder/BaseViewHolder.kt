package me.hatcloud.sms2mail.ui.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import me.hatcloud.sms2mail.ui.adapter.CommonListAdapter
import me.hatcloud.sms2mail.ui.adapter.ICommonListAdapter

abstract class BaseViewHolder<T : Any>(view: View, protected val adapter: ICommonListAdapter<*>? = null)
    : RecyclerView.ViewHolder(view) {

    protected lateinit var data: T
    protected val context: Context
        get() = itemView.context

    constructor(parent: ViewGroup, layoutResId: Int, adapter: ICommonListAdapter<*>? = null)
            : this(LayoutInflater.from(parent.context)
            .inflate(layoutResId, parent, false), adapter) {
    }

    fun bindData(data: T?): Boolean {
        if (data == null) {
            return false
        }
        this.data = data
        updateViewWithNewData()
        return true
    }

    protected abstract fun updateViewWithNewData()

    fun setItemClickListener(listener: CommonListAdapter.ItemClickListener<T>?) {
        if (itemView == null) {
            return
        }
        if (listener == null) {
            itemView.setOnClickListener(null)
            return
        }
        itemView.setOnClickListener { listener.onItemClick(adapterPosition, data) }
    }

    fun setItemLongClickListener(listener: CommonListAdapter.ItemLongClickListener<T>?) {
        if (itemView == null) {
            return
        }
        if (listener == null) {
            itemView.setOnLongClickListener(null)
            return
        }
        itemView.setOnLongClickListener {
            listener.onItemLongClick(adapterPosition, data)
            true
        }

    }
}
