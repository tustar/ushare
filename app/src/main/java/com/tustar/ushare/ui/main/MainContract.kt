package com.tustar.ushare.ui.main

import com.tustar.ushare.base.BasePresenter
import com.tustar.ushare.base.BaseView

interface MainContract {
    interface View : BaseView<Presenter> {
        fun toLoginUI()
    }

    interface Presenter : BasePresenter {
        fun onLogin()
    }
}