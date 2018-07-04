package com.gelostech.automartadmin.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gelostech.automartadmin.R
import com.gelostech.automartadmin.commoners.AppUtils
import com.gelostech.automartadmin.commoners.AppUtils.setDrawable
import com.gelostech.automartadmin.models.Booking
import com.gelostech.automartadmin.utils.TimeFormatter
import com.gelostech.automartadmin.utils.inflate
import com.gelostech.automartadmin.utils.loadUrl
import com.gelostech.automartadmin.utils.setDrawable
import com.mikepenz.ionicons_typeface_library.Ionicons
import kotlinx.android.synthetic.main.item_booking.view.*

class BookingsAdapter(val context: Context) : RecyclerView.Adapter<BookingsAdapter.BookingsHolder>() {
    private val bookings = mutableListOf<Booking>()

    fun addBooking(booking: Booking) {
        bookings.add(booking)
        notifyItemInserted(bookings.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingsHolder {
        return BookingsHolder(parent.inflate(R.layout.item_booking), context)
    }

    override fun getItemCount(): Int = bookings.size

    override fun onBindViewHolder(holder: BookingsHolder, position: Int) {
        holder.bind(bookings[position])
    }

    class BookingsHolder(view: View, val context: Context) : RecyclerView.ViewHolder(view) {
        private val bookingImage = view.image
        private val bookingName = view.name
        private val bookingUser = view.user
        private val bookingDate = view.date

        init {
            bookingUser.setDrawable(setDrawable(context, Ionicons.Icon.ion_person, R.color.secondaryText, 14))
            bookingDate.setDrawable(setDrawable(context, Ionicons.Icon.ion_calendar, R.color.secondaryText, 14))
        }

        fun bind(booking: Booking) {

            with(booking) {
                bookingImage.loadUrl(holderImage!!)
                bookingName.text = name
                bookingUser.text = bookerName
                bookingDate.text = TimeFormatter().getDetailDate(date!!)
            }

        }

    }

}