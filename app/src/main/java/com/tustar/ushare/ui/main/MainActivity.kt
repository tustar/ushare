package com.tustar.ushare.ui.main

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.tustar.ushare.R
import com.tustar.ushare.UShareApplication
import com.tustar.ushare.base.BaseActivity
import com.tustar.ushare.data.Injection
import com.tustar.ushare.ui.HomeActivity
import com.tustar.ushare.ui.lot.LotFragment
import com.tustar.ushare.ui.lot.LotPresenter
import com.tustar.ushare.ui.mine.MineFragment
import com.tustar.ushare.ui.mine.MinePresenter
import com.tustar.ushare.ui.topic.TopicFragment
import com.tustar.ushare.ui.topic.TopicPresenter
import com.tustar.ushare.util.CommonDefine
import com.tustar.ushare.util.Logger
import com.tustar.ushare.util.Preference
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.util.*


class MainActivity : BaseActivity(), MainContract.View {

    override lateinit var presenter: MainContract.Presenter

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var lottieView: LottieAnimationView
    private lateinit var lotFragment: LotFragment
    private lateinit var adapter: TabPagerAdapter
    private var tabItems = mutableMapOf<Int, TabItem>()
    private val listener = View.OnClickListener {
        val tab = tabLayout.getTabAt(it.tag as Int)
        tab?.let {
            if (!it.isSelected) {
                it.select()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar()
        setDarkStatusIcon(false)
        setStatusBarColor(R.color.action_bar_bg_color)
        presenter = MainPresenter(this, Injection.provideUserRepository(applicationContext))

        initViews()

        presenter.onLogin()
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

    override fun initViews() {
        super.initViews()
        setTitle(R.string.app_name)

        initViewPager()

        initTabLayout()

        initAnimationView()
    }

    private fun initViewPager() {
        viewPager = find(R.id.view_pager)
        adapter = TabPagerAdapter(supportFragmentManager)
        //
        lotFragment = LotFragment.newInstance()
        adapter.addFragment(lotFragment)
        LotPresenter(lotFragment, Injection.provideUserRepository(applicationContext))
        //
        val topicFragment = TopicFragment.newInstance()
        adapter.addFragment(topicFragment)
        TopicPresenter(topicFragment, Injection.provideTopicRepository())
        //
        val mineFragment = MineFragment.newInstance()
        adapter.addFragment(mineFragment)
        MinePresenter(mineFragment, Injection.provideUserRepository(applicationContext))
        viewPager.adapter = adapter
    }

    private fun initTabLayout() {
        tabLayout = find(R.id.tab_layout)
        tabLayout.setupWithViewPager(viewPager, true)

        for (i in 0..tabLayout.tabCount) {
            var tab = tabLayout.getTabAt(i)
            tab?.customView = getTabItemView(i)
            tab?.customView?.let {
                val tabView = it.parent as View
                tabView.tag = i
                tabView.setOnClickListener(listener)
            }
        }
    }

    private fun initAnimationView() {
        lottieView = find(R.id.lottie_view)
        lottieView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                Logger.d("updateWeight")
                lottieView.visibility = View.GONE
                val random = Random(System.currentTimeMillis())
                presenter.updateWeight(random.nextInt(100))
                updateLotUI()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_option, menu)
        return true
    }

    override fun onBackPressed() {
        forcedExitOnBackPressed()
    }

    override fun toLoginUI() {
        val intent = Intent(this, HomeActivity::class.java).apply {

        }
        startActivity(intent)
    }

    override fun updateLotUI() {
        lotFragment.presenter.getUsers()
    }

    override fun showToast(resId: Int) {
        toast(resId)
    }

    private fun showShakeUI() {
        var token: String by Preference(UShareApplication.context,
                CommonDefine.HEAD_ACCESS_TOKEN, "")
        if (token.isNullOrEmpty()) {
            toLoginUI()
            return
        }

        lottieView.visibility = View.VISIBLE
        lottieView.playAnimation()
    }

    private fun getTabItemView(position: Int): View {
        val itemView = layoutInflater.inflate(R.layout.item_tab, null)
        tabItems[position] = TabItem(itemView, position)
        itemView.tag = position
        itemView.setOnClickListener(listener)
        return itemView
    }

    inner class TabItem(itemView: View, position: Int) {

        lateinit var icon: ImageView
        lateinit var text: TextView

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
