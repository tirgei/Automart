package com.gelostech.automart.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.gelostech.automart.R
import com.gelostech.automart.adapters.CarsAdapter
import com.gelostech.automart.callbacks.CarCallback
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.models.Car
import com.gelostech.automart.utils.RecyclerFormatter
import com.gelostech.automart.utils.showView
import kotlinx.android.synthetic.main.activity_watchlist.*

class WatchlistActivity : AppCompatActivity(), CarCallback {
    private lateinit var carsAdapter: CarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watchlist)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Watchlist"

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.itemAnimator = DefaultItemAnimator()
        rv.addItemDecoration(RecyclerFormatter.DoubleDividerItemDecoration(this))

        carsAdapter = CarsAdapter(this, this)
        rv.adapter = carsAdapter
        rv.showShimmerAdapter()

        Handler().postDelayed({
            rv.hideShimmerAdapter()
            empty.showView()
        }, 2500)
    }

    override fun onClick(v: View, car: Car) {

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
