package de.brudaswen.kotlin.polyline

import kotlin.test.Test
import kotlin.test.assertEquals

internal class PolylineTest {

    @Test
    fun `when polyline is sampled with zero then remove redundant points`() {
        val input = Polyline(
            coordinates = listOf(
                Coordinate(0.0, 0.0),
                Coordinate(0.0, 0.0),
                Coordinate(0.0, 0.5),
                Coordinate(0.0, 0.5),
                Coordinate(0.0, 1.0),
                Coordinate(0.0, 1.0),
                Coordinate(0.5, 1.0),
                Coordinate(0.5, 1.0),
                Coordinate(1.0, 1.0),
                Coordinate(1.0, 1.0),
            ),
        )
        assertEquals(
            expected = Polyline(
                coordinates = listOf(
                    Coordinate(0.0, 0.0),
                    Coordinate(0.0, 1.0),
                    Coordinate(1.0, 1.0),
                ),
            ),
            actual = input.sample(thresholdInMeters = 0.0, epsilon = 0.0),
        )
    }
}
