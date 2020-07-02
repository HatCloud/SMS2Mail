package me.hatcloud.sms2mail.ui.adapter

interface ICommonListAdapter<T> {
    val isEmpty: Boolean

    val data: List<T>

    fun getItemCount(): Int

    fun getItem(position: Int): T?

    fun removeItem(position: Int)

    fun initData(data: List<T>)

    fun addData(data: List<T>)
}
