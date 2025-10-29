package de.brudaswen.kotlin.polyline

import de.brudaswen.kotlin.polyline.distance.HaversineDistance

/**
 * Coordinate of a [Polyline].
 */
public interface Coordinate {
    /** The latitude of the coordinate. */
    public val lat: Double

    /** The longitude of the coordinate. */
    public val lon: Double
}

/**
 * Create simple [Coordinate].
 *
 * @param lat The latitude of the coordinate.
 * @param lon The longitude of the coordinate.
 */
public fun Coordinate(
    lat: Double,
    lon: Double,
): Coordinate = CoordinateImpl(
    lat = lat,
    lon = lon,
)

public fun Coordinate.distanceTo(
    other: Coordinate,
): Double = HaversineDistance.distanceInMeters(
    lat1 = this.lat,
    lon1 = this.lon,
    lat2 = other.lat,
    lon2 = other.lon,
)

internal data class CoordinateImpl(
    override val lat: Double,
    override val lon: Double,
) : Coordinate
