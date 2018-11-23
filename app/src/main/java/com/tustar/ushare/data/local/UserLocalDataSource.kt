package com.tustar.ushare.data.local

import androidx.annotation.VisibleForTesting
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.data.local.dao.UserDao
import io.reactivex.Flowable

class UserLocalDataSource private constructor(
        val dao: UserDao
) {

    fun getAll(): Flowable<List<User>> {
        return dao.getAll()
    }

    fun insertUsers(users: Array<out User>) {
        return dao.insertUsers(*users)
    }

    fun updateUsers(users: Array<out User>) {
        return dao.updateUsers(*users)
    }

    fun deleteUsers(users: Array<out User>) {
        return dao.deleteUsers(*users)
    }

    companion object {
        private var INSTANCE: UserLocalDataSource? = null

        @JvmStatic
        fun getInstance(dao: UserDao): UserLocalDataSource {
            if (INSTANCE == null) {
                synchronized(UserLocalDataSource::javaClass) {
                    INSTANCE = UserLocalDataSource(dao)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}