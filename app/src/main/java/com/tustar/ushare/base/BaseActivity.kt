package com.tustar.ushare.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import com.tustar.ushare.R
import com.tustar.ushare.extension.setupActionBar
import com.tustar.ushare.util.ActivityCollector
import com.tustar.ushare.util.Logger
import org.jetbrains.anko.find
import org.jetbrains.anko.toast


open class BaseActivity : AppCompatActivity() {

    open var actionBarBack: ImageButton? = null
    open var actionBarTitle: TextView? = null
    private var firstPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

    open fun setActionBar() {
        setupActionBar(R.id.action_bar) {
            actionBarTitle = find(R.id.action_bar_title)
            actionBarBack = find(R.id.action_bar_back)
            actionBarBack?.setOnClickListener {
                finish()
            }
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onTitleChanged(title: CharSequence, color: Int) {
        super.onTitleChanged(title, color)
        actionBarTitle?.text = title
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun forcedExitOnBackPressed() {
        Logger.d()
        if (System.currentTimeMillis() - firstPressedTime < ON_BACK_EXIT_DURATION) {
            Logger.d("Exit app")
            ActivityCollector.finishAll()
        } else {
            toast(R.string.back_again_exit)
            firstPressedTime = System.currentTimeMillis()
        }
    }

    companion object {
        private const val ON_BACK_EXIT_DURATION = 2000
    }
}