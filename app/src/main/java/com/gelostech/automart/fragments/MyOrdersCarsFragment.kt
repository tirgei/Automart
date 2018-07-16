package com.gelostech.automart.fragments


import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automart.R
import com.gelostech.automart.adapters.BookingsAdapter
import com.gelostech.automart.commoners.BaseFragment
import com.gelostech.automart.models.Booking
import com.gelostech.automart.utils.RecyclerFormatter
import com.kizitonwose.time.days
import kotlinx.android.synthetic.main.fragment_my_bookings.view.*

class MyOrdersCarsFragment : BaseFragment() {
    private lateinit var bookingsAdapter: BookingsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_bookings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(v: View) {
        v.rv.setHasFixedSize(true)
        v.rv.layoutManager = LinearLayoutManager(activity)
        v.rv.itemAnimator = DefaultItemAnimator()
        v.rv.addItemDecoration(RecyclerFormatter.SimpleDividerItemDecoration(activity!!))

        bookingsAdapter = BookingsAdapter(activity!!)
        v.rv.adapter = bookingsAdapter
        v.rv.showShimmerAdapter()

        Handler().postDelayed({
            v.rv.hideShimmerAdapter()
            loadSample()
        }, 2500)
    }

    private fun loadSample() {
        val booking1 = Booking()
        booking1.holderImage = R.drawable.benz
        booking1.bookerName = "Vincent Tirgei"
        booking1.sellerName = "Jomic Autos"
        booking1.date = System.currentTimeMillis() - 10.days.inMilliseconds.longValue
        booking1.name = "Mercedes Benz E250"
        bookingsAdapter.addBooking(booking1)
    }

}
