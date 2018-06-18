package com.tustar.ushare.ui.login

import com.tustar.ushare.base.BasePresenter
import com.tustar.ushare.base.BaseView

interface LoginContract {

    interface View : BaseView<Presenter> {
        fun showToast(resId: Int)
        fun showCaptcha(captcha: String)
        fun setSubmitEnable(enable: Boolean)
        fun setCaptchaGetEnable(enable: Boolean)
        fun startCaptchaTimer()
        fun toMainUI()
    }

    interface Presenter : BasePresenter {
        fun login(mobile: String, captcha: String)
        fun sendCaptcha(mobile: String)
    }
}