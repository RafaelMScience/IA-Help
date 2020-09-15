package com.rafaelm.projecthermes.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rafaelm.projecthermes.data.entity.EntityChat

@Dao
interface DAOChat{
    @Insert
    fun insertChat(chat: EntityChat)

    @Query("SELECT * FROM chat_table")
    fun getChat(): LiveData<List<EntityChat>>
}