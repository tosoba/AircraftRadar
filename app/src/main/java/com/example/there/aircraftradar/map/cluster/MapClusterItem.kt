package com.example.there.aircraftradar.map.cluster

import android.os.Parcel
import android.os.Parcelable
import com.example.there.aircraftradar.data.impl.flights.Flight
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class MapClusterItem(val flight: Flight): ClusterItem, Parcelable {
    constructor(parcel: Parcel) : this(parcel.readParcelable(Flight::class.java.classLoader) as Flight)

    override fun getSnippet(): String = "${flight.origin} - ${flight.destination}"
    override fun getTitle(): String = flight.flight
    override fun getPosition(): LatLng = LatLng(flight.latitude, flight.longitude)

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeParcelable(flight, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<MapClusterItem> {
        override fun createFromParcel(parcel: Parcel): MapClusterItem = MapClusterItem(parcel)
        override fun newArray(size: Int): Array<MapClusterItem?> = arrayOfNulls(size)
    }
}