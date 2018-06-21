package com.tustar.ushare.ui.main

import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication
import com.tustar.ushare.data.entry.Message
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.exception.ExceptionHandler
import com.tustar.ushare.data.exception.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Logger
import com.tustar.ushare.util.Preference


class MainPresenter(private val view: MainContract.View,
                    private val repo: UserRepository) : MainContract.Presenter {

    private var token: String by Preference(UShareApplication.context,
            CommonDefine.HEAD_ACCESS_TOKEN, "")

    init {
        view.presenter = this
    }


    override fun onLogin() {
        if (token.isNullOrEmpty()) {
            view.toLoginUI()
        }
    }

    override fun updateWeight(weight: Int) {
        addSubscription(disposable = repo.updateWeight(weight).subscribe({
            when (it.code) {
                Response.OK -> {
                    view.showToast(R.string.lot_weight_success)
                    view.updateLotUI()
                }
                Response.FAILURE -> {
                    when (it.message) {
                        Message.Unauthorized -> Logger.d("Sign Error")
                        else -> view.showToast(R.string.lot_weight_err)
                    }
                }
                else -> {
                    // 更多情况
                }
            }
        }) {
            //
            val code = ExceptionHandler.handleException(it)
            when (code) {
                StatusCode.SOCKET_TIMEOUT_ERROR -> view.showToast(R.string.socket_timeout_error)
                StatusCode.CONNECT_ERROR -> view.showToast(R.string.connect_error)
                StatusCode.UNKNOWN_HOST_ERROR -> view.showToast(R.string.unkown_host_error)
                StatusCode.SERVER_ERROR -> view.showToast(R.string.server_err)
                else -> view.showToast(R.string.lot_weight_err)
            }
        })
    }
}