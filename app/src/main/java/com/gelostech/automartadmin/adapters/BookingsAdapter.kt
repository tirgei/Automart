package com.gelostech.automartadmin.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gelostech.automartadmin.R
import com.gelostech.automartadmin.models.Booking
import com.gelostech.automartadmin.utils.TimeFormatter
import com.gelostech.automartadmin.utils.inflate
import com.gelostech.automartadmin.utils.loadUrl
import kotlinx.android.synthetic.main.item_booking.view.*

class BookingsAdapter : RecyclerView.Adapter<BookingsAdapter.BookingsHolder>() {
    private val bookings = mutableListOf<Booking>()

    fun addBooking(booking: Booking) {
        bookings.add(booking)
        notifyItemInserted(bookings.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingsHolder {
        return BookingsHolder(parent.inflate(R.layout.item_booking))
    }

    override fun getItemCount(): Int = bookings.size

    override fun onBindViewHolder(holder: BookingsHolder, position: Int) {
        holder.bind(bookings[position])
    }

    class BookingsHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bookingImage = view.image
        private val bookingName = view.name
        private val bookingUser = view.user
        private val bookingDate = view.date

        fun bind(booking: Booking) {

            with(booking) {
                bookingImage.loadUrl(holderImage!!)
                bookingName.text = name
                bookingUser.text = bookerName
                bookingDate.text = TimeFormatter().getFullFormat(date!!)
            }

        }

    }

}