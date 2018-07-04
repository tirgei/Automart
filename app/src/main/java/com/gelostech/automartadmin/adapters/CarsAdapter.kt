package com.gelostech.automartadmin.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.Time
import android.view.View
import android.view.ViewGroup
import com.gelostech.automartadmin.R
import com.gelostech.automartadmin.commoners.AppUtils
import com.gelostech.automartadmin.commoners.AppUtils.setDrawable
import com.gelostech.automartadmin.models.Car
import com.gelostech.automartadmin.utils.TimeFormatter
import com.gelostech.automartadmin.utils.inflate
import com.gelostech.automartadmin.utils.loadUrl
import com.gelostech.automartadmin.utils.setDrawable
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.item_car.view.*
import java.lang.ref.WeakReference

class CarsAdapter(private val context: Context, private val onItemClick: OnItemClick) : RecyclerView.Adapter<CarsAdapter.CarHolder>(){
    private val cars = mutableListOf<Car>()

    fun addCar(car: Car) {
        cars.add(car)
        notifyItemInserted(cars.size-1)
    }

    fun addCars(cars: MutableList<Car>) {
        val initialPos = cars.size

        cars.addAll(cars)
        notifyItemRangeInserted(initialPos, cars.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarHolder {
        return CarHolder(parent.inflate(R.layout.item_car), onItemClick, context)
    }

    override fun getItemCount(): Int = cars.size

    override fun onBindViewHolder(holder: CarHolder, position: Int) {
        holder.bind(cars[position])
    }

    class CarHolder(view: View, private val onItemClick: OnItemClick, private var context: Context) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val postAvatar = view.avatar
        private val postUsername = view.username
        private val postTime = view.time
        private val postImage = view.image
        private val postName = view.carName
        private val postPrice = view.price
        private var postLocation = view.location
        private val postFeatures = view.features
        private val moreOptions = view.moreOptions
        private val root = view.root

        private val weakReference = WeakReference<OnItemClick>(onItemClick)
        private val bullet = "\u2022"
        private lateinit var car: Car

        init {
            moreOptions.setImageDrawable(setDrawable(context, Ionicons.Icon.ion_android_more_vertical, R.color.secondaryText, 14))
            postLocation.setDrawable(setDrawable(context, Ionicons.Icon.ion_location, R.color.secondaryText, 12))
            root.setOnClickListener(this)
        }

        fun bind(car: Car) {
            this.car = car

            with(car) {
                postAvatar.loadUrl(holderAvatar!!)
                postUsername.text = sellerName
                postTime.text = TimeFormatter().getTimeStamp(time!!)
                postImage.loadUrl(holderImage!!)
                postName.text = "$make $model"
                postPrice.text = "KES $price"
                postLocation.text = location
                postFeatures.text = "$year $bullet ${mileage}km $bullet $transmission"
            }

        }

        override fun onClick(v: View?) {
            when(v?.id) {
                root.id -> {
                    weakReference.get()!!.onItemClick(car)
                }
            }
        }
    }

    interface OnItemClick{
        fun onItemClick(car: Car)
    }

}