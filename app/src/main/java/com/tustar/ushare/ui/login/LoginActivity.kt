package com.tustar.ushare.ui.login

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.tustar.ushare.R
import com.tustar.ushare.base.BaseActivity
import com.tustar.ushare.data.Injection
import com.tustar.ushare.databinding.ActivityLoginBinding
import com.tustar.ushare.util.*
import org.jetbrains.anko.toast
import java.lang.ref.WeakReference


class LoginActivity : BaseActivity() {

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
    }

    override fun initViews() {
        setTitle(R.string.login_title)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
