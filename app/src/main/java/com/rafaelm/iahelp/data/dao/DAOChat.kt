package com.rafaelm.iahelp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafaelm.iahelp.data.entity.EntityChat
import com.rafaelm.iahelp.data.entity.EntityUser

@Dao
interface DAOChat{
    @Insert
    fun insertChat(chat: EntityChat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: EntityUser)

    @Query("SELECT * FROM user order by id")
    fun getUser(): LiveData<List<EntityUser>>

    @Query("SELECT * FROM chat_table order by msg_id DESC")
    fun getChat(): LiveData<List<EntityChat>>
}