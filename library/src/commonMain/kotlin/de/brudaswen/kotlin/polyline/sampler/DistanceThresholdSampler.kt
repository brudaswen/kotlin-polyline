package de.brudaswen.kotlin.polyline.sampler

import de.brudaswen.kotlin.polyline.Coordinate
import de.brudaswen.kotlin.polyline.Polyline
import de.brudaswen.kotlin.polyline.distanceTo

/**
 * Sample [Polyline] by discarding points that are closer than [thresholdInMeters] to
 * the last retained point.
 *
 * This is a simple and fast method to clean up any noise from the polyline (e.g. similar nodes
 * while not moving).
 *
 * @param thresholdInMeters The minimum distance (in meters) required to keep a point.
 */
public class DistanceThresholdSampler(
    private val thresholdInMeters: Double,
) : TrackSampler {
    override fun sample(coordinates: List<Coordinate>): List<Coordinate> {
        if (thresholdInMeters <= 0.0) return coordinates
        if (coordinates.size < 2) return coordinates

        val sampled = mutableListOf<Coordinate>()

        var current = coordinates.first()
        sampled.add(current)

        coordinates.forEach { coordinate ->
            if (current.distanceTo(coordinate) >= thresholdInMeters) {
                sampled.add(coordinate)
                current = coordinate
            }
        }

        return sampled
    }
}
