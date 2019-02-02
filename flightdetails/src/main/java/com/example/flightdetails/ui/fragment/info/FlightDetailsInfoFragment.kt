package com.example.flightdetails.ui.fragment.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coreandroid.ext.screenOrientation
import com.example.coreandroid.ext.setLayoutManager
import com.example.coreandroid.model.Flight
import com.example.coreandroid.model.FlightDetails
import com.example.flightdetails.R
import com.example.flightdetails.ui.fragment.FlightDetailsFragment
import kotlinx.android.synthetic.main.fragment_flight_details_info.view.*


class FlightDetailsInfoFragment : FlightDetailsFragment() {

    private val infoListAdapter: FlightDetailsInfoAdapter by lazy {
        FlightDetailsInfoAdapter(flightDetails?.info ?: emptyList())
    }

    override var flightDetails: FlightDetails? = null
        set(value) {
            if (value == null || field != null) return
            field = value
            infoListAdapter.items = value.info
            infoListAdapter.notifyDataSetChanged()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flight_details_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
    }

    private fun initViews(view: View) {
        view.flight_info_fragment_recyler_view?.setLayoutManager(view.context, activity!!.screenOrientation, 2)
        view.flight_info_fragment_recyler_view?.adapter = infoListAdapter
    }

    companion object {
        fun newInstance(
                flight: Flight
        ): FlightDetailsInfoFragment = FlightDetailsInfoFragment().apply { putArguments(flight) }
    }
}
