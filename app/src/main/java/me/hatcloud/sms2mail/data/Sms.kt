package me.hatcloud.sms2mail.data

import android.database.Cursor

data class Sms(
    val _id: Long,               // 短信序号，如 100
    val address: String?,         // 发件人地址，即手机号，如 + 86138138000
    val person: String?,         // 发件人，如果发件人在通讯录中则为具体姓名，陌生人为 null
    val body: String,            // 短信具体内容
    val date: Long,              // 日期，long 型，如 1346988516，可以对日期显示格式进行设置
    val thread_id: Long,         // 对话的序号，如 100，与同一个手机号互发的短信，其序号是相同的
    val read: Int = 1,           // 是否阅读 0 未读，1 已读
    val protocol: Int = 0,       // 协议 0 SMS_PROTO 短信，1 MMS_PROTO 彩信
    val status: Int = -1,        // 短信状态 - 1 接收，0complete,64pending,128failed
    val type: Int = 1
) {         // 短信类型 1 是接收到的，2 是已发出
    constructor(cur: Cursor) : this(
        cur.getLong(cur.getColumnIndex("_id")),
        cur.getString(cur.getColumnIndex("address")),
        cur.getString(cur.getColumnIndex("person")),
        cur.getString(cur.getColumnIndex("body")),
        cur.getLong(cur.getColumnIndex("date")),
        cur.getLong(cur.getColumnIndex("thread_id")),
        cur.getInt(cur.getColumnIndex("read")),
        cur.getInt(cur.getColumnIndex("protocol")),
        cur.getInt(cur.getColumnIndex("status")),
        cur.getInt(cur.getColumnIndex("type"))
    )

    companion object {

        fun generateFakeItem(): Sms = Sms(
            _id = 0,
            address = "dfsadf",
            person = "safsdfa",
            body = "afsdafwefwafas",
            date = 2312313L,
            thread_id = 231,
            read = 0,
        )

    }

}

enum class SmsType(val value: Int) {
    RECEIVED(1), SENT(2)
}

enum class SmsStatus(val value: Int) {
    RECEIVED(-1), COMPLETE(0), PENDING(64), FAILED(128)
}

enum class SmsProtocol(val value: Int) {
    SMS_PROTO(0), MMS_PROTO(1)
}

enum class SmsReadStatus(val value: Int) {
    UNREAD(0), MARK_READ(1)
}
