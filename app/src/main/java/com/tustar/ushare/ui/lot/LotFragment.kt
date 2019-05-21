package com.tustar.ushare.ui.lot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tustar.action.RxBus
import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.rxbus.EventLot
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Logger
import com.tustar.ushare.util.Preference
import com.uber.autodispose.android.lifecycle.autoDisposable
import org.jetbrains.anko.find

class LotFragment : Fragment(), LotAdapter.OnItemClickListener {

    private lateinit var viewModel: LotViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LotAdapter
    private var users = arrayListOf<User>()
    private var oldToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = LotViewModel.get(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lot, container,
                false)
        initRecycleView(view)
        viewModel.getUsers()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
    }

    override fun onResume() {
        super.onResume()
        var token: String by Preference(UShareApplication.context,
                CommonDefine.HEAD_ACCESS_TOKEN, "")
        if (oldToken != token) {
            viewModel.getUsers()
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

    private fun initObservers() {

        viewModel.users.observe(this, Observer {
            updateUsers(it)
        })

        RxBus.get().toObservable(EventLot::class.java)
                .autoDisposable(this)
                .subscribe {
                    viewModel.getUsers()
                }
    }

    private fun updateUsers(users: MutableList<User>) {
        Logger.d("users = $users")
        this.users.clear()
        this.users.addAll(users)
        adapter.users = users
        adapter.notifyDataSetChanged()
    }

    companion object {

        @JvmStatic
        fun newInstance() = LotFragment()
    }
}
