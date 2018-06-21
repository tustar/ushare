package com.tustar.ushare.data.remote

import android.support.annotation.VisibleForTesting
import com.tustar.ushare.UShareApplication
import com.tustar.ushare.data.entry.Code
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.DeviceUtils
import com.tustar.ushare.util.NetUtils
import com.tustar.ushare.util.Preference
import io.reactivex.Observable

class UserRemoteDataSource private constructor() {

    /**
     * 获取验证码
     */
    fun code(mobile: String): Observable<Response<Code, Any>> {
        var params = mutableMapOf<String, String>()
        params["mobile"] = mobile
        params["deviceId"] = DeviceUtils.getDeviceId(UShareApplication.context) ?: ""
        params = NetUtils.getSignedParams(params)

        return RetrofitManager.service.sendCode(params)
    }

    /**
     * 登录
     */
    fun login(mobile: String, captcha: String): Observable<Response<User, Any>> {
        var params = mutableMapOf<String, String>()
        params["mobile"] = mobile
        params["captcha"] = captcha
        params = NetUtils.getSignedParams(params)

        return RetrofitManager.service.login(params)
    }

    /**
     * 登录
     */
    fun weight(weight: Int): Observable<Response<User, Any>> {
        var params = mutableMapOf<String, String>()
        var mobile:String by Preference(UShareApplication.context,
                CommonDefine.PREF_KEY_USER_MOBILE, "")
        var token: String by Preference(UShareApplication.context,
                CommonDefine.HEAD_ACCESS_TOKEN, "")
        params["mobile"] = mobile
        params[CommonDefine.HEAD_ACCESS_TOKEN] = token
        params = NetUtils.getSignedParams(params)
        params["weight"] = weight.toString()
        params = NetUtils.getSignedParams(params)

        return RetrofitManager.service.weight(params)
    }

    fun userList(page: Int, pageSize: Int): Observable<Response<MutableList<User>, Any>> {
        var params = mutableMapOf<String, Any>()
        params["page"] = page
        params["page_size"] = pageSize

        return RetrofitManager.service.userList()
    }


    companion object {
        private var INSTANCE: UserRemoteDataSource? = null

        @JvmStatic
        fun getInstance(): UserRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(UserRemoteDataSource::javaClass) {
                    INSTANCE = UserRemoteDataSource()
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