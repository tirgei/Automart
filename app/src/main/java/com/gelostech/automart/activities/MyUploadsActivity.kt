package com.gelostech.automart.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.gelostech.automart.R
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.fragments.MyUploadsCarsFragment
import com.gelostech.automart.fragments.MyUploadsPartsFragment
import com.gelostech.automart.utils.PagerAdapter
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.activity_my_uploads.*

class MyUploadsActivity : BaseActivity(), TabLayout.OnTabSelectedListener {

    companion object {
        private const val CARS = "CARS"
        private const val PARTS = "PARTS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_uploads)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Uploads"

        setupViewPager()
        setupTabs()
        initFab()

    }

    private fun setupViewPager() {
        val adapter = PagerAdapter(supportFragmentManager, this)
        val cars = MyUploadsCarsFragment()
        val parts = MyUploadsPartsFragment()

        adapter.addAllFrags(cars, parts)
        adapter.addAllTitles(CARS, PARTS)

        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 1
        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

    }

    private fun setupTabs() {
        tabs.setupWithViewPager(viewpager)
        tabs.addOnTabSelectedListener(this)
    }

    private fun initFab() {
        fabCar.colorNormal = ContextCompat.getColor(this, R.color.colorPrimary)
        fabCar.colorPressed = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fabCar.colorRipple = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fabCar.setImageDrawable(IconicsDrawable(this).icon(Ionicons.Icon.ion_plus).color(Color.WHITE).sizeDp(17))

        fabPart.colorNormal = ContextCompat.getColor(this, R.color.colorPrimary)
        fabPart.colorPressed = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fabPart.colorRipple = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fabPart.setImageDrawable(IconicsDrawable(this).icon(Ionicons.Icon.ion_plus).color(Color.WHITE).sizeDp(17))

        fabCar.setOnClickListener {
            fam.close(true)
            startActivity(Intent(this, AddCarActivity::class.java))
            AppUtils.animateEnterRight(this)
        }

        fabPart.setOnClickListener {
            fam.close(true)
            startActivity(Intent(this, AddPartActivity::class.java))
            AppUtils.animateEnterRight(this)
        }
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
