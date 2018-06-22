package com.tustar.ushare.ui.mine

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


class MinePresenter(private val view: MineContract.View,
                    private val repo: UserRepository) : MineContract.Presenter {

    init {
        view.presenter = this
    }

    override fun onLogin() {
        var token: String by Preference(UShareApplication.context,
                CommonDefine.HEAD_ACCESS_TOKEN, "")
        if (token.isNullOrEmpty()) {
            view.toLoginUI()
        } else {
//            getUserInfo()
        }
    }

    override fun getUserInfo() {
        addSubscription(disposable = repo.getUser().subscribe({
            when (it.code) {
                Response.OK -> {
                    view.showToast(R.string.mine_user_success)
                    view.updateUserUI(it.data)
                }
                Response.FAILURE -> {
                    when (it.message) {
                        Message.Unauthorized -> Logger.d("Sign Error")
                        else -> view.showToast(R.string.mine_user_err)
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
                else -> view.showToast(R.string.mine_user_err)
            }
        })
    }

    override fun updateNick(nick: String) {

        addSubscription(disposable = repo.updateNick(nick).subscribe({
            when (it.code) {
                Response.OK -> {
                    view.showToast(R.string.mine_nick_success)
                    view.updateUserUI(it.data)
                }
                Response.FAILURE -> {
                    when (it.message) {
                        Message.Unauthorized -> Logger.d("Sign Error")
                        else -> view.showToast(R.string.mine_nick_err)
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
                else -> view.showToast(R.string.mine_nick_err)
            }
        })

    }
}