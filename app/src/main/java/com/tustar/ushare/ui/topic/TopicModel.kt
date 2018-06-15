package com.tustar.ushare.ui.topic

import android.content.Context
import com.tustar.ushare.data.bean.HttpResult
import com.tustar.ushare.data.bean.Topic
import com.tustar.ushare.net.RetrofitManager
import com.tustar.ushare.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

class TopicModel {
    fun topicList(context: Context, page: Int, pageSize: Int): Observable<HttpResult<MutableList<Topic>, Any>> {
        var params = mutableMapOf<String, Any>()
        params["page"] = page
        params["page_size"] = pageSize

        return RetrofitManager.service
                .topicList().compose(SchedulerUtils.ioToMain())
    }
}
