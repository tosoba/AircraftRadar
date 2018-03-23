package com.example.there.aircraftradar.map.cluster

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class MapClusterRenderer(context: Context,
                         map: GoogleMap,
                         clusterManager: ClusterManager<MapClusterItem>,
                         private val markerIcon: BitmapDescriptor): DefaultClusterRenderer<MapClusterItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: MapClusterItem?, markerOptions: MarkerOptions?) {
        item?.let {
            markerOptions?.icon(markerIcon)
            markerOptions?.rotation(item.flight.bearing.toFloat())
        }
    }
}