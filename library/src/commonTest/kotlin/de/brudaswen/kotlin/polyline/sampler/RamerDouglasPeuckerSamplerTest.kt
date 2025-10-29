package de.brudaswen.kotlin.polyline.sampler

import de.brudaswen.kotlin.polyline.Coordinate
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RamerDouglasPeuckerSamplerTest {

    private val sampler = RamerDouglasPeuckerSampler(
        epsilon = 1.0,
    )

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
    fun `when sampling list with distance larger than epsilon then return original list`() {
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
    fun `when sampling list with distance smaller than epsilon then drop coordinate`() {
        val input = listOf(
            Coordinate(0.0, 0.0),
            Coordinate(0.0, 5.0),
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
            Coordinate(4.999993, 4.999993),
            Coordinate(4.999994, 4.999994),
            Coordinate(5.0, 5.0),
            Coordinate(4.999996, 4.999996),
            Coordinate(4.999997, 4.999997),
            Coordinate(0.000001, 9.999998),
            Coordinate(0.000001, 9.999999),
            Coordinate(0.0, 10.0),
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
