package de.brudaswen.kotlin.polyline.distance

import de.brudaswen.kotlin.polyline.math.toRadians
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

internal const val EARTH_RADIUS_M = 6_371_000.0

/**
 * [Haversine distance](https://en.wikipedia.org/wiki/Haversine_formula) calculation between
 * two `(latitude, longitude)` coordinates.
 */
public object HaversineDistance {

    /**
     * Calculate distance between two `(latitude, longitude)` coordinates.
     *
     * @param lat1 Latitude of the first coordinate.
     * @param lon1 Longitude of the first coordinate.
     * @param lat2 Latitude of the second coordinate.
     * @param lon2 Longitude of the second coordinate.
     * @return The distance between the coordinates in meters.
     */
    public fun distanceInMeters(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
    ): Double {
        val lat1Rad = lat1.toRadians()
        val lon1Rad = lon1.toRadians()
        val lat2Rad = lat2.toRadians()
        val lon2Rad = lon2.toRadians()

        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        val a = sin(dLat / 2).pow(2) +
            sin(dLon / 2).pow(2) *
            cos(lat1Rad) * cos(lat2Rad)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_M * c
    }
}
