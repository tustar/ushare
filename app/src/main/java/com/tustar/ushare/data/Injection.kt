package com.tustar.ushare.data

import android.content.Context
import com.tustar.ushare.data.local.UShareDatabase
import com.tustar.ushare.data.local.UserLocalDataSource
import com.tustar.ushare.data.remote.UserRemoteDataSource
import com.tustar.ushare.data.repository.TopicRepository
import com.tustar.ushare.data.repository.UserRepository

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val database = UShareDatabase.getInstance(context)
        return UserRepository.getInstance(
                UserRemoteDataSource.getInstance(),
                UserLocalDataSource.getInstance(database.userDao()))
    }

    fun provideTopicRepository(): TopicRepository {
        return TopicRepository()
    }
}