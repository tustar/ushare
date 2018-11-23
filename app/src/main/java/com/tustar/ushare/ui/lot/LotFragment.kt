package com.tustar.ushare.ui.lot

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Logger
import com.tustar.ushare.util.Preference
import org.jetbrains.anko.find

class LotFragment : Fragment(), LotContract.View, LotAdapter.OnItemClickListener {

    override lateinit var presenter: LotContract.Presenter

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LotAdapter
    private var users = arrayListOf<User>()
    private var oldToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lot, container,
                false)
        initRecycleView(view)
        presenter.getUsers()

        return view
    }

    override fun onResume() {
        super.onResume()
        var token: String by Preference(UShareApplication.context,
                CommonDefine.HEAD_ACCESS_TOKEN, "")
        if (oldToken != token) {
            presenter.getUsers()
            oldToken = token
        }
    }

    private fun initRecycleView(view: View) {
        recyclerView = view.find(R.id.lot_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = LotAdapter(users)
        adapter.setOnItemClickListener(this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(view: View, position: Int) {
        // TODO
    }

    override fun showToast(resId: Int) {
//        toast(resId)
    }

    override fun updateUsers(users: MutableList<User>) {
        Logger.d("topics = $users")
        this.users.clear()
        this.users.addAll(users)
        adapter.users = users
        adapter.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        presenter?.detachView()
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                LotFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
