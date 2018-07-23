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
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Car
import com.gelostech.automart.utils.RecyclerFormatter
import com.gelostech.automart.utils.hideView
import com.gelostech.automart.utils.showView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_my_cars.*
import kotlinx.android.synthetic.main.fragment_my_cars.view.*
import timber.log.Timber

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

        loadCars()
    }

    private fun initViews(v: View) {
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(activity!!)
        rv.itemAnimator = DefaultItemAnimator()
        rv.addItemDecoration(RecyclerFormatter.DoubleDividerItemDecoration(activity!!))

        carsAdapter = CarsAdapter(activity!!, this)
        rv.adapter = carsAdapter
        rv.showShimmerAdapter()
    }

    private fun loadCars() {
        getFirestore().collection(K.CARS)
                .orderBy(K.TIMESTAMP, Query.Direction.DESCENDING)
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Timber.e("Error fetching cars $firebaseFirestoreException")
                        noCars()
                    }

                    if (querySnapshot == null || querySnapshot.isEmpty) {
                        noCars()
                    } else {
                        hasCars()

                        for (docChange in querySnapshot.documentChanges) {

                            when(docChange.type) {
                                DocumentChange.Type.ADDED -> {
                                    val car = docChange.document.toObject(Car::class.java)
                                    if (car.sellerId == getUid()) {
                                        carsAdapter.addCar(car)
                                    }
                                }

                                DocumentChange.Type.MODIFIED -> {

                                }

                                DocumentChange.Type.REMOVED -> {

                                }

                            }

                        }

                    }
                }
    }

    private fun hasCars() {
        rv.hideShimmerAdapter()
        empty.hideView()
        rv.showView()
    }

    private fun noCars() {
        rv.hideShimmerAdapter()
        rv.hideView()
        empty.showView()
    }

    override fun onClick(v: View, car: Car) {

    }
}
