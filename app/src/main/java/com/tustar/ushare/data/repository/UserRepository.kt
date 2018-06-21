package com.tustar.ushare.data.repository

import com.tustar.ushare.UShareApplication
import com.tustar.ushare.data.entry.Code
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.data.local.UserLocalDataSource
import com.tustar.ushare.data.remote.UserRemoteDataSource
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.NetUtils
import com.tustar.ushare.util.Preference
import com.tustar.ushare.util.scheduler.SchedulerUtils
import io.reactivex.Observable


class UserRepository(val remote: UserRemoteDataSource,
                     val local: UserLocalDataSource) {

    /**
     * 获取验证码
     */
    fun code(mobile: String): Observable<Response<Code, Any>> {
        return remote.code(mobile).compose(SchedulerUtils.ioToMain())
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
