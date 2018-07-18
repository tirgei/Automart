package com.gelostech.automart.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.gelostech.automart.R
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Part
import com.gelostech.automart.utils.setDrawable
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.activity_part.*

class PartActivity : AppCompatActivity() {
    private lateinit var part: Part

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_part)

        part = intent.getSerializableExtra(K.PART) as Part

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = part.name

        seller_icon.setImageDrawable(AppUtils.setDrawable(this, FontAwesome.Icon.faw_user2, R.color.secondaryText, 15))
        location_icon.setImageDrawable(AppUtils.setDrawable(this, Ionicons.Icon.ion_location, R.color.secondaryText, 15))
        category_icon.setImageDrawable(AppUtils.setDrawable(this, FontAwesome.Icon.faw_list, R.color.secondaryText, 15))

        image.setImageResource(intent.getIntExtra(K.PART_IMAGE, R.drawable.brake))
        seller.text = "Jomic Auto parts"
        location.text = "Grogon"
        category.text = "Suspension"
        qty.text = "13"
        unit.text = "${part.price}"
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
