package com.tustar.ushare.data.repository

import com.tustar.ushare.data.entry.Captcha
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.data.local.UserLocalDataSource
import com.tustar.ushare.data.remote.UserRemoteDataSource
import com.tustar.ushare.util.scheduler.SchedulerUtils
import io.reactivex.Observable


class UserRepository(val remote: UserRemoteDataSource,
                     val local: UserLocalDataSource) {

    /**
     * 获取验证码
     */
    fun captcha(mobile: String): Observable<Response<Captcha, Any>> {
        return remote.captcha(mobile).compose(SchedulerUtils.ioToMain())
    }

    /**
     * 登录
     */
    fun login(mobile: String, captcha: String): Observable<Response<User, Any>> {
        return remote.login(mobile, captcha).compose(SchedulerUtils.ioToMain())
    }

    /**
     * 更新权重
     */
    fun updateWeight(weight:Int): Observable<Response<User, Any>> {
        return remote.weight(weight).compose(SchedulerUtils.ioToMain())
    }

    fun userList(page: Int, pageSize: Int): Observable<Response<MutableList<User>, Any>> {
        return remote.userList(page, pageSize).compose(SchedulerUtils.ioToMain())
    }

    /**
     * 更新权重
     */
    fun updateNick(nick: String): Observable<Response<User, Any>> {
        return remote.nick(nick).compose(SchedulerUtils.ioToMain())
    }

    /**
     * 获取用户信息
     */
    fun getUser(): Observable<Response<User, Any>> {
        return remote.info().compose(SchedulerUtils.ioToMain())
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
