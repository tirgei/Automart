package com.gelostech.automart.fragments


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automart.R
import com.gelostech.automart.activities.CarActivity
import com.gelostech.automart.activities.ChatActivity
import com.gelostech.automart.adapters.CarsAdapter
import com.gelostech.automart.callbacks.CarCallback
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.BaseFragment
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Car
import com.gelostech.automart.utils.RecyclerFormatter
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : BaseFragment(), CarCallback {
    private lateinit var carsAdapter: CarsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(v: View) {
        v.rv.setHasFixedSize(true)
        v.rv.layoutManager = LinearLayoutManager(activity)
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
        val car1 = Car()
        car1.make = "Subaru"
        car1.model = "Forester"
        car1.price = 2400000.toString()
        car1.holderAvatar = R.drawable.person
        car1.holderImage = R.drawable.forester
        car1.year = 2010.toString()
        car1.transmission = "Manual"
        car1.mileage = 123000.toString()
        car1.location = "Mombasa rd"
        car1.sellerName = "Skyline Motors"
        car1.time = System.currentTimeMillis()
        carsAdapter.addCar(car1)

        val car2 = Car()
        car2.make = "Toyota"
        car2.model = "NZE"
        car2.price = 350000.toString()
        car2.holderAvatar = R.drawable.person
        car2.holderImage = R.drawable.nze
        car2.year = 2004.toString()
        car2.transmission = "Automatic"
        car2.mileage = 215000.toString()
        car2.location = "Mombasa rd"
        car2.sellerName = "Skyline Motors"
        car2.time = System.currentTimeMillis()
        carsAdapter.addCar(car2)

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

        val car6 = Car()
        car6.make = "Toyota"
        car6.model = "Premio"
        car6.price = 45000.toString()
        car6.holderAvatar = R.drawable.person
        car6.holderImage = R.drawable.premio
        car6.year = 1995.toString()
        car6.transmission = "Automatic"
        car6.mileage = 320000.toString()
        car6.location = "Kariobangi"
        car6.sellerName = "Martin Kamau"
        car6.time = System.currentTimeMillis()
        carsAdapter.addCar(car6)

        val car7 = Car()
        car7.make = "Mercedes Benz"
        car7.model = "E250"
        car7.price = 3200000.toString()
        car7.holderAvatar = R.drawable.person
        car7.holderImage = R.drawable.benz
        car7.year = 2012.toString()
        car7.transmission = "Automatic"
        car7.mileage = 43000.toString()
        car7.location = "Industrial Area"
        car7.sellerName = "DT Dobie"
        car7.time = System.currentTimeMillis()
        carsAdapter.addCar(car7)

    }

    override fun onClick(v: View, car: Car) {
        when(v.id) {
            R.id.action, R.id.root -> {
                val i = Intent(activity, CarActivity::class.java)
                i.putExtra(K.CAR, car)
                startActivity(i)
                AppUtils.animateFadein(activity!!)
            }

            R.id.contact -> {
                val i = Intent(activity, ChatActivity::class.java)
                i.putExtra(K.CHAT_ID, car.sellerId)
                i.putExtra(K.CHAT_NAME, car.sellerName)
                activity!!.startActivity(i)
                AppUtils.animateFadein(activity!!)
            }
        }
    }
}
