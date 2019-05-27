package com.tustar.ushare.data.repository

import com.tustar.ushare.data.entry.Captcha
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.data.local.UserLocalDataSource
import com.tustar.ushare.data.remote.UserRemoteDataSource
import kotlinx.coroutines.Deferred


class UserRepository(val remote: UserRemoteDataSource,
                     val local: UserLocalDataSource) {

    /**
     * 获取验证码
     */
    fun captcha(mobile: String): Deferred<Response<Captcha, Any>> {
        return remote.captcha(mobile)
    }

    /**
     * 登录
     */
    fun login(mobile: String, captcha: String): Deferred<Response<User, Any>> {
        return remote.login(mobile, captcha)
    }

    /**
     * 更新权重
     */
    fun updateWeight(weight:Int): Deferred<Response<User, Any>> {
        return remote.weight(weight)
    }

    fun userList(page: Int, pageSize: Int): Deferred<Response<MutableList<User>, Any>> {
        return remote.userList(page, pageSize)
    }

    /**
     * 更新权重
     */
    fun updateNick(nick: String): Deferred<Response<User, Any>> {
        return remote.nick(nick)
    }

    /**
     * 获取用户信息
     */
    fun getUser(): Deferred<Response<User, Any>> {
        return remote.info()
    }

    companion object {

        private var INSTANCE: UserRepository? = null

        @JvmStatic
        fun getInstance(remote: UserRemoteDataSource,
                        local: UserLocalDataSource) =
                INSTANCE ?: synchronized(UserRepository::class.java) {
                    INSTANCE ?: UserRepository(remote, local)
                            .also { INSTANCE = it }
                }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
