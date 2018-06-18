package com.tustar.ushare.ui.topic

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tustar.ushare.util.Logger
import com.tustar.ushare.R
import com.tustar.ushare.data.bean.Topic
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.toast


class TopicFragment : Fragment(), TopicContract.View, TopicAdapter.OnItemClickListener {

    override lateinit var presenter: TopicContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TopicAdapter
    private var topics = arrayListOf<Topic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.ushare_fragment_topic, container, false)

        initRecycleView(view)
        presenter.getTopics()

        return view
    }

    private fun initRecycleView(view: View) {
        recyclerView = view.find(R.id.topic_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TopicAdapter(topics)
        adapter.setOnItemClickListener(this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(view: View, position: Int) {
        // TODO
    }

    override fun showToast(resId: Int) {
        toast(resId)
    }

    override fun updateTopics(topics: MutableList<Topic>) {
        Logger.d("topics = $topics")
        this.topics.clear()
        this.topics.addAll(topics)
        adapter.topics = topics
        adapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        presenter = TopicPresenter(this)
    }

    override fun onDetach() {
        super.onDetach()
        presenter?.detachView()
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                TopicFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
