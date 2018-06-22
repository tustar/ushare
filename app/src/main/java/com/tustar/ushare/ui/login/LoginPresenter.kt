package com.tustar.ushare.ui.login

import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication
import com.tustar.ushare.data.entry.Message
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.exception.ExceptionHandler
import com.tustar.ushare.data.exception.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.util.*

class LoginPresenter(private val view: LoginContract.View,
                     private val repo: UserRepository) : LoginContract.Presenter {

    init {
        view.presenter = this
    }


    override fun login(mobile: String, captcha: String) {
        Logger.d("mobile = $mobile, captcha = $captcha")
        if (!MobileUtils.isMobileOk(mobile)) {
            view.showToast(R.string.login_phone_err)
            return
        }

        if (!CodeUtils.isCodeOk(captcha)) {
            view.showToast(R.string.login_captcha_err)
            return
        }

        view.setSubmitEnable(false)
        addSubscription(disposable = repo.login(mobile, captcha).subscribe({
            view.setSubmitEnable(true)
            when (it.code) {
                Response.OK -> {
                    var token: String by Preference(UShareApplication.context,
                            CommonDefine.HEAD_ACCESS_TOKEN, "")
                    token = it.data.token
                    var mobile: String by Preference(UShareApplication.context,
                            CommonDefine.PREF_KEY_USER_MOBILE, "")
                    mobile = it.data.mobile
                    var nick: String by Preference(UShareApplication.context,
                            CommonDefine.PREF_KEY_USER_NICK, "")
                    nick = it.data.nick
                    view.toMainUI()
                }
                Response.FAILURE -> {
                    when (it.message) {
                        Message.Unauthorized -> Logger.d("Sign Error")
                        else -> view.showToast(R.string.login_submit_err)
                    }
                }
                else -> {
                    // 更多情况
                }
            }
        }) {
            //
            view.setSubmitEnable(true)
            val code = ExceptionHandler.handleException(it)
            when (code) {
                StatusCode.SOCKET_TIMEOUT_ERROR -> view.showToast(R.string.socket_timeout_error)
                StatusCode.CONNECT_ERROR -> view.showToast(R.string.connect_error)
                StatusCode.UNKNOWN_HOST_ERROR -> view.showToast(R.string.unkown_host_error)
                StatusCode.SERVER_ERROR -> view.showToast(R.string.server_err)
                else -> view.showToast(R.string.login_submit_err)
            }
        })
    }

    override fun sendCaptcha(mobile: String) {

        if (!MobileUtils.isMobileOk(mobile)) {
            view.showToast(R.string.login_phone_err)
            return
        }

        view.setCaptchaGetEnable(false)
        addSubscription(disposable = repo.captcha(mobile).subscribe({
            view.startCaptchaTimer()
            when (it.code) {
                Response.OK -> {
                    view.showToast(R.string.login_captcha_get_success)
                    view.showCaptcha(it.data.code)
                }
                Response.FAILURE -> {
                    when (it.message) {
                        Message.Unauthorized -> Logger.d("Sign Error")
                        else -> {
                        }
                    }
                }
                else -> {

                }
            }
        }) {
            view.setCaptchaGetEnable(true)
            val code = ExceptionHandler.handleException(it)
            when (code) {
                StatusCode.SOCKET_TIMEOUT_ERROR -> view.showToast(R.string.socket_timeout_error)
                StatusCode.CONNECT_ERROR -> view.showToast(R.string.connect_error)
                StatusCode.UNKNOWN_HOST_ERROR -> view.showToast(R.string.unkown_host_error)
                StatusCode.SERVER_ERROR -> view.showToast(R.string.server_err)
                else -> view.showToast(R.string.login_captcha_get_err)
            }
        })
    }
}