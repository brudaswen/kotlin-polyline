package de.brudaswen.kotlin.polyline

/**
 * A polyline of multiple [Coordinates][Coordinate].
 */
public interface Polyline {
    /** The [Coordinates][Coordinate] representing this polyline. */
    public val coordinates: List<Coordinate>

    public companion object {
        /**
         * Decode a [Polyline] from [String] using
         * [Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html).
         *
         * @param polyline The [String] encoded polyline.
         * @return The decoded [Polyline].
         */
        public fun from(polyline: String): Polyline =
            PolylineEncoding.decode(polyline)
    }
}

/**
 * Create simple [Polyline].
 *
 * @param coordinates The [Coordinates][Coordinate] representing this polyline.
 */
public fun Polyline(
    coordinates: List<Coordinate>,
): Polyline = PolylineImpl(
    coordinates = coordinates,
)

/**
 * Encodes this [Polyline] as [String] using
 * [Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html).
 *
 * @return String encoded polyline.
 */
public fun Polyline.encoded(): String = PolylineEncoding.encode(this)

internal data class PolylineImpl(
    override val coordinates: List<Coordinate>,
) : Polyline
