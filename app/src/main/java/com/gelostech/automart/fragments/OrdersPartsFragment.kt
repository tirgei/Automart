package com.gelostech.automart.fragments


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automart.R
import com.gelostech.automart.adapters.PartOrdersAdapter
import com.gelostech.automart.models.PartOrder
import com.gelostech.automart.utils.RecyclerFormatter
import kotlinx.android.synthetic.main.fragment_parts_orders.view.*

class OrdersPartsFragment : Fragment() {
    private lateinit var partOrdersAdapter: PartOrdersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parts_orders, container, false)
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

        partOrdersAdapter = PartOrdersAdapter(activity!!)
        v.rv.adapter = partOrdersAdapter
        v.rv.showShimmerAdapter()

        Handler().postDelayed({
            v.rv.hideShimmerAdapter()
            loadSample()
        }, 2500)
    }

    private fun loadSample() {
        val order1 = PartOrder()
        order1.holderImage = R.drawable.brake
        order1.buyerName = "James Mackenzie"
        order1.sellerName = "Ngigi Parts"
        order1.name = "Brembo brakes"
        order1.description = "2 Brembo brakes"
        partOrdersAdapter.addPartOrder(order1)

    }


}
