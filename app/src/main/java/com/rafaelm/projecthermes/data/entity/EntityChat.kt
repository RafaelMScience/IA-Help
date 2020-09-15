package com.rafaelm.projecthermes.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_table")
data class EntityChat(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="msg_id")
    var msgId: Int = 0,

    @ColumnInfo(name = "send_msg")
    var sendMsg: String?,

    @ColumnInfo(name = "receiver_msg")
    var receiverMsg: String?,

    @ColumnInfo(name = "number_send")
    var number_send: Int?,

    @ColumnInfo(name = "number_receiver")
    var number_receiver: Int?,
)