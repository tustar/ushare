package com.tustar.ushare.ui.topic

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tustar.ushare.R
import com.tustar.ushare.data.entry.Topic
import org.jetbrains.anko.find


class TopicAdapter(var topics: MutableList<Topic>) : RecyclerView.Adapter<TopicAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_topic, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return topics.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTopic(topics[position])
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }


    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        private lateinit var userId: TextView
        private lateinit var title: TextView
        private lateinit var desc: TextView

        init {
            itemView?.apply {
                userId = find(R.id.topic_user_id_text)
                title = find(R.id.topic_title_text)
                desc = find(R.id.topic_desc_text)
                setOnClickListener {
                    itemClickListener?.onItemClick(it, adapterPosition)
                }
            }
        }

        fun bindTopic(topic: Topic) {
            userId.text = topic.user_id.toString()
            title.text = topic.title
            desc.text = topic.description
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}