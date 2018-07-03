package com.gelostech.automartadmin.activities

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.gelostech.automartadmin.R
import com.gelostech.automartadmin.commoners.AppUtils.setDrawable
import com.gelostech.automartadmin.commoners.BaseActivity
import com.gelostech.automartadmin.fragments.ChatFragment
import com.gelostech.automartadmin.fragments.HomeFragment
import com.gelostech.automartadmin.fragments.NotificationsFragment
import com.gelostech.automartadmin.fragments.OrdersFragment
import com.gelostech.automartadmin.utils.PagerAdapter
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity(), AHBottomNavigation.OnTabSelectedListener,
        AHBottomNavigation.OnNavigationPositionListener, ViewPager.OnPageChangeListener {

    private var doubleBackToExit = false

    private lateinit var homeFragment: HomeFragment
    private lateinit var ordersFragment: OrdersFragment
    private lateinit var notificationsFragment: NotificationsFragment
    private lateinit var chatFragment: ChatFragment

    companion object {
        private const val HOME = "Automart"
        private const val ORDERS = "Bookings"
        private const val NOTIFICATIONS = "Notifications"
        private const val CHATS = "Chats"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment = HomeFragment()
        ordersFragment = OrdersFragment()
        notificationsFragment = NotificationsFragment()
        chatFragment = ChatFragment()

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = HOME
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        setupBottomNav()
        setupViewPager()
    }

    //Setup the bottom navigation bar
    private fun setupBottomNav() {
        val homeIcon = setDrawable(this, Ionicons.Icon.ion_ios_home, R.color.secondaryText, 18)
        val ordersIcon = setDrawable(this, FontAwesome.Icon.faw_inbox, R.color.secondaryText, 18)
        val notificationIcon = setDrawable(this, Ionicons.Icon.ion_ios_bell, R.color.secondaryText, 18)
        val chatIcon = setDrawable(this, Ionicons.Icon.ion_chatbubbles, R.color.secondaryText, 18)

        bottomNav.addItem(AHBottomNavigationItem(HOME, homeIcon))
        bottomNav.addItem(AHBottomNavigationItem(ORDERS, ordersIcon))
        bottomNav.addItem(AHBottomNavigationItem(NOTIFICATIONS, notificationIcon))
        bottomNav.addItem(AHBottomNavigationItem(CHATS, chatIcon))

        bottomNav.defaultBackgroundColor = Color.WHITE
        bottomNav.inactiveColor = ContextCompat.getColor(this, R.color.inactiveColor)
        bottomNav.accentColor = ContextCompat.getColor(this, R.color.colorPrimary)
        bottomNav.isBehaviorTranslationEnabled = false
        bottomNav.titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
        bottomNav.setUseElevation(true, 5f)

        bottomNav.setOnTabSelectedListener(this)
        bottomNav.setOnNavigationPositionListener(this)
    }

    //Setup the view pager
    private fun setupViewPager() {
        val adapter = PagerAdapter(supportFragmentManager, this)

        adapter.addAllFrags(homeFragment, ordersFragment, notificationsFragment, chatFragment)
        adapter.addAllTitles(HOME, ORDERS, NOTIFICATIONS, CHATS)

        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(this)
        viewPager.offscreenPageLimit = 3
    }

    override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        viewPager.setCurrentItem(position, true)

        when(position) {
            0 -> supportActionBar?.title = HOME
            1 -> supportActionBar?.title = ORDERS
            2 -> supportActionBar?.title = NOTIFICATIONS
            3 -> supportActionBar?.title = CHATS
        }

        return true
    }

    override fun onPositionChange(y: Int) {
        viewPager.setCurrentItem(y, true)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        bottomNav.currentItem = position
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.onBackPressed()
        } else {
            doubleBackToExit = true
            toast("Tap back again to exit")

            Handler().postDelayed({doubleBackToExit = false}, 1500)
        }
    }

}
