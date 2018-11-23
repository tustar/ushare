package com.tustar.ushare.ui

import android.os.Bundle
import androidx.navigation.findNavController
import com.tustar.ushare.R
import com.tustar.ushare.base.BaseActivity


class HomeActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    override fun onSupportNavigateUp()
            = findNavController(R.id.nav_host_fragment).navigateUp()
}
