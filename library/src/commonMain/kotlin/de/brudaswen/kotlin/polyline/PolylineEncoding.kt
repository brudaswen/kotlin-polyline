package de.brudaswen.kotlin.polyline

import kotlin.math.roundToInt

/**
 * [Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html)
 * to convert [Polyline] to/from [String].
 *
 * Polyline encoding is a lossy compression algorithm that allows you to store a series of
 * coordinates as a single string.
 *
 * The encoding process converts a binary value into a series of character codes for ASCII
 * characters using the familiar base64 encoding scheme: to ensure proper display of these
 * characters, encoded values are summed with 63 (the ASCII character '?') before converting them
 * into ASCII. The algorithm also checks for additional character codes for a given point by
 * checking the least significant bit of each byte group; if this bit is set to 1, the point is not
 * yet fully formed and additional data must follow.
 *
 * Additionally, to conserve space, points only include the offset from the previous point (except
 * of course for the first point). All points are encoded in Base64 as signed integers, as latitudes
 * and longitudes are signed values. The encoding format within a polyline needs to represent two
 * coordinates representing latitude and longitude to a reasonable precision. Given a maximum
 * longitude of +/- 180 degrees to a precision of 5 decimal places (180.00000 to -180.00000), this
 * results in the need for a 32 bit signed binary integer value.
 */
public object PolylineEncoding {

    /**
     * Encodes a [Polyline] as [String] using
     * [Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html).
     *
     * @param polyline The polyline to encode.
     * @return String encoded polyline.
     */
    public fun encode(polyline: Polyline): String =
        encode(polyline = polyline.coordinates)

    /**
     * Encodes a [Polyline] as [String] using
     * [Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html).
     *
     * @param polyline The polyline to encode.
     * @return String encoded polyline.
     */
    public fun encode(polyline: List<Coordinate>): String =
        encode(polyline = polyline.asSequence())

    /**
     * Encodes a [Polyline] as [String] using
     * [Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html).
     *
     * @param polyline The polyline to encode.
     * @return String encoded polyline.
     */
    public fun encode(polyline: Sequence<Coordinate>): String {
        var previousLat = 0
        var previousLon = 0

        return buildString {
            polyline.forEach { coordinate ->
                val lat = coordinate.lat.toCoordinateInt()
                val lon = coordinate.lon.toCoordinateInt()

                val deltaLat = lat - previousLat
                val deltaLon = lon - previousLon

                appendEncodedValue(deltaLat)
                appendEncodedValue(deltaLon)

                previousLat = lat
                previousLon = lon
            }
        }
    }

    /**
     * Decode a [Polyline] from [String] using
     * [Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html).
     *
     * @param polyline The [String] encoded polyline.
     * @return The decoded [Polyline].
     */
    public fun decode(polyline: String): Polyline =
        Polyline(
            coordinates = decodeCoordinates(polyline).toList(),
        )

    /**
     * Decode a [Polyline] from [String] using
     * [Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html).
     *
     * @param polyline The [String] encoded polyline.
     * @param polylineFactory Custom [PolylineFactory] to create arbitrary [Polyline] type.
     * @param coordinateFactory Custom [CoordinateFactory] to create arbitrary [Coordinate] type.
     * @return The decoded [Polyline].
     */
    public fun <P : Polyline, C : Coordinate> decode(
        polyline: String,
        polylineFactory: PolylineFactory<P, C>,
        coordinateFactory: CoordinateFactory<C>,
    ): Polyline =
        polylineFactory.create(
            coordinates = decodeCoordinates(
                polyline = polyline,
                coordinateFactory = coordinateFactory,
            ),
        )

    /**
     * Decode a [Polyline] from [String] using
     * [Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html).
     *
     * @param polyline The [String] encoded polyline.
     * @return The decoded list of [Coordinates][Coordinate] of the [Polyline].
     */
    public fun decodeCoordinates(polyline: String): Sequence<Coordinate> =
        decodeCoordinates(
            polyline = polyline,
            coordinateFactory = { lat, lon -> Coordinate(lat = lat, lon = lon) },
        )

    /**
     * Decode a [Polyline] from [String] using
     * [Google Polyline encoding algorithm](http://code.google.com/apis/maps/documentation/polylinealgorithm.html).
     *
     * @param polyline The [String] encoded polyline.
     * @param coordinateFactory Custom [CoordinateFactory] to create arbitrary [Coordinate] type.
     * @return The decoded list of [Coordinates][Coordinate] of the [Polyline].
     */
    public fun <C : Coordinate> decodeCoordinates(
        polyline: String,
        coordinateFactory: CoordinateFactory<C>,
    ): Sequence<C> {
        val encoded = polyline.toCharArray().iterator()

        var previousLat = 0
        var previousLon = 0

        return generateSequence {
            if (encoded.hasNext()) {
                val deltaLat = encoded.readEncodedValue()
                val deltaLon = encoded.readEncodedValue()

                previousLat += deltaLat
                previousLon += deltaLon

                val lat = previousLat.toCoordinateDouble()
                val lon = previousLon.toCoordinateDouble()

                coordinateFactory.create(lat = lat, lon = lon)
            } else {
                null
            }
        }
    }

    private fun StringBuilder.appendEncodedValue(value: Int) {
        // Left-shift the binary value one bit.
        // If the original decimal value is negative, invert this encoding.
        val actualValue = if (value < 0) (value shl 1).inv() else value shl 1

        // Break the binary value out into 5-bit chunks (starting from the right hand side).
        // Place the 5-bit chunks into reverse order.
        var remainingBits = actualValue

        do {
            val lastFiveBits = remainingBits and 0b11111

            remainingBits = remainingBits shr 5

            // OR each value with 0x20 if another bit chunk follows
            // Convert each value to decimal
            val chunk = if (remainingBits != 0) lastFiveBits or 0x20 else lastFiveBits

            // Add 63 to each value.
            // Convert each value to its ASCII equivalent.
            append((chunk + 63).toChar())
        } while (remainingBits != 0)
    }

    private fun Iterator<Char>.readEncodedValue(): Int {
        var value = 0
        var shiftPos = 0

        do {
            val chunk = next().code - 63
            val isLast = (chunk and 0x20) == 0

            value = value or ((chunk and 0b11111) shl shiftPos)
            shiftPos += 5
        } while (!isLast)

        return if (value and 0x1 > 0) value.inv() shr 1 else value shr 1
    }

    private fun Double.toCoordinateInt() = (this * 1e5).roundToInt()

    private fun Int.toCoordinateDouble() = this / 1e5
}

public fun interface PolylineFactory<P : Polyline, C : Coordinate> {
    public fun create(coordinates: Sequence<C>): P
}

public fun interface CoordinateFactory<C : Coordinate> {
    public fun create(lat: Double, lon: Double): C
}
