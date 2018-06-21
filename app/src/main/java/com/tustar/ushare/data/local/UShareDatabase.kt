package com.tustar.ushare.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.tustar.ushare.data.entry.Topic
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.data.local.dao.TopicDao
import com.tustar.ushare.data.local.dao.UserDao

@Database(entities = [(User::class), (Topic::class)], version = 1)
@TypeConverters(value = [Converters::class])
abstract class UShareDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun topicDao(): TopicDao

    companion object {
        private var INSTANCE: UShareDatabase? = null
        private val lock = Any()
        fun getInstance(context: Context): UShareDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            UShareDatabase::class.java, "UShare.db")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}