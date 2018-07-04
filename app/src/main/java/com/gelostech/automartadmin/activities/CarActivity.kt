package com.gelostech.automartadmin.activities

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.view.MenuItem
import android.widget.ImageView
import com.gelostech.automartadmin.R
import com.gelostech.automartadmin.commoners.AppUtils
import com.gelostech.automartadmin.commoners.K
import com.gelostech.automartadmin.models.Car
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_car.*

class CarActivity : AppCompatActivity(), ImageListener {
    val images = arrayOf(R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six)
    private lateinit var car: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)

        car = intent.getSerializableExtra(K.CAR) as Car

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null
        toolbarTitle()

        carousel.pageCount = images.size
        carousel.setImageListener(this)

    }

    private fun toolbarTitle() {
        toolbarLayout.title = ""
        appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener{
            var showTitle = true
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBar.totalScrollRange
                }

                if (scrollRange + verticalOffset == 0) {
                    toolbarLayout.setCollapsedTitleTextColor(Color.WHITE)
                    toolbarLayout.title = "${car.make} ${car.model}"
                    showTitle = true
                } else if (showTitle) {
                    toolbarLayout.title = ""
                    showTitle = false

                }
            }
        })
    }

    override fun setImageForPosition(position: Int, imageView: ImageView?) {
        imageView!!.scaleType =ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(images[position])
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
