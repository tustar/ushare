package com.tustar.ushare.ui.topic

import com.tustar.ushare.base.BasePresenter
import com.tustar.ushare.base.BaseView
import com.tustar.ushare.data.entry.Topic

interface TopicContract {
    interface View : BaseView<Presenter> {
        fun showToast(resId: Int)
        fun updateTopics(topics: MutableList<Topic>)
    }

    interface Presenter : BasePresenter {
        fun getTopics()
    }
}