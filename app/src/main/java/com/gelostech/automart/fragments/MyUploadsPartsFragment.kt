package com.gelostech.automart.fragments


import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automart.R
import com.gelostech.automart.adapters.PartsAdapter
import com.gelostech.automart.callbacks.PartCallback
import com.gelostech.automart.commoners.BaseFragment
import com.gelostech.automart.models.Part
import com.gelostech.automart.utils.RecyclerFormatter
import kotlinx.android.synthetic.main.fragment_my_parts.view.*

class MyUploadsPartsFragment : BaseFragment(), PartCallback {
    private lateinit var partsAdapter: PartsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_parts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(v: View) {
        v.rv.setHasFixedSize(true)
        v.rv.layoutManager = GridLayoutManager(activity!!, 2)
        v.rv.itemAnimator = DefaultItemAnimator()
        v.rv.addItemDecoration(RecyclerFormatter.GridItemDecoration(activity!!, 2, 10))

        partsAdapter = PartsAdapter(this)
        v.rv.adapter = partsAdapter
        v.rv.showShimmerAdapter()

        Handler().postDelayed({
            v.rv.hideShimmerAdapter()
            loadSample()
        }, 2500)
    }

    private fun loadSample() {
        val part2 = Part(null, null, "sf", "Wakamau", 7500, null, R.drawable.hl)
        part2.name = "Headlights"
        partsAdapter.addParts(part2)

        val part3 = Part(null, null, "sf", "Wakamau", 12000, null, R.drawable.bl)
        part3.name = "Brake Lights"
        partsAdapter.addParts(part3)

    }

    override fun onClick(v: View, part: Part) {

    }
}
