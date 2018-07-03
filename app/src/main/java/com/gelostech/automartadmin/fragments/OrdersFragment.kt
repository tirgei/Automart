package com.gelostech.automartadmin.fragments


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automartadmin.R
import com.gelostech.automartadmin.adapters.BookingsAdapter
import com.gelostech.automartadmin.commoners.BaseFragment
import com.gelostech.automartadmin.models.Booking
import com.kizitonwose.time.TimeUnit
import com.kizitonwose.time.days
import kotlinx.android.synthetic.main.fragment_orders.view.*

class OrdersFragment : BaseFragment() {
    private lateinit var bookingsAdapter: BookingsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(v: View) {
        v.rv.setHasFixedSize(true)
        v.rv.layoutManager = LinearLayoutManager(activity)
        v.rv.itemAnimator = DefaultItemAnimator()

        bookingsAdapter = BookingsAdapter()
        v.rv.adapter = bookingsAdapter
        v.rv.showShimmerAdapter()

        Handler().postDelayed({
            v.rv.hideShimmerAdapter()
            loadSample()
        }, 2500)
    }

    private fun loadSample() {
        val booking1 = Booking()
        booking1.holderImage = R.drawable.fozzy
        booking1.bookerName = "Vincent Tirgei"
        booking1.date = System.currentTimeMillis() - 10.days.inMilliseconds.longValue
        booking1.name = "Subaru Forester"
        bookingsAdapter.addBooking(booking1)

        val booking2 = Booking()
        booking2.holderImage = R.drawable.fozzy
        booking2.bookerName = "Vincent Tirgei"
        booking2.date = System.currentTimeMillis() - 10.days.inMilliseconds.longValue
        booking2.name = "Subaru Forester"
        bookingsAdapter.addBooking(booking2)

        val booking3 = Booking()
        booking3.holderImage = R.drawable.fozzy
        booking3.bookerName = "Vincent Tirgei"
        booking3.date = System.currentTimeMillis() - 10.days.inMilliseconds.longValue
        booking3.name = "Subaru Forester"
        bookingsAdapter.addBooking(booking3)

        val booking4 = Booking()
        booking4.holderImage = R.drawable.fozzy
        booking4.bookerName = "Vincent Tirgei"
        booking4.date = System.currentTimeMillis() - 10.days.inMilliseconds.longValue
        booking4.name = "Subaru Forester"
        bookingsAdapter.addBooking(booking4)

        val booking5 = Booking()
        booking5.holderImage = R.drawable.fozzy
        booking5.bookerName = "Vincent Tirgei"
        booking5.date = System.currentTimeMillis() - 10.days.inMilliseconds.longValue
        booking5.name = "Subaru Forester"
        bookingsAdapter.addBooking(booking5)

        val booking6 = Booking()
        booking6.holderImage = R.drawable.fozzy
        booking6.bookerName = "Vincent Tirgei"
        booking6.date = System.currentTimeMillis() - 10.days.inMilliseconds.longValue
        booking6.name = "Subaru Forester"
        bookingsAdapter.addBooking(booking6)

        val booking7 = Booking()
        booking7.holderImage = R.drawable.fozzy
        booking7.bookerName = "Vincent Tirgei"
        booking7.date = System.currentTimeMillis() - 10.days.inMilliseconds.longValue
        booking7.name = "Subaru Forester"
        bookingsAdapter.addBooking(booking7)

        val booking8 = Booking()
        booking8.holderImage = R.drawable.fozzy
        booking8.bookerName = "Vincent Tirgei"
        booking8.date = System.currentTimeMillis() - 10.days.inMilliseconds.longValue
        booking8.name = "Subaru Forester"
        bookingsAdapter.addBooking(booking8)

    }


}
