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
import com.gelostech.automart.activities.AddCarActivity
import com.gelostech.automart.activities.CarActivity
import com.gelostech.automart.activities.ChatActivity
import com.gelostech.automart.adapters.CarsAdapter
import com.gelostech.automart.callbacks.CarCallback
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.BaseFragment
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Car
import com.gelostech.automart.utils.RecyclerFormatter
import com.gelostech.automart.utils.hideView
import com.gelostech.automart.utils.showView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_add_car.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.alert
import timber.log.Timber

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

        loadCars()
    }

    private fun initViews(v: View) {
        v.rv.setHasFixedSize(true)
        v.rv.layoutManager = LinearLayoutManager(activity)
        v.rv.itemAnimator = DefaultItemAnimator()
        v.rv.addItemDecoration(RecyclerFormatter.DoubleDividerItemDecoration(activity!!))

        carsAdapter = CarsAdapter(activity!!, this)
        v.rv.adapter = carsAdapter
        v.rv.showShimmerAdapter()
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
                                    carsAdapter.addCar(car)
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
        if (rv != null) {
            rv.hideShimmerAdapter()
        }
        rv.hideView()
        empty.showView()
    }

    override fun onClick(v: View, car: Car) {
        when(v.id) {
            R.id.action -> {
                if (car.sellerId == getUid()) {
                    activity?.alert("Mark ${car.make} ${car.model} as sold?") {
                        positiveButton("YES") {}
                        negativeButton("CANCEL") {}
                    }!!.show()

                } else {
                    val i = Intent(activity, CarActivity::class.java)
                    i.putExtra(K.CAR, car)
                    startActivity(i)
                    AppUtils.animateFadein(activity!!)
                }
            }

            R.id.image -> {
                val i = Intent(activity, CarActivity::class.java)
                i.putExtra(K.CAR, car)
                startActivity(i)
                AppUtils.animateFadein(activity!!)
            }

            R.id.contact -> {
                if (car.sellerId == getUid()) {
                    val i = Intent(activity, AddCarActivity::class.java)
                    activity!!.startActivity(i)
                    AppUtils.animateEnterLeft(activity!!)
                } else {
                    val i = Intent(activity, ChatActivity::class.java)
                    i.putExtra(K.CHAT_ID, car.sellerId)
                    i.putExtra(K.CHAT_NAME, car.sellerName)
                    activity!!.startActivity(i)
                    AppUtils.animateFadein(activity!!)
                }
            }
        }
    }
}
