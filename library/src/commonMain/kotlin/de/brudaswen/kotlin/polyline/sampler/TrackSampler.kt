package de.brudaswen.kotlin.polyline.sampler

import de.brudaswen.kotlin.polyline.Coordinate
import de.brudaswen.kotlin.polyline.Polyline

/**
 * Sample a [Polyline] by discarding unimportant nodes.
 */
public fun interface TrackSampler {
    /**
     * Sample a [Polyline] by discarding unimportant nodes.
     *
     * @param polyline The polyline to sample.
     * @return The sampled polyline.
     */
    public fun sample(polyline: Polyline): Polyline =
        Polyline(
            coordinates = sample(polyline.coordinates),
        )

    /**
     * Sample a [Polyline] by discarding unimportant nodes.
     *
     * @param coordinates The polyline to sample.
     * @return The sampled polyline.
     */
    public fun sample(coordinates: List<Coordinate>): List<Coordinate>
}
