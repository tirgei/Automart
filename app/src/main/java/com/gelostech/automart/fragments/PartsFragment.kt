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
import kotlinx.android.synthetic.main.fragment_parts.*
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
        rv.setHasFixedSize(true)
        rv.layoutManager = GridLayoutManager(activity, 2)
        rv.addItemDecoration(RecyclerFormatter.GridItemDecoration(activity!!, 2, 10))
        rv.itemAnimator = DefaultItemAnimator()

        partsAdapter = PartsAdapter(this)
        rv.adapter = partsAdapter

    }

    override fun onClick(v: View, part: Part) {
        val i = Intent(activity, PartActivity::class.java)
        i.putExtra(K.PART, part)
        i.putExtra(K.PART_IMAGE, part.holderImage)
        startActivity(i)
        AppUtils.animateFadein(activity!!)

    }
}
