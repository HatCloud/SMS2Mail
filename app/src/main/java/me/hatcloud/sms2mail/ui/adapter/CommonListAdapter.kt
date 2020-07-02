package me.hatcloud.sms2mail.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import me.hatcloud.sms2mail.ui.viewholder.BaseViewHolder
import me.hatcloud.sms2mail.ui.viewholder.ViewHolderManager
import java.util.*

/**
 * 适用于单一数据的列表的 Adapter
 *
 * @param <T> 列表中数据的类型如 Topic 等
</T> */
class CommonListAdapter<T : Any>(val viewHolderType: ViewHolderManager.ViewHolderType,
                                 var itemClickListener: ItemClickListener<T>? = null,
                                 var itemLongClickListener: ItemLongClickListener<T>? = null)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ICommonListAdapter<T> {

    override val data: MutableList<T> = ArrayList()
    override val isEmpty = data.isEmpty()

    var showEmptyViewListener: ShowEmptyViewListener? = null

    interface ItemClickListener<T> {
        fun onItemClick(position: Int, data: T?)
    }

    interface ItemLongClickListener<T> {
        fun onItemLongClick(position: Int, data: T?)
    }

    interface ShowEmptyViewListener {
        fun onShowEmptyView()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderManager.newInstance(viewHolderType, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is BaseViewHolder<*>) {
            return
        }
        val baseViewHolder: BaseViewHolder<T>
        try {
            baseViewHolder = holder as BaseViewHolder<T>
        } catch (e: ClassCastException) {
            e.printStackTrace()
            return
        }

        baseViewHolder.bindData(getItem(position))

        if (itemClickListener != null) {
            baseViewHolder.setItemClickListener(itemClickListener)
        }
        if (itemLongClickListener != null) {
            baseViewHolder.setItemLongClickListener(itemLongClickListener)
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItem(position: Int): T? {
        if (position < 0 || position >= itemCount) {
            return null
        }
        return data[position]
    }

    private fun notifyEmptyViewListener() = showEmptyViewListener?.onShowEmptyView()

    override fun initData(data: List<T>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun addData(data: List<T>) {
        val startIndex = this.data.size
        this.data.addAll(startIndex, data)
        notifyItemRangeInserted(startIndex, data.size)
    }

    fun addData(data: T) {
        this.data.add(data)
        notifyItemRangeInserted(this.data.size, 1)
    }

    fun addData(data: T, index: Int) {
        this.data.add(index, data)
        notifyItemRangeInserted(index, 1)
    }

    override fun removeItem(position: Int) {
        if (position < 0 || position >= itemCount) {
            return
        }

        data.removeAt(position)
        notifyItemRemoved(position)
        if (isEmpty) {
            notifyEmptyViewListener()
        }
    }

    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }
}