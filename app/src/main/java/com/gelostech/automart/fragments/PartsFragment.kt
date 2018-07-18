package com.gelostech.automart.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automart.R
import com.gelostech.automart.activities.PartActivity
import com.gelostech.automart.adapters.PartsAdapter
import com.gelostech.automart.callbacks.PartCallback
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.BaseFragment
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Part
import com.gelostech.automart.utils.RecyclerFormatter
import kotlinx.android.synthetic.main.fragment_parts.view.*


class PartsFragment : BaseFragment(), PartCallback {
    private lateinit var partsAdapter: PartsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(v:View) {
        v.rv.setHasFixedSize(true)
        v.rv.layoutManager = GridLayoutManager(activity, 2)
        v.rv.addItemDecoration(RecyclerFormatter.GridItemDecoration(activity!!, 2, 10))
        v.rv.itemAnimator = DefaultItemAnimator()

        partsAdapter = PartsAdapter(this)
        v.rv.adapter = partsAdapter

        loadSample()
    }

    private fun loadSample() {
        val part1 = Part(null, null, "sf", "Wakamau", 53000, null, R.drawable.brake)
        part1.name = "Land Rover brakes"
        partsAdapter.addParts(part1)

        val part2 = Part(null, null, "sf", "Wakamau", 7500, null, R.drawable.hl)
        part2.name = "Headlights"
        partsAdapter.addParts(part2)

        val part3 = Part(null, null, "sf", "Wakamau", 12000, null, R.drawable.bl)
        part3.name = "Brake Lights"
        partsAdapter.addParts(part3)

        val part4 = Part(null, null, "sf", "Wakamau", 32000, null, R.drawable.windscreen)
        part4.name = "Windscreen"
        partsAdapter.addParts(part4)

        val part5 = Part(null, null, "sf", "Wakamau", 27000, null, R.drawable.door)
        part5.name = "Rear door"
        partsAdapter.addParts(part5)
    }

    override fun onClick(v: View, part: Part) {
        val i = Intent(activity, PartActivity::class.java)
        i.putExtra(K.PART, part)
        i.putExtra(K.PART_IMAGE, part.holderImage)
        startActivity(i)
        AppUtils.animateFadein(activity!!)

    }
}
