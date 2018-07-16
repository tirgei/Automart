package com.gelostech.automart.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.gelostech.automart.R
import com.gelostech.automart.adapters.DetailsAdapter
import com.gelostech.automart.adapters.FeaturesAdapter
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Car
import com.gelostech.automart.utils.RecyclerFormatter
import com.gelostech.automart.utils.setDrawable
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.ionicons_typeface_library.Ionicons
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_car.*
import timber.log.Timber

class CarActivity : BaseActivity(), ImageListener, View.OnClickListener {
    val images = arrayOf(R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six)
    private lateinit var car: Car
    private lateinit var featuresAdapter: FeaturesAdapter
    private lateinit var detailsAdapter: DetailsAdapter

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
        setupFeatures()
        setupDetails()

        carousel.pageCount = images.size
        carousel.setImageListener(this)
        contactSeller.setOnClickListener(this)

        sellerName.setDrawable(AppUtils.setDrawable(this, FontAwesome.Icon.faw_user2, R.color.secondaryText, 15))
        sellerPhone.setDrawable(AppUtils.setDrawable(this, Ionicons.Icon.ion_android_call, R.color.secondaryText, 15))
        sellerLocation.setDrawable(AppUtils.setDrawable(this, Ionicons.Icon.ion_location, R.color.secondaryText, 15))
        sellerEmail.setDrawable(AppUtils.setDrawable(this, Ionicons.Icon.ion_email, R.color.secondaryText, 15))
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

    private fun setupDetails() {
        detailsRv.setHasFixedSize(true)
        detailsRv.layoutManager = LinearLayoutManager(this)
        detailsRv.addItemDecoration(RecyclerFormatter.SimpleDividerItemDecoration(this))

        detailsAdapter = DetailsAdapter(this)
        detailsRv.adapter = detailsAdapter

        var details = mutableMapOf<String, String>()
        details["Body Type"] = "Hatchback"
        details["CC"] = "2000cc"
        details["Transmission"] = "Manual"
        details["Fuel Type"] = "Petrol"
        details["Drive"] = "RWD"
        details["Mileage"] = "123000KM"


        detailsAdapter.addDetails(details)
    }

    private fun setupFeatures() {
        featuresRv.setHasFixedSize(true)
        featuresRv.layoutManager = LinearLayoutManager(this)

        featuresAdapter = FeaturesAdapter(this)
        featuresRv.adapter = featuresAdapter

        val features = mutableListOf("Sunroof", "Turbo charged", "Leather interior", "Alloy rims", "Electric Mirrors")
        featuresAdapter.addFeatures(features)

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

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.contactSeller -> {
                val i = Intent(this, ChatActivity::class.java)
                i.putExtra(K.CHAT_ID, car.sellerId)
                i.putExtra(K.CHAT_NAME, car.sellerName)
                startActivity(i)
                AppUtils.animateFadein(this)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils.animateEnterLeft(this)
    }



}
