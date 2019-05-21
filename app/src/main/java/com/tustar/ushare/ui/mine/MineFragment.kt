package com.tustar.ushare.ui.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tustar.ushare.R
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.ktx.clicks
import com.tustar.ushare.ui.HomeActivity
import com.uber.autodispose.android.lifecycle.autoDisposable
import kotlinx.android.synthetic.main.fragment_mine.*
import org.jetbrains.anko.support.v4.toast


class MineFragment : Fragment() {

    private lateinit var viewModel: MineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MineViewModel.get(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        initObservers()
    }

    private fun initViews() {
        mine_submit.clicks()
                .autoDisposable(this)
                .subscribe {
                    updateNick()
                }
    }

    private fun initObservers() {
        viewModel.user.observe(this, Observer {
            updateUserUI(it)
        })
        viewModel.toLoginUIEvent.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                toLoginUI()
            }
        })
    }

    private fun updateNick() {
        val nick = mine_nick_text.text.toString()
        if (nick.isNullOrEmpty()) {
            toast(R.string.mine_nick_error)
            return
        }

        viewModel.updateNick(nick)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onLogin()
    }

    private fun toLoginUI() {
        val intent = Intent(activity, HomeActivity::class.java).apply {
        }
        startActivity(intent)
    }

    private fun updateUserUI(user: User) {
        mine_nick_text.setText(user.nick)
        mine_mobile_text.text = user.mobile
        mine_weight_text.text = user.weight.toString()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                MineFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
