package com.gelostech.automart.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.gelostech.automart.R
import com.gelostech.automart.adapters.CarsAdapter
import com.gelostech.automart.callbacks.CarCallback
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.BaseActivity
import com.gelostech.automart.commoners.K
import com.gelostech.automart.models.Car
import com.gelostech.automart.utils.RecyclerFormatter
import com.gelostech.automart.utils.hideView
import com.gelostech.automart.utils.showView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_watchlist.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import timber.log.Timber

class WatchlistActivity : BaseActivity(), CarCallback {
    private lateinit var carsAdapter: CarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_watchlist)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Watchlist"

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)
        rv.itemAnimator = DefaultItemAnimator()
        rv.addItemDecoration(RecyclerFormatter.DoubleDividerItemDecoration(this))

        carsAdapter = CarsAdapter(this, this)
        rv.adapter = carsAdapter
        rv.showShimmerAdapter()

        loadCars()
    }

    private fun loadCars() {
        getFirestore().collection(K.WATCHLIST).document(getUid()).collection(K.CARS)
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
                    alert("Mark ${car.make} ${car.model} as sold?") {
                        positiveButton("YES") {}
                        negativeButton("CANCEL") {}
                    }!!.show()

                } else {
                    val i = Intent(this, CarActivity::class.java)
                    i.putExtra(K.CAR, car)
                    startActivity(i)
                    AppUtils.animateFadein(this)
                }
            }

            R.id.moreOptions -> {
                if (car.sellerId == getUid()) {
                    //sellerOptions(car)
                } else {
                    buyerOptions(car)
                }
            }

            R.id.image -> {
                val i = Intent(this, CarActivity::class.java)
                i.putExtra(K.CAR, car)
                startActivity(i)
                AppUtils.animateFadein(this)
            }

            R.id.contact -> {
                if (car.sellerId == getUid()) {
                    val i = Intent(this, AddCarActivity::class.java)
                    startActivity(i)
                    AppUtils.animateEnterLeft(this)

                } else {
                    val i = Intent(this, ChatActivity::class.java)
                    i.putExtra(K.MY_ID, getUid())
                    i.putExtra(K.OTHER_ID, car.sellerId)
                    i.putExtra(K.CHAT_NAME, car.sellerName)
                    this.startActivity(i)
                    AppUtils.animateFadein(this)
                }
            }
        }
    }

    private fun buyerOptions(car: Car) {
        val builder = AlertDialog.Builder(this)

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
            toast("${car.make} ${car.model} added to watchlist")

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
            toast("${car.make} ${car.model} removed from watchlist")

            getFirestore().collection(K.WATCHLIST).document(getUid()).collection(K.CARS).document(car.id!!).delete()
        }.addOnFailureListener {
            Timber.e("Error removing ${car.id} from watchlist: $it")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils.animateEnterLeft(this)
    }
}
