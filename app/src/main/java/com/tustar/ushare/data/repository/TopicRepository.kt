package com.tustar.ushare.data.repository

import com.tustar.ushare.data.entry.Response
import com.tustar.ushare.data.entry.Topic
import com.tustar.ushare.data.remote.RetrofitManager
import com.tustar.ushare.util.scheduler.SchedulerUtils
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.Deferred

class TopicRepository {
    fun topicList(page: Int, pageSize: Int): Deferred<Response<MutableList<Topic>, Any>> {
        var params = mutableMapOf<String, Any>()
        params["page"] = page
        params["page_size"] = pageSize

        return RetrofitManager.service.topicList()
    }
}
