package com.example.there.aircraftradar.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

abstract class FlightInfoBaseAdapter<VH>(var items: List<Pair<String, String>>) : RecyclerView.Adapter<VH>() where VH : FlightInfoBaseAdapter.ViewHolder {
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.infoLabelTxt.text = item.first
        holder.infoTxt.text = item.second
    }

    abstract class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        abstract val infoLabelTxt: TextView
        abstract val infoTxt: TextView
    }
}