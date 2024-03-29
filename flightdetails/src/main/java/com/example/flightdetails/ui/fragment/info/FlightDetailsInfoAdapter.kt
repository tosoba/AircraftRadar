package com.example.flightdetails.ui.fragment.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.coreandroid.view.FlightInfoBaseAdapter
import com.example.flightdetails.R
import kotlinx.android.synthetic.main.fragment_flight_details_info_item.view.*

class FlightDetailsInfoAdapter(
    items: List<Pair<String, String>>
) : FlightInfoBaseAdapter<FlightDetailsInfoAdapter.ViewHolder>(items) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_flight_details_info_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : FlightInfoBaseAdapter.ViewHolder(itemView) {
        override val infoLabelTxt: TextView = itemView.fragment_info_label_txt
        override val infoTxt: TextView = itemView.fragment_info_txt
    }
}