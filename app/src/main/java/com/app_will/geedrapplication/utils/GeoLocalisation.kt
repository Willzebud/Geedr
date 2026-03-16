package com.app_will.geedrapplication.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.app_will.geedrapplication.R

class GeoLocalisation(
    private val lat: Double,
    private val lon: Double
) {
    companion object {
        const val earthRadiusKm: Double = 6372.8
    }

    fun haversine(destination: GeoLocalisation): Double {
        val dLat = Math.toRadians(destination.lat - this.lat);
        val dLon = Math.toRadians(destination.lon - this.lon);
        val originLat = Math.toRadians(this.lat);
        val destinationLat = Math.toRadians(destination.lat);

        val a = Math.pow(
            Math.sin(dLat / 2),
            2.toDouble()
        ) + Math.pow(
            Math.sin(dLon / 2),
            2.toDouble()
        ) * Math.cos(originLat) * Math.cos(destinationLat);
        val c = 2 * Math.asin(Math.sqrt(a));
        return earthRadiusKm * c;
    }
}

fun actualGeoLocalisation(context: Context, latitude: Double, longitude: Double): String {

    val userGeo = GeoLocalisation(43.3375293, 5.3923826)
    val placeGeo = GeoLocalisation(latitude, longitude)

    val distanceKm = userGeo.haversine(placeGeo)

    return "${(distanceKm * 1000).toInt()} ${context.getString(R.string.meter)}"
}

fun openGoogleMap(
    context: Context,
) {
    val gmmIntentUri = Uri.parse(GEO_URI_STRING)
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

    context.startActivity(mapIntent)
}
