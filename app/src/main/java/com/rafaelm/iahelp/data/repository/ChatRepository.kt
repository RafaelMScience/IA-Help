package com.rafaelm.iahelp.data.repository

import android.app.Application
import com.rafaelm.iahelp.data.dao.DAOChat
import com.rafaelm.iahelp.data.database.DatabaseChat
import com.rafaelm.iahelp.data.entity.EntityChat
import com.rafaelm.iahelp.data.entity.EntityUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class ChatRepository (application: Application): CoroutineScope{
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var daoChat: DAOChat?

    init {
        val db = DatabaseChat.getInstance(application)
        daoChat = db.databaseChat
    }

    fun getChat() = daoChat?.getChat()

    fun getUser() = daoChat?.getUser()

    fun insetChat(chat: EntityChat){
        launch {
            insert(chat)
        }
    }

    fun insertUser(user: EntityUser){
        launch {
            insertFunUser(user)
        }
    }

    private suspend fun insertFunUser(user: EntityUser){
        withContext(Dispatchers.IO){
            daoChat?.insertUser(user)
        }
    }

    private suspend fun insert(chat: EntityChat){
        withContext(Dispatchers.IO){

            daoChat?.insertChat(chat)
        }
    }
}