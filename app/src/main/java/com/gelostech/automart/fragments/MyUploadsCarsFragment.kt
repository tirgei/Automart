package com.gelostech.automart.fragments


import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automart.R
import com.gelostech.automart.adapters.CarsAdapter
import com.gelostech.automart.callbacks.CarCallback
import com.gelostech.automart.commoners.BaseFragment
import com.gelostech.automart.models.Car
import com.gelostech.automart.utils.RecyclerFormatter
import kotlinx.android.synthetic.main.fragment_my_cars.view.*

class MyUploadsCarsFragment : BaseFragment(), CarCallback {
    private lateinit var carsAdapter: CarsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_cars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(v: View) {
        v.rv.setHasFixedSize(true)
        v.rv.layoutManager = LinearLayoutManager(activity!!)
        v.rv.itemAnimator = DefaultItemAnimator()
        v.rv.addItemDecoration(RecyclerFormatter.DoubleDividerItemDecoration(activity!!))

        carsAdapter = CarsAdapter(activity!!, this)
        v.rv.adapter = carsAdapter
        v.rv.showShimmerAdapter()

        Handler().postDelayed({
            v.rv.hideShimmerAdapter()
            loadSample()
        }, 2500)
    }

    private fun loadSample() {

        val car3 = Car()
        car3.make = "Toyota"
        car3.model = "Hilux Surf"
        car3.price = 11250000.toString()
        car3.holderAvatar = R.drawable.person
        car3.holderImage = R.drawable.hilux
        car3.year = 1997.toString()
        car3.transmission = "Manual"
        car3.mileage = 189000.toString()
        car3.location = "Mombasa rd"
        car3.sellerName = "Skyline Motors"
        car3.time = System.currentTimeMillis()
        carsAdapter.addCar(car3)

        val car4 = Car()
        car4.make = "Honda"
        car4.model = "Fit"
        car4.price = 68000.toString()
        car4.holderAvatar = R.drawable.person
        car4.holderImage = R.drawable.fit
        car4.year = 2011.toString()
        car4.transmission = "Automatic"
        car4.mileage = 75000.toString()
        car4.location = "Outering road"
        car4.sellerName = "Skyline Motors"
        car4.time = System.currentTimeMillis()
        carsAdapter.addCar(car4)

        val car5 = Car()
        car5.make = "Toyota"
        car5.model = "Bb"
        car5.price = 875000.toString()
        car5.holderAvatar = R.drawable.person
        car5.holderImage = R.drawable.bb
        car5.year = 2009.toString()
        car5.transmission = "Automatic"
        car5.mileage = 68000.toString()
        car5.location = "Ngong road"
        car5.sellerName = "Agba Auto"
        car5.time = System.currentTimeMillis()
        carsAdapter.addCar(car5)

    }

    override fun onClick(v: View, car: Car) {

    }
}
