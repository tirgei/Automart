package com.gelostech.automart.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.MenuItem
import com.gelostech.automart.R
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.fragments.MyOrdersCarsFragment
import com.gelostech.automart.fragments.MyOrdersPartsFragment
import com.gelostech.automart.utils.PagerAdapter
import kotlinx.android.synthetic.main.activity_my_orders.*

class MyOrdersActivity : BaseActivity(), TabLayout.OnTabSelectedListener {

    companion object {
        private const val BOOKINGS = "TEST DRIVES"
        private const val PARTS = "SPARE PARTS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Orders"

        setupViewPager()
        setupTabs()
    }

    private fun setupViewPager() {
        val adapter = PagerAdapter(supportFragmentManager, this)
        val bookings = MyOrdersCarsFragment()
        val parts = MyOrdersPartsFragment()

        adapter.addAllFrags(bookings, parts)
        adapter.addAllTitles(BOOKINGS, PARTS)

        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 1
        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

    }

    private fun setupTabs() {
        tabs.setupWithViewPager(viewpager)
        tabs.addOnTabSelectedListener(this)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewpager.setCurrentItem(tab!!.position, true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils.animateEnterLeft(this)
    }
}
