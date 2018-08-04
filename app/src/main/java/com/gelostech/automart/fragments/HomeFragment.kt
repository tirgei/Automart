package com.gelostech.automart.fragments


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SimpleItemAnimator
import android.util.Log
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
import com.google.firebase.firestore.Transaction
import kotlinx.android.synthetic.main.activity_add_car.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
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
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.itemAnimator = DefaultItemAnimator()
        rv.addItemDecoration(RecyclerFormatter.DoubleDividerItemDecoration(activity!!))
        (rv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

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
                        positiveButton("YES") {

                            getFirestore().collection(K.SOLD).document(getUid()).collection(K.CARS).document(car.id!!).set(car)

                            getFirestore().collection(K.CARS).document(car.id!!).delete().addOnSuccessListener {
                                activity?.toast("${car.make} ${car.model} sold")
                            }
                        }
                        negativeButton("CANCEL") {}
                    }!!.show()

                } else {
                    val i = Intent(activity, CarActivity::class.java)
                    i.putExtra(K.CAR, car)
                    startActivity(i)
                    AppUtils.animateFadein(activity!!)
                }
            }

            R.id.moreOptions -> {
                if (car.sellerId == getUid()) {
                    sellerOptions(car)
                } else {
                    buyerOptions(car)
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
                    i.putExtra(K.CAR, car)
                    activity!!.startActivity(i)
                    AppUtils.animateEnterLeft(activity!!)

                } else {
                    val i = Intent(activity, ChatActivity::class.java)
                    i.putExtra(K.MY_ID, getUid())
                    i.putExtra(K.OTHER_ID, car.sellerId)
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

    private fun buyerOptions(car: Car) {
        val builder = AlertDialog.Builder(activity!!)

        if (car.watchlist.containsKey(getUid())) {
            val items = arrayOf<CharSequence>("Remove from watchlist")

            builder.setItems(items) { _, item ->
                when(item) {
                    0 -> {
                        removeFromWatchlist(car)
                    }
                }
            }

        } else {
            val items = arrayOf<CharSequence>("Add to watchlist")

            builder.setItems(items) { _, item ->
                when(item) {
                    0 -> {
                        addToWatchList(car)
                    }
                }
            }

        }

        val alert = builder.create()
        alert.show()
    }

    private fun addToWatchList(car: Car) {
        val docRef = getFirestore().collection(K.CARS).document(car.id!!)

        getFirestore().runTransaction {
            val snapshot = it[docRef].toObject(Car::class.java)
            val watchlist = snapshot?.watchlist
            watchlist?.put(getUid(), true)
            car.watchlist[getUid()] = true

            it.update(docRef, K.WATCHLIST, watchlist)

            return@runTransaction null
        }.addOnSuccessListener {
            Timber.e("Successfully added ${car.id} to ${getUid()} watchlist")
            activity?.toast("${car.make} ${car.model} added to watchlist")

            getFirestore().collection(K.WATCHLIST).document(getUid()).collection(K.CARS).document(car.id!!).set(car)
        }.addOnFailureListener {
            Timber.e("Error adding ${car.id} to watchlist: $it")
        }
    }

    private fun removeFromWatchlist(car: Car) {
        val docRef = getFirestore().collection(K.CARS).document(car.id!!)

        getFirestore().runTransaction {
            val snapshot = it[docRef].toObject(Car::class.java)
            val watchlist = snapshot?.watchlist
            watchlist?.remove(getUid())
            car.watchlist.remove(getUid())

            it.update(docRef, K.WATCHLIST, watchlist)

            return@runTransaction null
        }.addOnSuccessListener {
            Timber.e("Successfully removed ${car.id} from ${getUid()} watchlist")
            activity?.toast("${car.make} ${car.model} removed from watchlist")

            getFirestore().collection(K.WATCHLIST).document(getUid()).collection(K.CARS).document(car.id!!).delete()
        }.addOnFailureListener {
            Timber.e("Error removing ${car.id} from watchlist: $it")
        }
    }
}
