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
            loadSample()
        }, 2500)
    }

    private fun loadSample() {
        val car5 = Car()
        car5.make = "Toyota"
        car5.model = "Bb"
        car5.price = 875000
        car5.holderAvatar = R.drawable.person
        car5.holderImage = R.drawable.bb
        car5.year = 2009
        car5.transmission = "Automatic"
        car5.mileage = 68000
        car5.location = "Ngong road"
        car5.sellerName = "Agba Auto"
        car5.time = System.currentTimeMillis()
        carsAdapter.addCar(car5)

        val car6 = Car()
        car6.make = "Toyota"
        car6.model = "Premio"
        car6.price = 45000
        car6.holderAvatar = R.drawable.person
        car6.holderImage = R.drawable.premio
        car6.year = 1995
        car6.transmission = "Automatic"
        car6.mileage = 320000
        car6.location = "Kariobangi"
        car6.sellerName = "Martin Kamau"
        car6.time = System.currentTimeMillis()
        carsAdapter.addCar(car6)

        val car7 = Car()
        car7.make = "Mercedes Benz"
        car7.model = "E250"
        car7.price = 3200000
        car7.holderAvatar = R.drawable.person
        car7.holderImage = R.drawable.benz
        car7.year = 2012
        car7.transmission = "Automatic"
        car7.mileage = 43000
        car7.location = "Industrial Area"
        car7.sellerName = "DT Dobie"
        car7.time = System.currentTimeMillis()
        carsAdapter.addCar(car7)

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
