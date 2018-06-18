package com.tustar.ushare.ui.login

import com.tustar.ushare.UShareApplication
import com.tustar.ushare.util.DeviceUtils
import com.tustar.ushare.data.bean.Code
import com.tustar.ushare.data.bean.HttpResult
import com.tustar.ushare.data.bean.User
import com.tustar.ushare.net.RetrofitManager
import com.tustar.ushare.rx.scheduler.SchedulerUtils
import com.tustar.ushare.util.NetUtils
import io.reactivex.Observable


class LoginModel {

    /**
     * 获取验证码
     */
    fun code( mobile: String): Observable<HttpResult<Code, Any>> {
        var params = mutableMapOf<String, String>()
        params["mobile"] = mobile
        params["deviceId"] = DeviceUtils.getDeviceId(UShareApplication.context) ?: ""
        params = NetUtils.getSignedParams(UShareApplication.context, params)

        return RetrofitManager.service
                .sendCode(params).compose(SchedulerUtils.ioToMain())
    }

    /**
     * 登录
     */
    fun login(mobile: String, captcha: String): Observable<HttpResult<User, Any>> {
        var params = mutableMapOf<String, String>()
        params["mobile"] = mobile
        params["captcha"] = captcha
        params = NetUtils.getSignedParams(UShareApplication.context, params)

        return RetrofitManager.service
                .login(params).compose(SchedulerUtils.ioToMain())
    }
}
