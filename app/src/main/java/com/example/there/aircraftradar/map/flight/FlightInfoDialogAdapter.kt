package com.example.there.aircraftradar.map.flight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.there.aircraftradar.R
import com.example.there.aircraftradar.view.FlightInfoBaseAdapter
import kotlinx.android.synthetic.main.dialog_flight_details_info_item.view.*


class FlightInfoDialogAdapter(
        items: List<Pair<String, String>>
) : FlightInfoBaseAdapter<FlightInfoDialogAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dialog_flight_details_info_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View) : FlightInfoBaseAdapter.ViewHolder(itemView) {
        override val infoLabelTxt: TextView = itemView.dialog_info_label_txt
        override val infoTxt: TextView = itemView.dialog_info_txt
    }
}