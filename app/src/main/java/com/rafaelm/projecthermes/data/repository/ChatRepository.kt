package com.rafaelm.projecthermes.data.repository

import android.app.Application
import com.rafaelm.projecthermes.data.dao.DAOChat
import com.rafaelm.projecthermes.data.database.DatabaseChat
import com.rafaelm.projecthermes.data.entity.EntityChat
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

    fun insetChat(chat: EntityChat){
        launch {
            insert(chat)
        }
    }

    private suspend fun insert(chat: EntityChat){
        withContext(Dispatchers.IO){

            daoChat?.insertChat(chat)
        }
    }
}