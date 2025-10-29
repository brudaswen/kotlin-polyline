package de.brudaswen.kotlin.polyline

import de.brudaswen.kotlin.polyline.sampler.DistanceThresholdSampler
import de.brudaswen.kotlin.polyline.sampler.RamerDouglasPeuckerSampler

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

/**
 * Sample a [Polyline] by discarding unimportant nodes.
 *
 * First applies the [DistanceThresholdSampler] by discarding points that are closer than
 * [thresholdInMeters] to the last retained point.
 *
 * Afterwards, applies the [RamerDouglasPeuckerSampler] to discard points that are close to a
 * simplified curve.
 *
 * @param thresholdInMeters The minimum distance (in meters) required to keep a point.
 * @param epsilon The maximum distance between the original curve and the simplified curve. A
 * smaller epsilon results in a curve with more points, closer to the original, while a larger
 * epsilon leads to a more simplified curve with fewer points. This value must be greater than zero.
 */
public fun Polyline.sample(
    thresholdInMeters: Double = 1.0,
    epsilon: Double = 1.0,
): Polyline = Polyline(
    coordinates = RamerDouglasPeuckerSampler(epsilon = epsilon).sample(
        coordinates = DistanceThresholdSampler(thresholdInMeters = thresholdInMeters).sample(
            coordinates,
        ),
    ),
)

internal data class PolylineImpl(
    override val coordinates: List<Coordinate>,
) : Polyline
