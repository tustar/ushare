package com.tustar.ushare.ui.main

import android.animation.Animator
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.tustar.action.RxBus
import com.tustar.ushare.R
import com.tustar.ushare.base.BaseActivity
import com.tustar.ushare.data.entry.User
import com.tustar.ushare.extension.setDarkStatusIcon
import com.tustar.ushare.extension.setStatusBarColor
import com.tustar.ushare.extension.toLoginUI
import com.tustar.ushare.rxbus.EventLot
import com.tustar.ushare.ui.lot.LotFragment
import com.tustar.ushare.ui.mine.MineFragment
import com.tustar.ushare.ui.topic.TopicFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import java.util.*


class MainActivity : BaseActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var tabPagerAdapter: TabPagerAdapter

    private var tabItems = mutableMapOf<Int, TabItem>()
    private val listener = View.OnClickListener { view ->
        val tab = main_tab_layout.getTabAt(view.tag as Int)
        tab?.let { tab ->
            if (!tab.isSelected) {
                tab.select()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //
        viewModel = MainViewModel.get(this)
        //
        setActionBar()
        setDarkStatusIcon(false)
        setStatusBarColor(R.color.action_bar_bg_color)
        //
        setTitle(R.string.app_name)
        initViews()
        initObservers()
    }

    override fun onResume() {
        super.onResume()

        User.isTokenActive(
                active = {
                    //
                },
                inactive = {
                    toLoginUI()
                })
    }

    override fun setActionBar() {
        super.setActionBar()
        actionBarBack?.visibility = View.GONE
        val toolbar = find(R.id.action_bar) as Toolbar
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_shake -> showShakeUI()
            }
            return@setOnMenuItemClickListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_option, menu)
        return true
    }

    override fun onBackPressed() {
        forcedExitOnBackPressed()
    }

    private fun initViews() {

        initViewPager()

        initTabLayout()

        initAnimationView()
    }

    private fun initViewPager() {
        val lotFragment = LotFragment.newInstance()
        val topicFragment = TopicFragment.newInstance()
        val mineFragment = MineFragment.newInstance()
        tabPagerAdapter = TabPagerAdapter(supportFragmentManager).apply {
            addFragment(lotFragment)
            addFragment(topicFragment)
            addFragment(mineFragment)
        }
        main_view_pager.adapter = tabPagerAdapter
    }

    private fun initTabLayout() {
        main_tab_layout.setupWithViewPager(main_view_pager, true)
        for (i in 0..main_tab_layout.tabCount) {
            var tab = main_tab_layout.getTabAt(i)
            tab?.customView = getTabItemView(i)
            tab?.customView?.let {
                val tabView = it.parent as View
                tabView.tag = i
                tabView.setOnClickListener(listener)
            }
        }
    }

    private fun initAnimationView() {
        main_lottie_view.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                updateWeight()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
    }

    private fun initObservers() {
        viewModel.updateLotUIEvent.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                updateLotUI()
            }
        })
    }

    private fun updateWeight() {
        main_lottie_view.visibility = View.GONE
        val random = Random(System.currentTimeMillis())
        viewModel.updateWeight(random.nextInt(100))
    }

    private fun updateLotUI() {
        RxBus.get().post(EventLot())
    }

    private fun showShakeUI() {
        User.isTokenActive(
                active = {
                    main_lottie_view.visibility = View.VISIBLE
                    main_lottie_view.playAnimation()
                },
                inactive = {
                    toLoginUI()
                })
    }

    private fun getTabItemView(position: Int): View {
        val itemView = layoutInflater.inflate(R.layout.item_tab, null)
        tabItems[position] = TabItem(itemView, position)
        itemView.tag = position
        itemView.setOnClickListener(listener)
        return itemView
    }

    inner class TabItem(itemView: View, position: Int) {

        private var icon: ImageView
        private var text: TextView

        init {
            with(itemView) {
                icon = find(R.id.tab_bar_item_image)
                text = find(R.id.tab_bar_item_text)
            }

            when (position) {
                TAB_LOT -> {
                    icon.setImageResource(R.drawable.selector_ic_lot)
                    text.text = getString(R.string.tab_lot)
                }
                TAB_TOPIC -> {
                    icon.setImageResource(R.drawable.selector_ic_topic)
                    text.text = getString(R.string.tab_topic)
                }
                TAB_MINE -> {
                    icon.setImageResource(R.drawable.selector_ic_mine)
                    text.text = getString(R.string.tab_mine)
                }
            }
        }
    }

    companion object {
        const val TAB_LOT = 0
        const val TAB_TOPIC = 1
        const val TAB_MINE = 2
    }
}
