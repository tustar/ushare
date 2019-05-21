package com.tustar.ushare.vmf

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tustar.ushare.data.Injection
import com.tustar.ushare.data.entry.Topic
import com.tustar.ushare.data.repository.TopicRepository
import com.tustar.ushare.data.repository.UserRepository
import com.tustar.ushare.ui.login.LoginViewModel
import com.tustar.ushare.ui.lot.LotViewModel
import com.tustar.ushare.ui.mine.MineViewModel
import com.tustar.ushare.ui.topic.TopicViewModel

class TopicViewModelFactory(private val repo: TopicRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(TopicViewModel::class.java) -> {
                        TopicViewModel(repo)
                    }
                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {
        @Volatile
        private var INSTANCE: TopicViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE
                        ?: synchronized(TopicViewModelFactory::class.java) {
                            INSTANCE
                                    ?: TopicViewModelFactory(
                                            Injection.provideTopicRepository())
                                            .also { INSTANCE = it }
                        }

    }
}