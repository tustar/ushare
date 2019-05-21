package com.tustar.ushare.ui.topic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tustar.ushare.R
import com.tustar.ushare.data.entry.Topic
import kotlinx.android.synthetic.main.fragment_topic.*


class TopicFragment : Fragment(), TopicAdapter.OnItemClickListener {

    private lateinit var topicAdapter: TopicAdapter
    private var topics = arrayListOf<Topic>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        topicAdapter = TopicAdapter(topics)
        topicAdapter.setOnItemClickListener(this)
        topic_recycle_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapter
        }
    }

    override fun onItemClick(view: View, position: Int) {
        // TODO
    }

    companion object {
        @JvmStatic
        fun newInstance() = TopicFragment()
    }
}
