package de.brudaswen.kotlin.polyline.distance

import de.brudaswen.kotlin.polyline.Coordinate
import de.brudaswen.kotlin.polyline.distanceTo
import de.brudaswen.kotlin.polyline.math.toRadians
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Calculates the perpendicular (cross-track) distance.
 */
internal object CrossTrackDistance {

    /**
     * Calculates the perpendicular (cross-track) distance from [point] to the great-circle
     * segment defined by points [start] and [end].
     *
     * @return Distance in meters.
     */
    fun crossTrackDistanceMeters(
        point: Coordinate,
        start: Coordinate,
        end: Coordinate,
    ): Double {
        val latA = start.lat.toRadians()
        val lonA = start.lon.toRadians()
        val latB = end.lat.toRadians()
        val lonB = end.lon.toRadians()
        val latP = point.lat.toRadians()
        val lonP = point.lon.toRadians()

        // Calculate initial bearing from A to B
        val bearingAB = atan2(
            sin(lonB - lonA) * cos(latB),
            cos(latA) * sin(latB) - sin(latA) * cos(latB) * cos(lonB - lonA),
        )

        // Calculate initial bearing from A to P
        val bearingAP = atan2(
            sin(lonP - lonA) * cos(latP),
            cos(latA) * sin(latP) - sin(latA) * cos(latP) * cos(lonP - lonA),
        )

        // Calculate angular distance from A to P
        val angularDistAP = start.distanceTo(point) / EARTH_RADIUS_M

        // Calculate cross-track distance
        val crossTrackDist = asin(
            sin(angularDistAP) * sin(bearingAP - bearingAB),
        ) * EARTH_RADIUS_M

        // Return the absolute value
        return abs(crossTrackDist)
    }
}
