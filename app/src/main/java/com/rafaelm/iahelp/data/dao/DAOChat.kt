package com.rafaelm.iahelp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rafaelm.iahelp.data.entity.EntityChat
import com.rafaelm.iahelp.data.entity.EntityUser
import com.rafaelm.iahelp.data.model.firebase.User

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

    @Query("DELETE FROM user")
    fun deleteUserData()

    @Query("DELETE FROM chat_table")
    fun deleteChatData()
}