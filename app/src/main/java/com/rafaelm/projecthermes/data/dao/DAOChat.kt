package com.rafaelm.projecthermes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rafaelm.projecthermes.data.entity.EntityChat
import com.rafaelm.projecthermes.data.entity.EntityUser
import com.rafaelm.projecthermes.data.model.firebase.User

@Dao
interface DAOChat{
    @Insert
    fun insertChat(chat: EntityChat)

    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM chat_table order by msg_id DESC")
    fun getChat(): LiveData<List<EntityChat>>
}