package de.brudaswen.kotlin.polyline

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

internal data class CoordinateImpl(
    override val lat: Double,
    override val lon: Double,
) : Coordinate
