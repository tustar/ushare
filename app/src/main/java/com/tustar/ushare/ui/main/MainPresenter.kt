package com.tustar.ushare.ui.main

import com.tustar.ushare.UShareApplication
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Preference


class MainPresenter(var view: MainContract.View) : MainContract.Presenter {

    private var token: String by Preference(UShareApplication.context,
            CommonDefine.HEAD_ACCESS_TOKEN, "")

    init {
        view.presenter = this
    }

    private val model by lazy {
        MainModel()
    }

    override fun onLogin() {
        if (token.isNullOrEmpty()) {
            view.toLoginUI()
        }
    }
}