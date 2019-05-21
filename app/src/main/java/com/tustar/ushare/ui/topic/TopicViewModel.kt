package com.tustar.ushare.ui.topic

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.tustar.ushare.base.BaseViewModel
import com.tustar.ushare.data.repository.TopicRepository
import com.tustar.ushare.vmf.TopicViewModelFactory

class TopicViewModel(private val repo:TopicRepository):BaseViewModel(){

    companion object {

        fun get(fragment: Fragment): TopicViewModel {
            val application = checkApplication(checkActivity(fragment))
            return ViewModelProviders.of(fragment, TopicViewModelFactory.getInstance(application))
                    .get(TopicViewModel::class.java)
        }
    }
}