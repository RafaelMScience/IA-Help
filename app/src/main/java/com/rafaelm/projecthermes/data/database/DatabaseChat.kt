package com.rafaelm.projecthermes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rafaelm.projecthermes.data.dao.DAOChat
import com.rafaelm.projecthermes.data.entity.EntityChat
import com.rafaelm.projecthermes.data.entity.EntityUser

@Database(entities = [EntityChat::class], version = 1, exportSchema = false)
abstract class DatabaseChat:RoomDatabase() {
    abstract val databaseChat: DAOChat

    companion object{
        @Volatile
        private var INSTANCE: DatabaseChat? = null

        fun getInstance(context: Context): DatabaseChat{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseChat::class.java,
                        "chat_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }

    }
}