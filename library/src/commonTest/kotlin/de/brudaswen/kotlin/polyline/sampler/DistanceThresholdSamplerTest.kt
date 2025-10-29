package de.brudaswen.kotlin.polyline.sampler

import de.brudaswen.kotlin.polyline.Coordinate
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DistanceThresholdSamplerTest {

    private val sampler = DistanceThresholdSampler(
        thresholdInMeters = 1.0,
    )

    @Test
    fun `when thresholdInMeters is negative then no sampling should occur`() {
        val sampler = DistanceThresholdSampler(
            thresholdInMeters = -1.0,
        )

        val input = listOf(Coordinate(0.0, 0.0), Coordinate(0.0, 0.0))
        assertEquals(
            expected = input,
            actual = sampler.sample(input),
        )
    }

    @Test
    fun `when thresholdInMeters is zero then remove redundant points`() {
        val sampler = DistanceThresholdSampler(
            thresholdInMeters = 0.0,
        )

        val input = listOf(
            Coordinate(0.0, 0.0),
            Coordinate(0.0, 0.0),
            Coordinate(1.0, 1.0),
            Coordinate(1.0, 1.0),
            Coordinate(1.0, 1.0),
            Coordinate(0.0, 0.0),
            Coordinate(0.0, 0.0),
        )
        assertEquals(
            expected = listOf(
                Coordinate(0.0, 0.0),
                Coordinate(1.0, 1.0),
                Coordinate(0.0, 0.0),
            ),
            actual = sampler.sample(input),
        )
    }

    @Test
    fun `when sampling empty list then return empty list`() {
        val input = emptyList<Coordinate>()
        assertEquals(
            expected = input,
            actual = sampler.sample(input),
        )
    }

    @Test
    fun `when sampling single-item list then return original list`() {
        val input = listOf(Coordinate(0.0, 0.0))
        assertEquals(
            expected = input,
            actual = sampler.sample(input),
        )
    }

    @Test
    fun `when sampling two-item list then return original list`() {
        val input = listOf(Coordinate(0.0, 0.0), Coordinate(1.0, 1.0))
        assertEquals(
            expected = input,
            actual = sampler.sample(input),
        )
    }

    @Test
    fun `when sampling list with distance larger than threshold then return original list`() {
        val input = listOf(
            Coordinate(0.0, 0.0),
            Coordinate(5.0, 5.0),
            Coordinate(0.0, 10.0),
        )
        assertEquals(
            expected = input,
            actual = sampler.sample(input),
        )
    }

    @Test
    fun `when sampling list with distance smaller than threshold then drop coordinate`() {
        val input = listOf(
            Coordinate(0.0, 0.0),
            Coordinate(0.000001, 0.000001),
            Coordinate(0.0, 10.0),
        )
        assertEquals(
            expected = listOf(
                Coordinate(0.0, 0.0),
                Coordinate(0.0, 10.0),
            ),
            actual = sampler.sample(input),
        )
    }

    @Test
    fun `when sampling list then drop coordinates with distance smaller than epsilon`() {
        val input = listOf(
            Coordinate(0.0, 0.0),
            Coordinate(0.000001, 0.000001),
            Coordinate(0.000002, 0.000002),
            Coordinate(5.0, 5.0),
            Coordinate(4.999996, 4.999996),
            Coordinate(5.000001, 5.000001),
            Coordinate(0.0, 10.0),
            Coordinate(0.000001, 9.999998),
            Coordinate(0.000001, 10.000001),
        )
        assertEquals(
            expected = listOf(
                Coordinate(0.0, 0.0),
                Coordinate(5.0, 5.0),
                Coordinate(0.0, 10.0),
            ),
            actual = sampler.sample(input),
        )
    }
}
