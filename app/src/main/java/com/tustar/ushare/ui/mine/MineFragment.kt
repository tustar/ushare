package com.tustar.ushare.ui.mine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.tustar.ushare.R
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.ui.login.LoginActivity
import com.tustar.ushare.util.NoFastClickListener
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.toast


class MineFragment : Fragment(), MineContract.View {

    override lateinit var presenter: MineContract.Presenter
    private lateinit var nickEditText: EditText
    private lateinit var mobileText: TextView
    private lateinit var weightText: TextView
    private lateinit var updateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mine, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        with(view) {
            nickEditText = find(R.id.mine_nick_text)
            mobileText = find(R.id.mine_mobile_text)
            weightText = find(R.id.mine_weight_text)
            updateBtn = find(R.id.mine_submit)
        }
        updateBtn.setOnClickListener(object : NoFastClickListener() {
            override fun onNoFastClick(v: View) {
                presenter.updateNick(nickEditText.text.toString().trim())
            }
        })
    }

    override fun updateUserUI(user: User) {
        user?.let {
            nickEditText.setText(user.nick)
            mobileText.text = user.mobile
            weightText.text = user.weight.toString()
        }
    }

    override fun showToast(resId: Int) {
        toast(resId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        presenter.onLogin()
    }

    override fun onDetach() {
        super.onDetach()
        presenter?.detachView()
    }

    override fun toLoginUI() {
        val intent = Intent(activity, LoginActivity::class.java).apply {

        }
        startActivity(intent)
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
