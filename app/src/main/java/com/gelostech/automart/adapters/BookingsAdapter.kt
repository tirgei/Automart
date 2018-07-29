package com.gelostech.automart.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gelostech.automart.R
import com.gelostech.automart.commoners.AppUtils
import com.gelostech.automart.commoners.AppUtils.setDrawable
import com.gelostech.automart.databinding.ItemBookingBinding
import com.gelostech.automart.models.Booking
import com.gelostech.automart.utils.TimeFormatter
import com.gelostech.automart.utils.inflate
import com.gelostech.automart.utils.loadUrl
import com.gelostech.automart.utils.setDrawable
import com.google.firebase.auth.FirebaseAuth
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

    class BookingsHolder(private val binding: ItemBookingBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.user.setDrawable(setDrawable(context, Ionicons.Icon.ion_person, R.color.secondaryText, 14))
            binding.date.setDrawable(setDrawable(context, Ionicons.Icon.ion_calendar, R.color.secondaryText, 14))
        }

        fun bind(booking: Booking) {

            binding.data = booking
            binding.isMine = (booking.sellerId == FirebaseAuth.getInstance().currentUser?.uid)

        }

    }

}