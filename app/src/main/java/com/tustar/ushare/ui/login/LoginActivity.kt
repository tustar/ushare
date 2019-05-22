package com.tustar.ushare.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import com.tustar.action.RxBus
import com.tustar.ushare.R
import com.tustar.ushare.base.BaseActivity
import com.tustar.ushare.extension.setDarkStatusIcon
import com.tustar.ushare.extension.setStatusBarColor
import com.tustar.ushare.rxbus.EventCode
import com.tustar.ushare.util.CommonDefine


class LoginActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setDarkStatusIcon(false)
        setStatusBarColor(R.color.action_bar_bg_color)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.getStringExtra(CommonDefine.EXTRA_VCODE)?.let {
            RxBus.get().post(EventCode(it))
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()
}
