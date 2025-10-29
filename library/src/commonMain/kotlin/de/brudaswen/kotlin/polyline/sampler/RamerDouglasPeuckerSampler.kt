package de.brudaswen.kotlin.polyline.sampler

import de.brudaswen.kotlin.polyline.Coordinate
import de.brudaswen.kotlin.polyline.Polyline
import de.brudaswen.kotlin.polyline.distance.CrossTrackDistance

/**
 * Sample [Polyline] using the Ramer-Douglas-Peucker algorithm.
 *
 * The `Ramer-Douglas-Peucker` algorithm, also known as the `Douglas-Peucker` algorithm, is a method
 * used to simplify a polyline composed of line segments into a similar curve with fewer points.
 * The primary goal of the algorithm is to reduce  thecomplexity of a polyline while retaining its
 * essential shape.
 *
 * The algorithm works by recursively dividing a line. It starts with the line segment connecting
 * the first and last points of the curve. Then, it finds the point on the curve that is farthest
 * from this line segment. If this point is closer than a specified tolerance [epsilon], all the
 * points between the start and end points of the segment are discarded. If it is farther than the
 * tolerance, that point is kept, and the algorithm is applied recursively to the two new
 * sub-segments created on either side of this point.
 *
 * @param epsilon The maximum distance between the original curve and the simplified curve. A
 * smaller epsilon results in a curve with more points, closer to the original, while a larger
 * epsilon leads to a more simplified curve with fewer points. This value must be greater than zero.
 */
public class RamerDouglasPeuckerSampler(
    private val epsilon: Double,
) : TrackSampler {

    override fun sample(coordinates: List<Coordinate>): List<Coordinate> {
        if (epsilon <= 0.0) return coordinates
        if (coordinates.size < 3) return coordinates

        return ramerDouglasPeucker(
            polyline = coordinates,
            epsilon = epsilon,
        ).toList()
    }

    /**
     * Applies the Ramer–Douglas–Peucker algorithm to simplify a polyline.
     *
     * @param polyline The polyline to simplify.
     * @param epsilon The maximum allowed perpendicular distance (tolerance) in meters.
     *
     * @return A list of indices of the points that should be retained.
     */
    private fun ramerDouglasPeucker(
        polyline: List<Coordinate>,
        epsilon: Double,
    ): Sequence<Coordinate> {
        val start = polyline.first()
        val end = polyline.last()

        // Find the point with the maximum distance from the line segment (startIndex to endIndex)
        val maxDistanceIndex = (1..polyline.lastIndex - 1)
            .asSequence()
            .map { index ->
                index to CrossTrackDistance.crossTrackDistanceMeters(
                    point = polyline[index],
                    start = start,
                    end = end,
                )
            }
            .maxByOrNull { it.second }
            ?.takeIf { it.second > epsilon }
            ?.first

        return if (maxDistanceIndex == null) {
            // The entire segment is simple enough (max deviation < epsilon for all points between
            // start amd end index), so we only keep the endpoints
            sequenceOf(start, end)
        } else {
            // Recursively call for the first half (start to maxDistanceIndex)
            val firstHalfIndices = ramerDouglasPeucker(
                polyline = polyline.subList(0, maxDistanceIndex + 1),
                epsilon = epsilon,
            )

            // Recursively call for the second half (maxDistanceIndex to end)
            val secondHalfIndices = ramerDouglasPeucker(
                polyline = polyline.subList(maxDistanceIndex, polyline.lastIndex + 1),
                epsilon = epsilon,
            )

            firstHalfIndices + secondHalfIndices.drop(1)
        }
    }
}
