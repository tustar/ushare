package com.tustar.ushare.ui.lot

import com.tustar.ushare.R
import com.tustar.ushare.data.entry.Message
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.exception.ExceptionHandler
import com.tustar.ushare.data.exception.StatusCode
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Logger

class LotPresenter(private val view: LotContract.View,
                   private val repo: UserRepository) : LotContract.Presenter {

    private var page = 1

    init {
        view.presenter = this
    }

    override fun getUsers() {
        addSubscription(disposable = repo.userList(page, CommonDefine.PAGE_SIZE)
                .subscribe({
                    when (it.code) {
                        Response.OK -> {
                            view.updateUsers(it.data)
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