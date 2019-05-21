package com.tustar.ushare.ui.topic

import com.tustar.ushare.R
import com.tustar.ushare.data.entry.execute
import com.tustar.ushare.data.helper.Message
import com.tustar.ushare.data.helper.StatusCode
import com.tustar.ushare.data.repository.TopicRepository
import com.tustar.ushare.util.CommonDefine


class TopicPresenter(private val view: TopicContract.View,
                     private val repo: TopicRepository) : TopicContract.Presenter {
    private var page = 1

    init {
        view.presenter = this
    }

    override fun getTopics() {
        addSubscription(disposable = repo.topicList(page, CommonDefine.PAGE_SIZE)
                .subscribe({
                    it.execute(
                            ok = { topics ->
                                view.updateTopics(topics)
                            },
                            failure = { message ->
                                Message.handleFailure(message) {

                                }
                            })
                }) {
                    StatusCode.handleException(it) {
                        R.string.login_captcha_get_err
                    }
                })
    }
}