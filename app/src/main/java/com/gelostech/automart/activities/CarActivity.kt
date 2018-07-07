package com.gelostech.automart.activities

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gelostech.automart.R
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.AppUtils.setDrawable
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.commoners.K
import com.gelostech.automart.databinding.ItemFeaturesBinding
import com.gelostech.automart.models.Car
import com.gelostech.automart.utils.inflate
import com.gelostech.automart.utils.setDrawable
import com.mikepenz.ionicons_typeface_library.Ionicons
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_car.*
import kotlinx.android.synthetic.main.item_features.view.*

class CarActivity : BaseActivity(), ImageListener {
    val images = arrayOf(R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six)
    private lateinit var car: Car
    private lateinit var featuresAdapter: FeaturesAdapter

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

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils.animateEnterLeft(this)
    }

    class FeaturesAdapter(val context: Context) : RecyclerView.Adapter<FeaturesAdapter.FeaturesHolder>() {
        val features = mutableListOf<String>()

        fun addFeatures(features: MutableList<String>) {
            features.addAll(features)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesHolder {
            return FeaturesHolder(parent.inflate(R.layout.item_features), context)
        }

        override fun getItemCount(): Int = features.size

        override fun onBindViewHolder(holder: FeaturesHolder, position: Int) {
            holder.bind(features[position])
        }

        class FeaturesHolder(private val binding: ItemFeaturesBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

            init {
                binding.tv.setDrawable(setDrawable(context, Ionicons.Icon.ion_android_checkmark_circle, R.color.colorAccent, 18))
            }

            fun bind(feature: String) {

                binding.feature = feature
            }

        }

    }

}
