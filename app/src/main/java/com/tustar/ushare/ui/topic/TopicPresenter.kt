package com.tustar.ushare.ui.topic

import com.tustar.ushare.R
import com.tustar.ushare.data.entry.Message
import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.exception.ExceptionHandler
import com.tustar.ushare.data.exception.StatusCode
import com.tustar.ushare.data.repository.TopicRepository
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Logger


class TopicPresenter(private val view: TopicContract.View,
                     private val repo: TopicRepository) : TopicContract.Presenter {
    private var page = 1

    init {
        view.presenter = this
    }

    override fun getTopics() {
        addSubscription(disposable = repo.topicList(page, CommonDefine.PAGE_SIZE)
                .subscribe({
                    when (it.code) {
                        Response.OK -> {
                            view.updateTopics(it.data)
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