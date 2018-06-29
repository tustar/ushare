package com.tustar.ushare.ui.login

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.tustar.ushare.R
import com.tustar.ushare.base.BaseActivity
import com.tustar.ushare.databinding.ActivityLoginBinding
import com.tustar.ushare.util.obtainViewModel


class LoginActivity : BaseActivity(), LoginNavigator {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = obtainViewModel(LoginViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.vm = viewModel
        setActionBar()
        setDarkStatusIcon(false)
        setStatusBarColor(R.color.action_bar_bg_color)
        initViews()

        subscribeToNavigationChanges(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.detachView()
    }

    override fun initViews() {
        setTitle(R.string.login_title)
    }

    override fun toMainUI() {
        finish()
    }

    private fun subscribeToNavigationChanges(viewModel: LoginViewModel) {
        val activity = this@LoginActivity
        viewModel.run {
            toMainCommand.observe(activity,
                    Observer { activity.toMainUI() })
        }
    }
}
