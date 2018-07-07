package com.gelostech.automart.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gelostech.automart.R
import com.gelostech.automart.adapters.PartsAdapter
import com.gelostech.automart.commoners.BaseFragment
import com.gelostech.automart.models.Part
import com.gelostech.automart.utils.RecyclerFormatter
import kotlinx.android.synthetic.main.fragment_parts.view.*


class PartsFragment : BaseFragment() {
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
        v.rv.addItemDecoration(RecyclerFormatter.GridItemDecoration(activity!!, R.dimen.small_padding))
        v.rv.itemAnimator = DefaultItemAnimator()

        partsAdapter = PartsAdapter()
        v.rv.adapter = partsAdapter

        loadSample()
    }

    private fun loadSample() {
        val part1 = Part(null, null, "sf", "Wakamau", 53000, null, R.drawable.four)
        part1.name = "Brembo brakes"
        partsAdapter.addParts(part1)

        val part2 = Part(null, null, "sf", "Wakamau", 53000, null, R.drawable.four)
        part2.name = "Brembo brakes"
        partsAdapter.addParts(part2)

        val part3 = Part(null, null, "sf", "Wakamau", 53000, null, R.drawable.four)
        part3.name = "Brembo brakes"
        partsAdapter.addParts(part3)

        val part4 = Part(null, null, "sf", "Wakamau", 53000, null, R.drawable.four)
        part4.name = "Brembo brakes"
        partsAdapter.addParts(part4)

        val part5 = Part(null, null, "sf", "Wakamau", 53000, null, R.drawable.four)
        part5.name = "Brembo brakes"
        partsAdapter.addParts(part5)
    }

}
