package com.gelostech.automart.fragments


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
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
import kotlinx.android.synthetic.main.fragment_my_cars.*
import kotlinx.android.synthetic.main.fragment_my_cars.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
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
        (rv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        carsAdapter = CarsAdapter(activity!!, this)
        rv.adapter = carsAdapter
        rv.showShimmerAdapter()
    }

    private fun loadCars() {
        getFirestore().collection(K.CARS)
                .whereEqualTo("sellerId", getUid())
                //.orderBy(K.TIMESTAMP, Query.Direction.DESCENDING)
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
                                    val car = docChange.document.toObject(Car::class.java)
                                    carsAdapter.updateCar(car)
                                }

                                DocumentChange.Type.REMOVED -> {
                                    val car = docChange.document.toObject(Car::class.java)
                                    carsAdapter.removeCar(car)
                                }

                            }

                        }

                    }
                }
    }

    private fun hasCars() {
        rv?.hideShimmerAdapter()
        empty?.hideView()
        rv?.showView()
    }

    private fun noCars() {
        rv?.hideShimmerAdapter()
        rv?.hideView()
        empty?.showView()
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

            R.id.moreOptions -> {
                if (car.sellerId == getUid()) {
                    sellerOptions(car)
                } else {
                    //buyerOptions(car)
                }
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

    private fun sellerOptions(car: Car) {
        val items = arrayOf<CharSequence>("Delete")

        val builder = AlertDialog.Builder(activity!!)
        builder.setItems(items) { _, item ->
            when(item) {
                0 -> {
                    getFirestore().collection(K.CARS).document(car.id!!).delete()
                            .addOnSuccessListener {
                                activity?.toast("${car.make} ${car.model} deleted!")
                            }
                            .addOnFailureListener {
                                Timber.e("Error deleting ${car.id}")
                                activity?.toast("Error deleting ${car.make} ${car.model}")
                            }
                }
            }
        }
        val alert = builder.create()
        alert.show()
    }

}
